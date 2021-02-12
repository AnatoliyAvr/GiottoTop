package by.tolikavr.plc4j

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.tolikavr.plc4j.databinding.ActivityMainBinding
import by.tolikavr.plc4j.utilits.APP_ACTIVITY


class MainActivity : AppCompatActivity() {

  lateinit var navController: NavController
  private var _binding: ActivityMainBinding? = null
  private val mBinding get() = _binding!!

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    _binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(mBinding.root)
    APP_ACTIVITY = this
    navController = Navigation.findNavController(this, R.id.nav_host_fragment)
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}