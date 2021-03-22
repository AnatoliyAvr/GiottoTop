package by.tolikavr.plc4j

import android.app.Application

open class App : Application() {

  private var initStart = true

  fun getInitStart() = initStart

  fun setInitStart(b: Boolean) {
    initStart = b
  }
}
