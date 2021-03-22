package by.tolikavr.plc4j.ui.start

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StartViewModel : ViewModel() {

  private val _initStart = MutableStateFlow(true)
  val initStart: StateFlow<Boolean> = _initStart.asStateFlow() // только для чтения

  fun setInitStart(state: Boolean) {
    _initStart.value = state
  }

}