package com.example.nirs

import android.graphics.Bitmap
import android.util.Log

class Encryption {
    fun encodeImage(image: Bitmap, message: String): Bitmap {
        val width = image.width
        val height = image.height
        var index = 0
        val mutableBitmap: Bitmap = image.copy(Bitmap.Config.ARGB_8888, true)
        val binaryMessage = message.toByteArray().joinToString(separator = "") { String.format("%8s", Integer.toBinaryString(it.toInt() and 0xFF)).replace(' ', '0') }
        for (x in 0 until width) {
            for (y in 0 until height) {
                if (index < binaryMessage.length) {
                    val pixel = mutableBitmap.getPixel(x, y)
                    val newPixel = if (binaryMessage[index] == '1') pixel or 1 else pixel and 0xFFFFFFFE.toInt()
                    mutableBitmap.setPixel(x, y, newPixel)
                    index++
                } else {
                    break
                }
            }
        }

        return mutableBitmap
    }

    fun decodeImage(image: Bitmap,length:Int): String {
        val width = image.width
        val height = image.height
        val binaryMessage = StringBuilder()

        for (x in 0 until width) {
            for (y in 0 until height) {
                val pixel = image.getPixel(x, y)
                binaryMessage.append(pixel and 1)
            }
        }

        val messageBytes = ByteArray(length)
        for (i in messageBytes.indices) {
            messageBytes[i] = Integer.parseInt(binaryMessage.substring(i * 8, (i + 1) * 8), 2).toByte()
        }

        return String(messageBytes)
    }
}