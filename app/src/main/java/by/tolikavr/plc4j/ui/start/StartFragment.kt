package by.tolikavr.plc4j.ui.start

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.tolikavr.plc4j.R
import by.tolikavr.plc4j.databinding.StartFragmentBinding
import by.tolikavr.plc4j.modbus.Connection
import by.tolikavr.plc4j.utilits.APP_ACTIVITY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class StartFragment : Fragment() {

  private var _binding: StartFragmentBinding? = null
  private val mBinding get() = _binding!!
  private lateinit var viewModel: StartViewModel

  //  private lateinit var powerSpinnerView: PowerSpinnerView
  private lateinit var btnValve1: Button
  private lateinit var btnValve2: Button
  private lateinit var btnValve3: Button
  private lateinit var ivAir1: ImageView
  private lateinit var ivAir2: ImageView
  private lateinit var ivAir3: ImageView
  private lateinit var description: TextView
  private lateinit var ivValve: ImageView

  private lateinit var connection: Connection

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = StartFragmentBinding.inflate(layoutInflater, container, false)
    val view = mBinding.root
//    powerSpinnerView = view.findViewById(R.id.spinner_mode)
    btnValve1 = view.findViewById(R.id.btn_valve1)
    btnValve2 = view.findViewById(R.id.btn_valve2)
    btnValve3 = view.findViewById(R.id.btn_valve3)
    ivAir1 = view.findViewById(R.id.iv_air1)
    ivAir2 = view.findViewById(R.id.iv_air2)
    ivAir3 = view.findViewById(R.id.iv_air3)
    description = view.findViewById(R.id.tv_description)
    ivValve = view.findViewById(R.id.iv_valve)

    return view
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this).get(StartViewModel::class.java)

    setHasOptionsMenu(true)
    APP_ACTIVITY.setTitle(R.string.modeAuto)
    initialization()

    GlobalScope.launch(Dispatchers.IO) {
      connection.initialization()
      while (true){

        Log.d("AAA", connection.getMaster().getValue(connection.loc).toString())
        delay(500)
      }
    }


//    powerSpinnerView.setOnSpinnerDismissListener {
//      when (powerSpinnerView.selectedIndex) {
//        0 -> {
//          btnValve1.isVisible = false
//          btnValve2.setText(R.string.start)
//          btnValve3.isVisible = false
//        }
//        1 -> {
//          btnValve1.isVisible = true
//          btnValve2.setText(R.string.valve2)
//          btnValve3.isVisible = true
//        }
//      }
//    }

    btnValve1.setOnClickListener {
      ivValve.setImageResource(R.drawable.valve2_open)
      description.setText(R.string.valveOpen)
      ivAir1.isVisible = true
      ivAir2.isVisible = false
      ivAir3.isVisible = false
    }

    btnValve2.setOnClickListener {
      ivValve.setImageResource(R.drawable.valve3_top_seat_flush)
      description.setText(R.string.valveTopSeatFlush)
      ivAir2.isVisible = true
      ivAir1.isVisible = false
      ivAir3.isVisible = false
    }

    btnValve3.setOnClickListener {
      ivValve.setImageResource(R.drawable.valve4_lower_seat_flush)
      description.setText(R.string.valveLowerSeatFlush)
      ivAir3.isVisible = true
      ivAir1.isVisible = false
      ivAir2.isVisible = false
    }
    //mBinding.tvMode.setText(R.string.modeAuto)
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.setting, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.btn_settings -> {
        APP_ACTIVITY.navController.navigate(R.id.action_startFragment_to_settingsFragment)
      }
    }
    return super.onOptionsItemSelected(item)
  }

  private fun initialization() {
    connection = Connection
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }

}