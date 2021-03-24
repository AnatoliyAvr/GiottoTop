package by.tolikavr.plc4j.ui.start

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.tolikavr.plc4j.App
import by.tolikavr.plc4j.R
import by.tolikavr.plc4j.databinding.StartFragmentBinding
import by.tolikavr.plc4j.modbus.ConnectionPLC
import by.tolikavr.plc4j.model.MbValue
import by.tolikavr.plc4j.utilits.APP_ACTIVITY
import by.tolikavr.plc4j.utilits.AppPreference
import com.serotonin.modbus4j.exception.ErrorResponseException
import com.serotonin.modbus4j.exception.ModbusInitException
import com.serotonin.modbus4j.exception.ModbusTransportException
import com.serotonin.modbus4j.locator.BaseLocator
import kotlinx.coroutines.*

@SuppressLint("ResourceAsColor")
class StartFragment : Fragment() {

  private val TAG = "StartFragment"
  private var _binding: StartFragmentBinding? = null
  private val mBinding get() = _binding!!
  private lateinit var viewModel: StartViewModel

  private lateinit var btnValve1: Button
  private lateinit var btnValve2: Button
  private lateinit var btnValve3: Button
  private lateinit var ivAir1: ImageView
  private lateinit var ivAir2: ImageView
  private lateinit var ivAir3: ImageView
  private lateinit var description: TextView
  private lateinit var ivValve: ImageView
  private lateinit var connection: ConnectionPLC
  private lateinit var job: Job
  private lateinit var app: App

  private var initBtn = true

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    app = context?.applicationContext as App
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = StartFragmentBinding.inflate(layoutInflater, container, false)
    val view = mBinding.root
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
    initialization()

    job = GlobalScope.launch(Dispatchers.IO) {
      while (true) {
        delay(100)
        when {
          app.getInitStart() -> {
            try {
              withContext(Dispatchers.Main) {
                btnValve1.visibility = View.GONE
                btnValve2.visibility = View.INVISIBLE
                btnValve3.visibility = View.GONE
                APP_ACTIVITY.setTitle(R.string.connecting)
              }
              connection.initialization(setHost = AppPreference.getIp(), setPort = AppPreference.getPort())
              descriptionStatus(R.color.gray_dark, R.string.valveClose)
            } catch (e: ModbusInitException) {
              descriptionStatus(Color.RED, R.string.no_connection, R.string.error_connection, true)
              Log.d(TAG, e.printStackTrace().toString())
              continue
            }
            app.setInitStart(false)
          }
          connection.getMaster().testSlaveNode(1) -> {
            try {
              connection.initMB()
              when {
                MbValue.getModeAuto -> {
                  withContext(Dispatchers.Main) {
                    APP_ACTIVITY.setTitle(R.string.modeAuto)
                    btnValve1.visibility = View.GONE
                    btnValve2.visibility = View.VISIBLE
                    btnValve2.setText(R.string.start)
                    btnValve3.visibility = View.GONE

                    ivAir1.isVisible = MbValue.getValve3
                    ivAir2.isVisible = MbValue.getValve1
                    ivAir3.isVisible = MbValue.getValve2
                  }
                  modeIndicator()
                }
                MbValue.getModeManual -> {
                  withContext(Dispatchers.Main) {
                    APP_ACTIVITY.setTitle(R.string.modeManual)
                    btnValve1.visibility = View.VISIBLE
                    btnValve2.visibility = View.VISIBLE
                    btnValve2.setText(R.string.valve2)
                    btnValve3.visibility = View.VISIBLE

                    ivAir1.isVisible = MbValue.getValve3
                    ivAir2.isVisible = MbValue.getValve1
                    ivAir3.isVisible = MbValue.getValve2
                  }
                  modeIndicator()
                }
                else -> {
                  withContext(Dispatchers.Main) {
                    APP_ACTIVITY.setTitle(R.string.modeOff)
                    btnValve1.visibility = View.GONE
                    btnValve2.visibility = View.INVISIBLE
                    btnValve3.visibility = View.GONE
                  }
                }

              }
            } catch (e: ErrorResponseException) {
              descriptionStatus(Color.RED, R.string.error_ip_port, R.string.error_illegal_function, true)
              Log.d(TAG, e.printStackTrace().toString())
              initBtn = true
              continue
            } catch (e: ModbusTransportException) {
              descriptionStatus(Color.RED, R.string.no_connection, R.string.error_connection, true)
              Log.d(TAG, e.printStackTrace().toString())
              initBtn = true
              continue
            }

            if (initBtn) {
              description.setTextColor(R.color.gray_dark)
              setValve(connection.setTime5, AppPreference.getTime5()!!.toInt())
              setValve(connection.setTime1, AppPreference.getTime1()!!.toInt())
              withContext(Dispatchers.Main) {
                if (MbValue.getValve1) {
                  btnValve1.isEnabled = true
                  btnValve2.isEnabled = false
                  btnValve3.isEnabled = false
                }
                if (MbValue.getValve2) {
                  btnValve1.isEnabled = false
                  btnValve2.isEnabled = true
                  btnValve3.isEnabled = false
                }
                if (MbValue.getValve3) {
                  btnValve1.isEnabled = false
                  btnValve2.isEnabled = false
                  btnValve3.isEnabled = true
                }
              }
              initBtn = false
            }
          }
          else -> {
            app.setInitStart(true)
          }
        }
      }
    }

