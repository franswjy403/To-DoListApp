package com.dicoding.todoapp.utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceManager {
    private const val PREF_NAME = "MyAppPreferences"
    private const val KEY_NOTIFICATION_ENABLED = "notificationEnabled"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun setNotificationEnabled(context: Context, enabled: Boolean) {
        val editor = getSharedPreferences(context).edit()
        editor.putBoolean(KEY_NOTIFICATION_ENABLED, enabled)
        editor.apply()
    }

    fun isNotificationEnabled(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_NOTIFICATION_ENABLED, false)
    }
}