package by.tolikavr.plc4j.ui.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import by.tolikavr.plc4j.R
import by.tolikavr.plc4j.databinding.SettingsFragmentBinding
import by.tolikavr.plc4j.utilits.APP_ACTIVITY
import by.tolikavr.plc4j.utilits.AppPreference
import by.tolikavr.plc4j.utilits.Utils

class SettingsFragment : Fragment() {

  private var _binding: SettingsFragmentBinding? = null
  private val mBinding get() = _binding!!
  private lateinit var viewModel: SettingsViewModel
  private lateinit var etIp: EditText
  private lateinit var etPort: EditText
  private lateinit var etTimer5: EditText
  private lateinit var etTimer1: EditText
  private lateinit var saveIpPort: Button
  private lateinit var saveSetTime: Button
  private lateinit var defaultIpPort: Button
  private lateinit var defaultTime: Button

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = SettingsFragmentBinding.inflate(layoutInflater, container, false)
    val view = mBinding.root
    etIp = view.findViewById(R.id.et_ip)
    etPort = view.findViewById(R.id.et_port)
    saveIpPort = view.findViewById(R.id.save_ip_port)
    defaultIpPort = view.findViewById(R.id.default_ip_port)
    etTimer5 = view.findViewById(R.id.et_timer5)
    etTimer1 = view.findViewById(R.id.et_timer1)
    saveSetTime = view.findViewById(R.id.save_set_time)
    defaultTime = view.findViewById(R.id.default_time)
    return view
  }

  @SuppressLint("ResourceType")
  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
    setHasOptionsMenu(true)
    APP_ACTIVITY.setTitle(R.string.settings)
    etIp.filters = Utils.filters()

    saveIpPort.setOnClickListener {
      AppPreference.setIp(etIp.text.toString())
      AppPreference.setPort(etPort.text.toString())
    }
    saveSetTime.setOnClickListener {
      AppPreference.setTime5(etTimer5.text.toString())
      AppPreference.setTime1(etTimer1.text.toString())
    }

    defaultIpPort.setOnClickListener {
      AppPreference.setIp(resources.getString(R.string.default_ip))
      AppPreference.setPort(resources.getString(R.string.default_port))
      etIp.setText(AppPreference.getIp())
      etPort.setText(AppPreference.getPort())
    }
    defaultTime.setOnClickListener {
      AppPreference.setTime5(resources.getString(R.string.default_time5))
      AppPreference.setTime1(resources.getString(R.string.default_time1))
      etTimer5.setText(AppPreference.getTime5())
      etTimer1.setText(AppPreference.getTime1())
    }
  }

  override fun onStart() {
    super.onStart()
    etIp.setText(AppPreference.getIp())
    etPort.setText(AppPreference.getPort().toString())
    etTimer5.setText(AppPreference.getTime5().toString())
    etTimer1.setText(AppPreference.getTime1().toString())
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

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}