    btnValve1.setOnClickListener {
      GlobalScope.launch(Dispatchers.IO) {
        try {
          setValve(connection.valve1, !MbValue.getValve1)
        } catch (e: ModbusTransportException) {
          Log.d(TAG, e.printStackTrace().toString())
        }
      }
      btnValve1.isEnabled = false
      btnValve2.isEnabled = false
      btnValve3.isEnabled = false

      Handler(Looper.getMainLooper()).postDelayed({
        btnValve1.isEnabled = true
        if (!MbValue.getValve1) {
          btnValve2.isEnabled = true
          btnValve3.isEnabled = true
        }
      }, 2000)
    }

    btnValve2.setOnClickListener {
      GlobalScope.launch(Dispatchers.IO) {
        try {
          if (MbValue.getModeAuto) {
            setValve(connection.start, !MbValue.getValve2)
          } else {
            setValve(connection.valve2, !MbValue.getValve2)
          }
        } catch (e: ErrorResponseException) {
          Log.d(TAG, e.printStackTrace().toString())
        } catch (e: ModbusTransportException) {
          Log.d(TAG, e.printStackTrace().toString())
        }
      }
      btnValve1.isEnabled = false
      btnValve2.isEnabled = false
      btnValve3.isEnabled = false

      Handler(Looper.getMainLooper()).postDelayed({
        btnValve2.isEnabled = true
        if (!MbValue.getValve2) {
          btnValve1.isEnabled = true
          btnValve3.isEnabled = true
        }
      }, 2000)
    }

    btnValve3.setOnClickListener {
      GlobalScope.launch(Dispatchers.IO) {
        try {
          setValve(connection.valve3, !MbValue.getValve3)
        } catch (e: ModbusTransportException) {
          Log.d(TAG, e.printStackTrace().toString())
        }
      }
      btnValve1.isEnabled = false
      btnValve2.isEnabled = false
      btnValve3.isEnabled = false

      Handler(Looper.getMainLooper()).postDelayed({
        btnValve3.isEnabled = true
        if (!MbValue.getValve3) {
          btnValve2.isEnabled = true
          btnValve1.isEnabled = true
        }
      }, 2000)
    }
  }


  private fun modeIndicator() {
    //test
    if (MbValue.getValve1) {
      connection.getMaster().setValue(connection.open, MbValue.getValve1)
      connection.getMaster().setValue(connection.close, !MbValue.getValve1)
    } else {
      connection.getMaster().setValue(connection.open, MbValue.getValve1)
      connection.getMaster().setValue(connection.close, !MbValue.getValve1)
    }

    //valve1
    if (MbValue.getOpen and !MbValue.getValve2 and !MbValue.getValve3) {
      ivValve.setImageResource(R.drawable.valve2_open)
      description.setText(R.string.valveOpen)
    } else if (MbValue.getClose and !MbValue.getValve2 and !MbValue.getValve3) {
      ivValve.setImageResource(R.drawable.valve1_close)
      description.setText(R.string.valveClose)
    }

    //valve2 valve 3
    when {
      MbValue.getValve2 and !MbValue.getValve1 and !MbValue.getValve3 -> {
        ivValve.setImageResource(R.drawable.valve3_top_seat_flush)
        description.setText(R.string.valveTopSeatFlush)
      }
      MbValue.getValve3 and !MbValue.getValve1 and !MbValue.getValve2 -> {
        ivValve.setImageResource(R.drawable.valve4_lower_seat_flush)
        description.setText(R.string.valveLowerSeatFlush)
      }
      !MbValue.getValve3 and !MbValue.getValve1 and !MbValue.getValve2 -> {
        ivValve.setImageResource(R.drawable.valve1_close)
        description.setText(R.string.valveClose)
      }
    }
  }

  private suspend fun setValve(
    value1: BaseLocator<Boolean>,
    value2: Boolean,
  ) {

    withContext(Dispatchers.IO) {
      try {
        connection.getMaster().setValue(value1, value2)
      } catch (e: ErrorResponseException) {
        Log.d(TAG, e.printStackTrace().toString())
      }
    }
  }

  private suspend fun setValve(
    valve1: BaseLocator<Number>,
    valve2: Number,
  ) {

    withContext(Dispatchers.IO) {
      try {
        connection.getMaster().setValue(valve1, valve2)
      } catch (e: ErrorResponseException) {
        Log.d(TAG, e.printStackTrace().toString())
      }
    }
  }

  private suspend fun descriptionStatus(
    @ColorRes color: Int,
    @StringRes text: Int,
    @StringRes error_connection: Int = R.string.error_connection,
    title: Boolean = false
  ) {
    withContext(Dispatchers.Main) {
      if (title)
        APP_ACTIVITY.setTitle(error_connection)
      description.setTextColor(color)
      description.setText(text)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.setting, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.btn_settings -> {
        AppPreference.setTime5(MbValue.getSetTime5.toString())
        AppPreference.setTime1(MbValue.getSetTime1.toString())
        APP_ACTIVITY.navController.navigate(R.id.action_startFragment_to_settingsFragment)
      }
    }
    return super.onOptionsItemSelected(item)
  }

  private fun initialization() {
    btnValve1.visibility = View.GONE
    btnValve2.visibility = View.INVISIBLE
    btnValve3.visibility = View.GONE
    connection = ConnectionPLC
  }

  override fun onStop() {
    super.onStop()
    job.cancel()
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}