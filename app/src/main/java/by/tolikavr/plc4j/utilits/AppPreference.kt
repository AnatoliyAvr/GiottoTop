package by.tolikavr.plc4j.utilits

import android.content.Context
import android.content.SharedPreferences

object AppPreference {

  private const val INIT_IP = "initIp"
  private const val INIT_PORT = "initPort"
  private const val NAME_PREF = "preference"

  private lateinit var mPreferences: SharedPreferences

  fun getPreference(context: Context): SharedPreferences {
    mPreferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE)
    return mPreferences
  }

  fun setIp(init: String) {
    mPreferences.edit()
      .putString(INIT_IP, init)
      .apply()
  }

  fun setPort(init: Int) {
    mPreferences.edit()
      .putInt(INIT_PORT, init)
      .apply()
  }

  fun getIp(): String? {
    return mPreferences.getString(INIT_IP, "127.0.0.1")
  }

  fun getPort(): Int {
    return mPreferences.getInt(INIT_PORT, 502)
  }

}