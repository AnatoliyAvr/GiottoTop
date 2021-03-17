package by.tolikavr.plc4j.ui.settings

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.tolikavr.plc4j.R
import by.tolikavr.plc4j.databinding.SettingsFragmentBinding
import by.tolikavr.plc4j.ui.start.TIME1
import by.tolikavr.plc4j.ui.start.TIME5
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

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = SettingsFragmentBinding.inflate(layoutInflater, container, false)
    val view = mBinding.root
    etIp = view.findViewById(R.id.et_ip)
    etPort = view.findViewById(R.id.et_port)
    etTimer5 = view.findViewById(R.id.et_timer5)
    etTimer1 = view.findViewById(R.id.et_timer1)
    return view
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
    setHasOptionsMenu(true)
    APP_ACTIVITY.setTitle(R.string.settings)
    etIp.filters = Utils.filters()

    etTimer5.setText(arguments?.getInt(TIME5).toString())
    etTimer1.setText(arguments?.getInt(TIME1).toString())
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
        AppPreference.setIp(etIp.text.toString())
        AppPreference.setPort(etPort.text.toString().toInt())
        val bundle = Bundle()
        bundle.putString(TIME5, etTimer5.text.toString())
        bundle.putString(TIME1, etTimer1.text.toString())
        APP_ACTIVITY.navController.navigate(R.id.action_settingsFragment_to_startFragment, bundle)
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }

}