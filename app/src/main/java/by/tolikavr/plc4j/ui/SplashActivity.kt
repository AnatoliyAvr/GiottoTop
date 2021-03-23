package by.tolikavr.plc4j.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import by.tolikavr.plc4j.databinding.SplashBinding
import by.tolikavr.plc4j.modbus.ConnectionPLC
import by.tolikavr.plc4j.utilits.APP_ACTIVITY
import by.tolikavr.plc4j.utilits.AppPreference
import com.serotonin.modbus4j.exception.ModbusInitException
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {

  private val activityScope = CoroutineScope(Dispatchers.Main)
  private var _binding: SplashBinding? = null
  private val binding get() = _binding!!

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    _binding = SplashBinding.inflate(layoutInflater)
    setContentView(binding.root)

    AppPreference.getPreference(applicationContext)

    activityScope.launch(Dispatchers.IO) {
      try {
        ConnectionPLC.initialization()
        ConnectionPLC.initMB()
      } catch (e: ModbusInitException) {
        Log.d("AAA", "dd")
      }
      delay(2000)
      val intent = Intent(this@SplashActivity, MainActivity::class.java)
      startActivity(intent)
      finish()
    }
  }
}