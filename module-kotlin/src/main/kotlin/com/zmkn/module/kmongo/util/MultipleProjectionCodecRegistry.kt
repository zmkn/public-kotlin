package com.zmkn.module.kmongo.util

import com.zmkn.module.kmongo.base.PathMap
import com.zmkn.module.kmongo.model.MultipleProjection
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import kotlin.reflect.KClass

private fun getMapPath(paths: List<Pair<String, KClass<*>>>): PathMap {
    val pathMap = PathMap()
    paths.forEach { (p, k) ->
        val split = p.split(".")
        var m = pathMap
        for (s in split.take(split.size - 1)) {
            m = m.getOrPut(s) { PathMap() } as PathMap
        }
        m[split.last()] = k
    }
    return pathMap
}

private fun BsonReader.decode(
    pathMap: PathMap,
    current: String,
    decoderContext: DecoderContext,
    registry: CodecRegistry,
    result: MutableMap<String, Any>
) {
    readStartDocument()
    while (readBsonType() != BsonType.END_OF_DOCUMENT) {
        val n = readName()
        val value = pathMap[n]
        val newPath = if (current.isEmpty()) n else "$current.$n"
        if (value is PathMap) {
            decode(value, newPath, decoderContext, registry, result)
        } else {
            val d = registry.get((value as KClass<*>).java).decode(this, decoderContext)
            result[newPath] = d
        }
    }
    readEndDocument()
}

fun multipleProjectionCodecRegistry(
    properties: List<Pair<String, KClass<*>>>,
    baseRegistry: CodecRegistry
): CodecRegistry {
    val pathMap = getMapPath(properties)
    return CodecRegistries.fromRegistries(
        CodecRegistries.fromCodecs(
            object : Codec<MultipleProjection<*>> {
                override fun getEncoderClass(): Class<MultipleProjection<*>> = MultipleProjection::class.java

                override fun encode(
                    writer: BsonWriter,
                    value: MultipleProjection<*>,
                    encoderContext: EncoderContext
                ) {
                    throw IllegalStateException("not supported")
                }

                override fun decode(reader: BsonReader, decoderContext: DecoderContext): MultipleProjection<*> {
                    val result: MutableMap<String, Any> = mutableMapOf()
                    reader.decode(
                        pathMap,
                        "",
                        decoderContext,
                        baseRegistry,
                        result
                    )
                    return MultipleProjection(result)
                }
            }
        ),
        baseRegistry
    )
}
