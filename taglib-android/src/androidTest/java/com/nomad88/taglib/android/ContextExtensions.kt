package com.nomad88.taglib.android

import android.content.Context
import java.io.File
import java.io.InputStream
import java.io.OutputStream

fun Context.inflateAsset(dstFolder: File, assetPath: String): File {
    val src = assets.open(assetPath)
    val fileName = assetPath.substringAfterLast("/", assetPath)
    val dst = File(dstFolder, fileName)
    dst.outputStream().use { copyStream(src, it) }
    src.close()
    return dst
}

fun Context.readAssetBytes(assetPath: String): ByteArray {
    return assets.open(assetPath).use { it.readBytes() }
}

private fun copyStream(source: InputStream, target: OutputStream) {
    val buf = ByteArray(8192)
    var length: Int
    while (true) {
        length = source.read(buf)
        if (length <= 0) break
        target.write(buf, 0, length)
    }
}
