package by.tolikavr.plc4j.utilits

import android.content.Context
import android.content.SharedPreferences

object AppPreference {

  private const val INIT_IP = "initIp"
  private const val INIT_PORT = "initPort"
  private const val INIT_TIME5 = "initTime5"
  private const val INIT_TIME1 = "initTime1"
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

  fun setPort(init: String) {
    mPreferences.edit()
      .putString(INIT_PORT, init)
      .apply()
  }

  fun setTime5(init: String) {
    mPreferences.edit()
      .putString(INIT_TIME5, init)
      .apply()
  }

  fun setTime1(init: String) {
    mPreferences.edit()
      .putString(INIT_TIME1, init)
      .apply()
  }

  fun getIp(): String? {
    return mPreferences.getString(INIT_IP, "127.0.0.1")
  }

  fun getPort(): String? {
    return mPreferences.getString(INIT_PORT, "502")
  }

  fun getTime5(): String? {
    return mPreferences.getString(INIT_TIME5, "0")
  }

  fun getTime1(): String? {
    return mPreferences.getString(INIT_TIME1, "0")
  }

}