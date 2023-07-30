package ru.kizapp.ftpclient.utils

import java.text.NumberFormat

private val numberFormat by lazy { NumberFormat.getInstance() }
private const val BYTES_SIZE = 1024

fun getFormattedFileSize(size: Long): String {
    if (size <= BYTES_SIZE) {
        return numberFormat.format(size) + " b"
    }
    val kiloBytes = size / BYTES_SIZE
    if (kiloBytes <= BYTES_SIZE) {
        return numberFormat.format(kiloBytes) + " kB"
    }

    val megaBytes = kiloBytes / BYTES_SIZE
    if (megaBytes <= BYTES_SIZE) {
        return numberFormat.format(megaBytes) + "MB"
    }

    val gigaBytes = megaBytes / BYTES_SIZE
    if (gigaBytes <= BYTES_SIZE) {
        return numberFormat.format(gigaBytes) + "GB"
    }
    return numberFormat.format(size)
}
