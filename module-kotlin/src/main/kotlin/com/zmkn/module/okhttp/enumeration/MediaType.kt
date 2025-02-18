package com.zmkn.module.okhttp.enumeration

import okhttp3.MediaType.Companion.toMediaType

enum class MediaType(val value: okhttp3.MediaType) {
    APPLICATION_JSON("application/json;charset=UTF-8".toMediaType()),
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded".toMediaType()),
    MULTIPART_MIXED("multipart/mixed".toMediaType()),
    MULTIPART_DIGEST("multipart/digest".toMediaType()),
    MULTIPART_PARALLEL("multipart/parallel".toMediaType()),
    MULTIPART_FORM_DATA("multipart/form-data".toMediaType()),
    MULTIPART_ALTERNATIVE("multipart/alternative".toMediaType())
}
