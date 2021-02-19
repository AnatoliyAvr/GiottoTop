package by.tolikavr.plc4j.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.tolikavr.plc4j.R
import by.tolikavr.plc4j.databinding.StartFragmentBinding
import com.serotonin.modbus4j.ModbusFactory
import com.serotonin.modbus4j.code.DataType
import com.serotonin.modbus4j.ip.IpParameters
import com.serotonin.modbus4j.locator.BaseLocator
import com.skydoves.powerspinner.PowerSpinnerView


class StartFragment : Fragment() {

  private var _binding: StartFragmentBinding? = null
  private val mBinding get() = _binding!!
  private lateinit var viewModel: StartViewModel
  private lateinit var powerSpinnerView: PowerSpinnerView
  private lateinit var btnValve1: Button
  private lateinit var btnValve2: Button
  private lateinit var btnValve3: Button

  val ipParameters: IpParameters = IpParameters().apply {
    host = "192.168.122.85"
    port = 502
  }
  val modbusFactory = ModbusFactory()
  val master = modbusFactory.createTcpMaster(ipParameters, true)

//  val loc = BaseLocator.holdingRegister(1, 0, DataType.TWO_BYTE_INT_UNSIGNED)
  val loc = BaseLocator.coilStatus(1, 0 )


  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = StartFragmentBinding.inflate(layoutInflater, container, false)
    val view = mBinding.root
    powerSpinnerView = view.findViewById(R.id.spinner_mode)
    btnValve1 = view.findViewById(R.id.btn_valve1)
    btnValve2 = view.findViewById(R.id.btn_valve2)
    btnValve3 = view.findViewById(R.id.btn_valve3)
    return view
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this).get(StartViewModel::class.java)

//    val connection = Connection



//    Log.d("AAA", connection.master.getValue(connection.loc).toString())
    Log.d("AAA", master.getValue(loc).toString())

    powerSpinnerView.setOnSpinnerDismissListener {
      when (powerSpinnerView.selectedIndex) {
        0 -> {
          btnValve1.isVisible = false
          btnValve2.setText(R.string.start)
          btnValve3.isVisible = false
        }
        1 -> {
          btnValve1.isVisible = true
          btnValve2.setText(R.string.valve2)
          btnValve3.isVisible = true
        }
      }
    }
    //mBinding.tvMode.setText(R.string.modeAuto)
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }

}