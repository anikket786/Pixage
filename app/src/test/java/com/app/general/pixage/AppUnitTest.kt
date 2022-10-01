package com.app.general.pixage

import com.app.general.pixage.common.util.calculateOffset
import com.app.general.pixage.common.util.encodeStringToUrl
import com.app.general.pixage.common.util.serializeToMap
import com.app.general.pixage.images.domain.entity.ImagePostsParam
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class AppUnitTest {
    @Test
    fun urlEncodingIsCorrect() {
        val encodedText = encodeStringToUrl("this is url encoded ? &%#@")
        assertEquals(encodedText, "this+is+url+encoded+%3F+%26%25%23%40")
    }

    @Test
    fun serializeToMapIsCorrect() {
        val serializedObject = ImagePostsParam(
            searchText = "abc",
            page = 1,
            perPage = 20,
        ).serializeToMap()

        val map = mapOf(
            Pair<String, String?>("searchText", "abc"),
            Pair("imageType", "photo"),
            Pair<String, Int?>("page", 1),
            Pair<String, Int?>("perPage", 20),
        )

        assertEquals(serializedObject, map)
    }

    @Test
    fun calculateOffsetIsCorrect() {
        val offset1 = calculateOffset(1, 20)
        assertEquals(offset1, 0)

        val offset2 = calculateOffset(2, 20)
        assertEquals(offset2, 20)
    }
}