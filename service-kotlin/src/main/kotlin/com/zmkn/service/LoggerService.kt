package com.zmkn.service

import com.zmkn.service.LoggerService.Level.*
import java.io.PrintWriter
import java.io.StringWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class LoggerService private constructor(private val config: Config) {
    private val _level: Level get() = getLevel() ?: config.level
    private val _stackIndex = STACK_INDEX + (getStackOffset() ?: config.stackOffset)

    private fun getAnsiColor(level: Level): AnsiColor {
        return when (level) {
            ERROR -> {
                AnsiColor.RED
            }

            WARN -> {
                AnsiColor.YELLOW
            }

            INFO -> {
                AnsiColor.GREEN
            }

            DEBUG -> {
                AnsiColor.BLUE
            }

            TRACE -> {
                AnsiColor.CYAN
            }

            OFF -> {
                AnsiColor.RED
            }
        }
    }

    private fun addColor(ansiColor: AnsiColor, text: String): String {
        val color = when (ansiColor) {
            AnsiColor.RED -> {
                ANSI_RED
            }

            AnsiColor.YELLOW -> {
                ANSI_YELLOW
            }

            AnsiColor.GREEN -> {
                ANSI_GREEN
            }

            AnsiColor.BLUE -> {
                ANSI_BLUE
            }

            AnsiColor.CYAN -> {
                ANSI_CYAN
            }
        }
        return "$color$text$ANSI_RESET"
    }

    private fun makeTop(): String {
        var top = ""
        for (i in 1..TOP_ITEM_COUNT) {
            if (i == 1) {
                top += TOP_LEFT_CORNER
            }
            top += TOP_ITEM
        }
        return top
    }

    private fun makeRow(): String {
        var row = ""
        for (i in 1..ROW_ITEM_COUNT) {
            if (i == 1) {
                row += ROW_LEFT_CORNER
            }
            row += ROW_ITEM
        }
        return row
    }

    private fun makeBottom(): String {
        var bottom = ""
        for (i in 1..BOTTOM_ITEM_COUNT) {
            if (i == 1) {
                bottom += BOTTOM_LEFT_CORNER
            }
            bottom += BOTTOM_ITEM
        }
        return bottom
    }

    private fun makeSummary(level: Level, stackOffset: Int): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDate = currentDateTime.format(formatter)
        val thread = Thread.currentThread()
        val tName = thread.name
        val tid = thread.threadId()
        val stackTrace = Throwable().stackTrace
        val stackTraceElement = stackTrace[_stackIndex + stackOffset]
        val fileName = stackTraceElement.fileName
        val className = stackTraceElement.className
        val methodName = stackTraceElement.methodName
        val lineNumber = stackTraceElement.lineNumber
        return "$formattedDate, [$tName] tid=$tid, [${level.value}] - at $className.$methodName($fileName:$lineNumber)"
    }

    private fun makeBody(ansiColor: AnsiColor, vararg messages: Any?): String {
        val top = addColor(ansiColor, makeTop())
        val row = addColor(ansiColor, makeRow())
        val bottom = addColor(ansiColor, makeBottom())
        val main = messages.joinToString("$_lineSeparator$row$_lineSeparator") {
            addColor(ansiColor, "$ROW_LEFT_BORDER${Formatter.anyToString(it)}")
        }
        return "$top$_lineSeparator$main$_lineSeparator$bottom"
    }

    private fun print(level: Level, stackOffset: Int, vararg messages: Any?) {
        if (level.code <= _level.code) {
            val summaryText = makeSummary(level, stackOffset)
            val bodyText = makeBody(getAnsiColor(level), summaryText, *messages)
            println(bodyText)
        }
    }

    private fun print(level: Level, vararg messages: Any?) = print(level, 0, *messages)
    private fun print(level: Level, stackOffset: Int, message: () -> Any?) = print(level, stackOffset + 1, message.invoke())
    private fun print(level: Level, message: () -> Any?) = print(level, 0, message)

    fun log(level: Level, stackOffset: Int, vararg messages: Any?) = print(level, stackOffset, *messages)
    fun log(level: Level, vararg messages: Any?) = print(level, *messages)
    fun log(level: Level, stackOffset: Int, message: () -> Any?) = print(level, stackOffset, message)
    fun log(level: Level, message: () -> Any?) = print(level, message)

    fun trace(stackOffset: Int, vararg messages: Any?) = print(TRACE, stackOffset, *messages)
    fun trace(vararg messages: Any?) = print(TRACE, *messages)
    fun trace(stackOffset: Int, message: () -> Any?) = print(TRACE, stackOffset, message)
    fun trace(message: () -> Any?) = print(TRACE, message)

    fun debug(stackOffset: Int, vararg messages: Any?) = print(DEBUG, stackOffset, *messages)
    fun debug(vararg messages: Any?) = print(DEBUG, *messages)
    fun debug(stackOffset: Int, message: () -> Any?) = print(DEBUG, stackOffset, message)
    fun debug(message: () -> Any?) = print(DEBUG, message)

    fun info(stackOffset: Int, vararg messages: Any?) = print(INFO, stackOffset, *messages)
    fun info(vararg messages: Any?) = print(INFO, *messages)
    fun info(stackOffset: Int, message: () -> Any?) = print(INFO, stackOffset, message)
    fun info(message: () -> Any?) = print(INFO, message)

    fun warn(stackOffset: Int, vararg messages: Any?) = print(WARN, stackOffset, *messages)
    fun warn(vararg messages: Any?) = print(WARN, *messages)
    fun warn(stackOffset: Int, message: () -> Any?) = print(WARN, stackOffset, message)
    fun warn(message: () -> Any?) = print(WARN, message)

    fun error(stackOffset: Int, vararg messages: Any?) = print(ERROR, stackOffset, *messages)
    fun error(vararg messages: Any?) = print(ERROR, *messages)
    fun error(stackOffset: Int, message: () -> Any?) = print(ERROR, stackOffset, message)
    fun error(message: () -> Any?) = print(ERROR, message)

    companion object {
        private const val ANSI_RED: String = "\u001B[31m"
        private const val ANSI_YELLOW: String = "\u001B[33m"
        private const val ANSI_GREEN: String = "\u001B[32m"
        private const val ANSI_BLUE: String = "\u001B[34m"
        private const val ANSI_CYAN: String = "\u001B[36m"
        private const val ANSI_RESET: String = "\u001B[0m"
        private const val ROW_ITEM: String = "┄"
        private const val ROW_ITEM_COUNT: Int = 200
        private const val ROW_LEFT_CORNER: String = "├"
        private const val ROW_LEFT_BORDER: String = "│ "
        private const val TOP_ITEM: String = "─"
        private const val TOP_ITEM_COUNT: Int = ROW_ITEM_COUNT
        private const val TOP_LEFT_CORNER: String = "┌"
        private const val BOTTOM_ITEM: String = TOP_ITEM
        private const val BOTTOM_ITEM_COUNT: Int = ROW_ITEM_COUNT
        private const val BOTTOM_LEFT_CORNER: String = "└"
        private const val STACK_INDEX: Int = 4
        private const val DEFAULT_LOG_KEY_PREFIX = "com.zmkn.service.LoggerService"
        private const val DEFAULT_LOG_LEVEL_KEY = "$DEFAULT_LOG_KEY_PREFIX.defaultLogLevel"
        private const val DEFAULT_LOG_STACK_OFFSET_KEY = "$DEFAULT_LOG_KEY_PREFIX.defaultStackOffset"
        private val _lineSeparator = System.lineSeparator()

        private val _instance by lazy {
            Builder().build()
        }

        private fun getProperty(key: String): String? {
            return System.getProperty(key)
        }

        private fun setProperty(key: String, value: String) {
            System.setProperty(key, value)
        }

        fun getInstance(): LoggerService {
            return _instance
        }

        fun getLevel(): Level? {
            return getProperty(DEFAULT_LOG_LEVEL_KEY)?.let {
                Level.fromValue(it)
            }
        }

        fun setLevel(level: Level) {
            setProperty(DEFAULT_LOG_LEVEL_KEY, level.value)
        }

        fun getStackOffset(): Int? {
            return getProperty(DEFAULT_LOG_STACK_OFFSET_KEY)?.toInt()
        }

        fun setStackOffset(stackOffset: Int) {
            setProperty(DEFAULT_LOG_STACK_OFFSET_KEY, stackOffset.toString())
        }
    }

    private enum class AnsiColor {
        RED,
        BLUE,
        CYAN,
        GREEN,
        YELLOW
    }

    enum class Level(val code: Int, val value: String) {
        OFF(0, "OFF"),
        ERROR(1, "ERROR"),
        WARN(2, "WARN"),
        INFO(3, "INFO"),
        DEBUG(4, "DEBUG"),
        TRACE(5, "TRACE");

        override fun toString(): String {
            return value
        }

        companion object {
            fun fromCode(code: Int): Level {
                return when (code) {
                    OFF.code -> OFF
                    ERROR.code -> ERROR
                    WARN.code -> WARN
                    INFO.code -> INFO
                    DEBUG.code -> DEBUG
                    TRACE.code -> TRACE
                    else -> throw IllegalArgumentException("Level code [$code] not recognized.")
                }
            }

            fun fromValue(value: String): Level {
                return when (value.uppercase()) {
                    OFF.value -> OFF
                    ERROR.value -> ERROR
                    WARN.value -> WARN
                    INFO.value -> INFO
                    DEBUG.value -> DEBUG
                    TRACE.value -> TRACE
                    else -> throw IllegalArgumentException("Level value [$value] not recognized.")
                }
            }
        }
    }

    data class Config(
        val level: Level = INFO,
        val stackOffset: Int = 0
    )

    class Builder {
        private var level: Level = INFO
        private var stackOffset: Int = 0

        fun setStackOffset(stackOffset: Int): Builder {
            this.stackOffset = stackOffset
            return this
        }

        fun build(): LoggerService {
            return LoggerService(
                Config(
                    level = level,
                    stackOffset = stackOffset
                )
            )
        }
    }

    object ThrowableUtils {
        private val _lineSeparator: String = System.lineSeparator()

        private fun getStackFrameList(throwable: Throwable): MutableList<String> {
            val sw = StringWriter()
            val pw = PrintWriter(sw, true)
            throwable.printStackTrace(pw)
            val stackTrace = sw.toString()
            val frames = StringTokenizer(stackTrace, _lineSeparator)
            val list: MutableList<String> = ArrayList()
            var traceStarted = false
            while (frames.hasMoreTokens()) {
                val token = frames.nextToken()
                // Determine if the line starts with <whitespace>at
                val at = token.indexOf("at")
                if (at != -1 && token.substring(0, at).trim { it <= ' ' }.isEmpty()) {
                    traceStarted = true
                    list.add(token)
                } else if (traceStarted) {
                    break
                }
            }
            return list
        }

        private fun removeCommonFrames(causeFrames: MutableList<String>, wrapperFrames: List<String>) {
            var causeFrameIndex = causeFrames.size - 1
            var wrapperFrameIndex = wrapperFrames.size - 1
            while (causeFrameIndex >= 0 && wrapperFrameIndex >= 0) {
                // Remove the frame from the cause trace if it is the same
                // as in the wrapper trace
                val causeFrame = causeFrames[causeFrameIndex]
                val wrapperFrame = wrapperFrames[wrapperFrameIndex]
                if (causeFrame == wrapperFrame) {
                    causeFrames.removeAt(causeFrameIndex)
                }
                causeFrameIndex--
                wrapperFrameIndex--
            }
        }

        fun getFullStackTrace(throwable: Throwable?): String {
            var t = throwable
            val throwableList: MutableList<Throwable> = ArrayList()
            while (t != null && !throwableList.contains(t)) {
                throwableList.add(t)
                t = t.cause
            }
            val size = throwableList.size
            val frames: MutableList<String> = ArrayList()
            var nextTrace = getStackFrameList(throwableList[size - 1])
            var i = size
            while (--i >= 0) {
                val trace = nextTrace
                if (i != 0) {
                    nextTrace = getStackFrameList(throwableList[i - 1])
                    removeCommonFrames(trace, nextTrace)
                }
                if (i == size - 1) {
                    frames.add(throwableList[i].toString())
                } else {
                    frames.add(" Caused by: " + throwableList[i].toString())
                }
                frames.addAll(trace)
            }
            val sb = StringBuilder()
            for (element in frames) {
                sb.append(element).append(_lineSeparator)
            }
            return sb.toString()
        }
    }

    object Formatter {
        private val _objectMapper = JacksonService.objectMapper

        private fun anyToJson(value: Any): String {
            return _objectMapper.writeValueAsString(value)
        }

        private fun mapToString(value: Map<*, *>): String {
            val map = value.mapValues { (_, v) -> allToString(v) }
            return map.toString()
        }

        private fun iterableToString(value: Iterable<*>): String {
            val list = value.map { v -> allToString(v) }
            return list.toTypedArray().contentToString()
        }

        private fun arrayToString(value: Array<*>): String {
            return iterableToString(value.toList())
        }

        private fun sequenceToString(value: Sequence<*>): String {
            return iterableToString(value.toList())
        }

        private fun allToString(value: Any?): String {
            return when (value) {
                is Array<*> -> {
                    arrayToString(value)
                }

                is Iterable<*> -> {
                    iterableToString(value)
                }

                is Sequence<*> -> {
                    sequenceToString(value)
                }

                is Map<*, *> -> {
                    mapToString(value)
                }

                is Throwable -> {
                    ThrowableUtils.getFullStackTrace(value)
                }

                is Boolean, is Byte, is Char, is Double, is Float, is Int, is Long, is Short -> {
                    value.toString()
                }

                else -> {
                    value?.let {
                        anyToJson(it)
                    } ?: "null"
                }
            }
        }

        fun anyToString(value: Any?): String {
            return allToString(value)
        }
    }
}
