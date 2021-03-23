package by.tolikavr.plc4j.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.tolikavr.plc4j.R
import by.tolikavr.plc4j.databinding.ActivityMainBinding
import by.tolikavr.plc4j.modbus.ConnectionPLC
import by.tolikavr.plc4j.utilits.APP_ACTIVITY
import by.tolikavr.plc4j.utilits.AppPreference
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

  lateinit var toolbar: Toolbar
  lateinit var navController: NavController
  private var _binding: ActivityMainBinding? = null
  private val mBinding get() = _binding!!

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    _binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(mBinding.root)
    APP_ACTIVITY = this
    navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    toolbar = mBinding.toolbar
    setSupportActionBar(toolbar)
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}