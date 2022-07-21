package com.sti.sticanteen.utils

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefWrapper @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) {

    fun storeKeyValue(key: String, value: String) =
        sharedPreferences.edit().putString(key, value).apply()

    fun removeKeyValue(key: String) = sharedPreferences.edit().remove(key).apply()

    fun readKeyValueString(key: String) = sharedPreferences.getString(key, "")

    fun readKeyValueInt(key: String) = sharedPreferences.getInt(key, 0)

    fun clearValues() = sharedPreferences.edit().clear().apply()

}