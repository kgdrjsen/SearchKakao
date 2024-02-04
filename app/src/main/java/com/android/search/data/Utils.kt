package com.android.search.data

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

object Utils {
    fun getDateFromTimestampWithFormat(
        timestamp: String?,
        fromFormatformat: String?,
        toFormatformat: String?
    ): String {
        var date: Date? = null
        var res = ""
        try {
            val format = SimpleDateFormat(fromFormatformat)
            date = format.parse(timestamp)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        Log.d("jbdate", "getDateFromTimestampWithFormat date >> $date")

        val df = SimpleDateFormat(toFormatformat)
        res = df.format(date)
        return res
    }

    fun addPrefItem(context: Context, item: KakaoImage) {
        val prefs = context.getSharedPreferences("pref", Activity.MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = GsonBuilder().create()
        editor.putString(item.url, gson.toJson(item))
        editor.apply()
    }

    fun deletePrefItem(context: Context, url: String) {
        val prefs = context.getSharedPreferences("pref", Activity.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove(url)
        editor.apply()
    }

    fun getPrefBookmarkItems(context: Context): ArrayList<KakaoImage> {
        val prefs = context.getSharedPreferences("pref", Activity.MODE_PRIVATE)
        val allEntries: Map<String, *> = prefs.all
        val bookmarkItems = ArrayList<KakaoImage>()
        val gson = GsonBuilder().create()
        for ((key, value) in allEntries) {
            val item = gson.fromJson(value as String, KakaoImage::class.java)
            bookmarkItems.add(item)
        }
        return bookmarkItems
    }

    fun String.setTime() : String {
        val receiveTime = OffsetDateTime.parse(this)
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return dateFormat.format(receiveTime)
    }
}