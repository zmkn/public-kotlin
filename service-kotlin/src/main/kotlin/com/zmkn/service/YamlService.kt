package com.zmkn.service

import com.zmkn.serialization.jackson.Jackson
import org.snakeyaml.engine.v2.api.Dump
import org.snakeyaml.engine.v2.api.DumpSettings
import org.snakeyaml.engine.v2.api.Load
import org.snakeyaml.engine.v2.api.LoadSettings
import org.snakeyaml.engine.v2.common.ScalarStyle
import java.io.*

class YamlService {
    private val _objectMapper = Jackson.objectMapper

    private val _loadSettings: LoadSettings
    private val _load: Load
    private val _dumpSettings: DumpSettings
    private val _dump: Dump

    constructor() : this(
        loadSettings = LoadSettings.builder().build(),
        dumpSettings = DumpSettings.builder().setDefaultScalarStyle(ScalarStyle.DOUBLE_QUOTED).build()
    )

    constructor(loadSettings: LoadSettings) : this(
        loadSettings = loadSettings,
        dumpSettings = DumpSettings.builder().setDefaultScalarStyle(ScalarStyle.DOUBLE_QUOTED).build()
    )

    constructor(dumpSettings: DumpSettings) : this(
        loadSettings = LoadSettings.builder().build(),
        dumpSettings = dumpSettings
    )

    constructor(loadSettings: LoadSettings, dumpSettings: DumpSettings) {
        _loadSettings = loadSettings
        _dumpSettings = dumpSettings
        _load = Load(_loadSettings)
        _dump = Dump(_dumpSettings)
    }

