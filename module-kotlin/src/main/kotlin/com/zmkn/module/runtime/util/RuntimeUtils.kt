package com.zmkn.module.runtime.util

import com.zmkn.module.runtime.listener.OnShutdownListener

object RuntimeUtils {
    private val _runtime: Runtime = Runtime.getRuntime()
    private val _shutdownHooks: MutableList<Thread> = mutableListOf()
    private val _onShutdownListeners: MutableList<OnShutdownListener> = mutableListOf()

    val shutdownHooks: List<Thread> = _shutdownHooks
    val onShutdownListeners: List<OnShutdownListener> = _onShutdownListeners

    init {
        addShutdownHook(
            Thread {
                _onShutdownListeners.forEach {
                    it.onShutdown()
                }
            }
        )
    }

    fun addShutdownHook(hook: Thread) = _runtime.addShutdownHook(hook).also {
        _shutdownHooks.add(hook)
    }

    fun removeShutdownHook(hook: Thread) = _runtime.removeShutdownHook(hook).also {
        _shutdownHooks.remove(hook)
    }

    fun addShutdownListener(onShutdownListener: OnShutdownListener) = _onShutdownListeners.add(onShutdownListener)

    fun addShutdownListener(
        index: Int,
        onShutdownListener: OnShutdownListener,
    ) = _onShutdownListeners.add(index, onShutdownListener)

    fun addFirstShutdownListener(onShutdownListener: OnShutdownListener) = _onShutdownListeners.addFirst(onShutdownListener)

    fun removeShutdownListener(onShutdownListener: OnShutdownListener): Boolean = _onShutdownListeners.remove(onShutdownListener)

    fun clearShutdownListener() = _onShutdownListeners.clear()
}
