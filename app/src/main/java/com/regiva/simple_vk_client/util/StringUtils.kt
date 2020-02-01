package com.regiva.simple_vk_client.util

import java.text.SimpleDateFormat
import java.util.*

fun Long.convertToDateFormat(
    dateFormat: String = "dd MMMM yyyy"
): String = SimpleDateFormat(dateFormat, Locale.getDefault()).format(Date(this.times(1000)))