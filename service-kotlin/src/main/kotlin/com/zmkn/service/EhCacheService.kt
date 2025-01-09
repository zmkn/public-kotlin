package com.zmkn.service

import org.ehcache.CacheManager
import org.ehcache.Status
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.xml.XmlConfiguration
import java.io.File
import java.net.URL

class EhCacheService {
    private val _cacheManager: CacheManager

    constructor(configUrl: URL) {
        val xmlConfiguration = XmlConfiguration(configUrl)
        _cacheManager = CacheManagerBuilder.newCacheManager(xmlConfiguration)
        _cacheManager.init()
    }

    constructor(configFile: File) : this(configFile.toURI().toURL())

    constructor(configFileAbsolutePath: String) : this(File(configFileAbsolutePath))

    val cacheManager: CacheManager
        get() = _cacheManager

    fun stop(): CacheManager {
        if (_cacheManager.status != Status.UNINITIALIZED) {
            _cacheManager.close()
        }
        return _cacheManager
    }

    fun activation(): CacheManager {
        if (_cacheManager.status != Status.AVAILABLE) {
            _cacheManager.init()
        }
        return _cacheManager
    }
}