    @Suppress("UNCHECKED_CAST")
    fun merge(destination: Map<*, *>, vararg sources: Map<*, *>): Map<*, *> {
        return if (sources.isEmpty()) {
            destination
        } else {
            val newDestination = destination.toMutableMap()
            sources.forEach { source ->
                for ((key, value) in source) {
                    newDestination[key] = if (value is Map<*, *>) {
                        val destValue = newDestination[key] as? Map<Any, Any> ?: mapOf()
                        merge(destValue, value)
                    } else {
                        value
                    }
                }
            }
            newDestination
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun merge(destination: Iterable<*>, vararg sources: Iterable<*>): Iterable<*> {
        return if (sources.isEmpty()) {
            destination
        } else {
            val newDestination = destination.toMutableList()
            sources.forEach { source ->
                source.forEachIndexed { index, value ->
                    val newDestinationValue = if (value is Map<*, *>) {
                        val destValue = newDestination.getOrNull(index) as? Map<Any, Any> ?: mapOf()
                        merge(destValue, value)
                    } else {
                        value
                    }
                    if (index in 0 until newDestination.size) {
                        newDestination[index] = newDestinationValue
                    } else {
                        newDestination.add(newDestinationValue)
                    }
                }
            }
            newDestination
        }
    }

    fun load(yamlStream: InputStream): Any {
        return _load.loadFromInputStream(yamlStream).also {
            yamlStream.close()
        }
    }

    fun load(yamlReader: Reader): Any {
        return _load.loadFromReader(yamlReader).also {
            yamlReader.close()
        }
    }

    fun load(yaml: String): Any {
        return _load.loadFromString(yaml)
    }

    fun loadAll(yamlStream: InputStream): Iterable<*> {
        return _load.loadAllFromInputStream(yamlStream).toList().also {
            yamlStream.close()
        }
    }

    fun loadAll(yamlReader: Reader): Iterable<*> {
        return _load.loadAllFromReader(yamlReader).toList().also {
            yamlReader.close()
        }
    }

    fun loadAll(yaml: String): Iterable<*> {
        return _load.loadAllFromString(yaml).toList()
    }

    fun <T : Any> convert(yamlMap: Map<*, *>, targetType: Class<T>): T {
        return _objectMapper.convertValue(yamlMap, targetType)
    }

    fun <T : Any> convert(yamlIterable: Iterable<*>, targetType: Class<T>): Iterable<*> {
        return yamlIterable.filterNotNull().mapNotNull {
            when (it) {
                is Map<*, *> -> {
                    convert(it, targetType)
                }

                is Iterable<*> -> {
                    convert(it, targetType)
                }

                else -> {
                    null
                }
            }
        }
    }

    fun convertToMap(data: Any): Map<*, *> {
        return _objectMapper.convertValue(data, Map::class.java)
    }

    fun convertToIterable(data: Iterable<*>): Iterable<*> {
        return _objectMapper.convertValue(data, Iterable::class.java)
    }

    fun <T : Any> read(targetType: Class<T>, vararg yamlInputStreams: InputStream): T {
        if (yamlInputStreams.isEmpty()) {
            throw IllegalArgumentException("Read YAML data exception, YAML input stream must not be empty.")
        }
        val yamlDataList = yamlInputStreams.map { yamlInputStream ->
            val yamlData = load(yamlInputStream)
            if (yamlData is Map<*, *>) {
                yamlData
            } else {
                throw IOException("Read YAML data exception, it is not map type.")
            }
        }
        val yamlData = if (yamlDataList.size > 1) {
            merge(yamlDataList[0], *yamlDataList.drop(1).toTypedArray())
        } else {
            yamlDataList[0]
        }
        return convert(yamlData, targetType)
    }

    fun <T : Any> read(targetType: Class<T>, vararg yamlFiles: File): T {
        if (yamlFiles.isEmpty()) {
            throw IllegalArgumentException("Read YAML data exception, YAML file must not be empty.")
        }
        val yamlInputStreams = yamlFiles.map { yamlFile ->
            FileInputStream(yamlFile)
        }
        return read(targetType, *yamlInputStreams.toTypedArray())
    }

    fun <T : Any> read(targetType: Class<T>, vararg yamlFileAbsolutePaths: String): T {
        if (yamlFileAbsolutePaths.isEmpty()) {
            throw IllegalArgumentException("Read YAML data exception, YAML file absolute path must not be empty.")
        }
        val yamlInputStreams = yamlFileAbsolutePaths.map { yamlPath ->
            FileInputStream(yamlPath)
        }
        return read(targetType, *yamlInputStreams.toTypedArray())
    }

    fun <T : Any> readList(targetType: Class<T>, vararg yamlInputStreams: InputStream): Iterable<*> {
        if (yamlInputStreams.isEmpty()) {
            throw IllegalArgumentException("Read YAML data exception, YAML input stream must not be empty.")
        }
        val yamlDataList = yamlInputStreams.map { yamlInputStream ->
            val yamlData = load(yamlInputStream)
            if (yamlData is Iterable<*>) {
                yamlData
            } else {
                throw IOException("Read YAML data exception, it is not iterable type.")
            }
        }
        val yamlListData = if (yamlDataList.size > 1) {
            merge(yamlDataList[0], *yamlDataList.drop(1).toTypedArray())
        } else {
            yamlDataList[0]
        }
        return convert(yamlListData, targetType)
    }

    fun <T : Any> readList(targetType: Class<T>, vararg yamlFiles: File): Iterable<*> {
        if (yamlFiles.isEmpty()) {
            throw IllegalArgumentException("Read YAML data exception, YAML file must not be empty.")
        }
        val yamlInputStreams = yamlFiles.map { yamlFile ->
            FileInputStream(yamlFile)
        }
        return readList(targetType, *yamlInputStreams.toTypedArray())
    }

    fun <T : Any> readList(targetType: Class<T>, vararg yamlFileAbsolutePaths: String): Iterable<*> {
        if (yamlFileAbsolutePaths.isEmpty()) {
            throw IllegalArgumentException("Read YAML data exception, YAML file absolute path must not be empty.")
        }
        val yamlInputStreams = yamlFileAbsolutePaths.map { yamlPath ->
            FileInputStream(yamlPath)
        }
        return readList(targetType, *yamlInputStreams.toTypedArray())
    }

    fun dump(yamlData: Any): String {
        return _dump.dumpToString(yamlData)
    }

    fun dumpAll(yamlDataIterator: Iterator<*>): String {
        return _dump.dumpAllToString(yamlDataIterator)
    }

    fun write(yamlFile: File, data: Any) {
        val yamlData = convertToMap(data)
        val output = dump(yamlData)
        yamlFile.parentFile?.mkdirs()
        yamlFile.writeText(output, charset = Charsets.UTF_8)
    }

    fun write(yamlFileAbsolutePath: String, data: Any) {
        val yamlFile = File(yamlFileAbsolutePath)
        write(yamlFile, data)
    }

    fun writeList(yamlFile: File, data: Iterable<*>) {
        val yamlData = convertToIterable(data)
        val output = dump(yamlData)
        yamlFile.parentFile?.mkdirs()
        yamlFile.writeText(output, charset = Charsets.UTF_8)
    }

    fun writeList(yamlFileAbsolutePath: String, data: Iterable<*>) {
        val yamlFile = File(yamlFileAbsolutePath)
        writeList(yamlFile, data)
    }

    fun writeAll(yamlFile: File, data: Iterator<*>) {
        val newData = mutableListOf<Any>()
        while (data.hasNext()) {
            data.next()?.let {
                newData.add(
                    if (it is Iterable<*>) {
                        convertToIterable(it)
                    } else {
                        convertToMap(it)
                    }
                )
            }
        }
        val output = dumpAll(newData.iterator())
        yamlFile.parentFile?.mkdirs()
        yamlFile.writeText(output, charset = Charsets.UTF_8)
    }

    fun writeAll(yamlFileAbsolutePath: String, data: Iterator<*>) {
        val yamlFile = File(yamlFileAbsolutePath)
        writeAll(yamlFile, data)
    }
}
