package by.tolikavr.plc4j

import androidx.appcompat.widget.Toolbar
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.tolikavr.plc4j.databinding.ActivityMainBinding
import by.tolikavr.plc4j.utilits.APP_ACTIVITY
import by.tolikavr.plc4j.utilits.AppPreference


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