package com.nomad88.taglib.android.demo

import java.io.InputStream
import java.io.OutputStream

object StreamUtils {

    fun copyStream(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (true) {
            length = source.read(buf)
            if (length <= 0) break
            target.write(buf, 0, length)
        }
    }
}
