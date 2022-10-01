package com.app.general.pixage.common.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.gson.Gson
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

val gson = Gson()

/**
 * Converts a class to a Map<String, Any>
 */
fun <T> T.serializeToMap(): Map<String, Any> {
    val json = gson.toJson(this)
    return jsonToMap(json)
}

/**
 * Converts a json string to Map<String, Any>
 * @param json : String
 * @return Map<String, Any>
 */
private fun jsonToMap(json: String): Map<String, Any> {
    val doubles = gson.fromJson<Map<String, Any>>(json, Map::class.java)
    fun doublesToLong(doubles: Map<String, Any>): Map<String, Any> = doubles
        .map { entry ->
            Pair(entry.key, entry.value.let {
                when (it) {
                    is Map<*, *> -> doublesToLong(it as Map<String, Any>)
                    is Double -> it.toLong()
                    else -> it
                }
            })
        }
        .toMap()
    return doublesToLong(doubles)
}

fun encodeStringToUrl(query: String): String? {
    return try {
        return URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
        null
    }
}

fun calculateOffset(page: Int?, perPage: Int?): Int {
    perPage?.let {
        val index = page ?: 1
        return (index - 1) * perPage
    }
    return 1
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val activeNetwork =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}