package by.tolikavr.plc4j.ui.settings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import by.tolikavr.plc4j.R
import by.tolikavr.plc4j.utilits.Utils
import by.tolikavr.plc4j.databinding.SettingsFragmentBinding
import by.tolikavr.plc4j.utilits.APP_ACTIVITY
import by.tolikavr.plc4j.utilits.AppPreference

class SettingsFragment : Fragment() {

  private var _binding: SettingsFragmentBinding? = null
  private val mBinding get() = _binding!!
  private lateinit var viewModel: SettingsViewModel
  private lateinit var etIp: EditText
  private lateinit var etPort: EditText
  private val pref = AppPreference.getPreference(APP_ACTIVITY)

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = SettingsFragmentBinding.inflate(layoutInflater, container, false)
    val view = mBinding.root
    etIp = view.findViewById(R.id.et_ip)
    etPort = view.findViewById(R.id.et_port)
    return view
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
    setHasOptionsMenu(true)
    APP_ACTIVITY.setTitle(R.string.settings)
    etIp.filters = Utils.filters()
  }

  override fun onStart() {
    super.onStart()
    etIp.setText(AppPreference.getIp())
    etPort.setText(AppPreference.getPort().toString())
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.exit, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.btn_exit -> {
        APP_ACTIVITY.navController.navigate(R.id.action_settingsFragment_to_startFragment)
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onStop() {
    super.onStop()
    AppPreference.setIp(etIp.text.toString())
    AppPreference.setPort(etPort.text.toString().toInt())
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }

}