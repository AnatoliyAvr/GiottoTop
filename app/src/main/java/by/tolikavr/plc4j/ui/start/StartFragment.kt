package by.tolikavr.plc4j.ui.start

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
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
import by.tolikavr.plc4j.R
import by.tolikavr.plc4j.databinding.StartFragmentBinding
import by.tolikavr.plc4j.modbus.Connection
import by.tolikavr.plc4j.utilits.APP_ACTIVITY
import by.tolikavr.plc4j.utilits.AppPreference
import com.serotonin.modbus4j.exception.ErrorResponseException
import com.serotonin.modbus4j.exception.ModbusInitException
import com.serotonin.modbus4j.exception.ModbusTransportException
import com.serotonin.modbus4j.locator.BaseLocator
import kotlinx.coroutines.*


const val TIME5 = "time5"
const val TIME1 = "time1"

@SuppressLint("ResourceAsColor")
class StartFragment : Fragment() {

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
  private lateinit var connection: Connection
  private lateinit var job: Job
  private var setTime5 = 0
  private var setTime1 = 0
  private var start = false
  private var valve1 = false
  private var valve2 = false
  private var valve3 = false

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
      var init = true
      while (true) {
        delay(200)
        when {
          init -> {
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
              Log.d("AAA", e.printStackTrace().toString())
              continue
            }
            init = false
          }
          connection.getMaster().testSlaveNode(1) -> {
            try {
              when {
                connection.getMaster().getValue(connection.modeAuto) -> {
                  descriptionText(R.string.modeAuto)
                  btnValve1.visibility = View.GONE
                  btnValve2.visibility = View.VISIBLE
                  btnValve2.setText(R.string.start)
                  btnValve3.visibility = View.GONE
                }
                connection.getMaster().getValue(connection.modeOff) -> {
                  descriptionText(R.string.modeOff)
                  btnValve1.visibility = View.GONE
                  btnValve2.visibility = View.INVISIBLE
                  btnValve3.visibility = View.GONE
                }
                connection.getMaster().getValue(connection.modeManual) -> {

                  //Ручной режим
                  //valve1
                  if (valve1) {
                    connection.getMaster().setValue(connection.open, valve1)
                    connection.getMaster().setValue(connection.close, !valve1)
                  } else {
                    connection.getMaster().setValue(connection.open, valve1)
                    connection.getMaster().setValue(connection.close, !valve1)
                  }

                  if (connection.getMaster().getValue(connection.open) and !valve2 and !valve3) {
                    ivValve.setImageResource(R.drawable.valve2_open)
                    description.setText(R.string.valveOpen)
                  } else if (connection.getMaster().getValue(connection.close) and !valve2 and !valve3) {
                    ivValve.setImageResource(R.drawable.valve1_close)
                    description.setText(R.string.valveClose)
                  }

                  //valve2
                  if (connection.getMaster().getValue(connection.valve2) and !valve1 and !valve3) {
                    ivValve.setImageResource(R.drawable.valve3_top_seat_flush)
                    description.setText(R.string.valveTopSeatFlush)
                  } else if (!connection.getMaster().getValue(connection.valve2) and !valve1 and !valve3) {
                    ivValve.setImageResource(R.drawable.valve1_close)
                    description.setText(R.string.valveClose)
                  }

                  //valve3
                  if (connection.getMaster().getValue(connection.valve3) and !valve1 and !valve2) {
                    ivValve.setImageResource(R.drawable.valve3_top_seat_flush)
                    description.setText(R.string.valveLowerSeatFlush)
                  } else if (!connection.getMaster().getValue(connection.valve3) and !valve1 and !valve2) {
                    ivValve.setImageResource(R.drawable.valve1_close)
                    description.setText(R.string.valveClose)
                  }
                  descriptionText(R.string.modeManual)
                  btnValve1.visibility = View.VISIBLE
                  btnValve2.visibility = View.VISIBLE
                  btnValve2.setText(R.string.valve2)
                  btnValve3.visibility = View.VISIBLE

                }
                else -> {
                  descriptionText(R.string.modeNotSet)
                  btnValve1.visibility = View.GONE
                  btnValve2.visibility = View.INVISIBLE
                  description.text = ""
                  btnValve3.visibility = View.GONE
                }
              }


            } catch (e: ErrorResponseException) {
              descriptionStatus(Color.RED, R.string.error_ip_port, R.string.error_illegal_function, true)
              Log.d("AAA", e.printStackTrace().toString())
              continue
            } catch (e: ModbusTransportException) {
              descriptionStatus(Color.RED, R.string.no_connection, R.string.error_connection, true)
              Log.d("AAA", e.printStackTrace().toString())
              continue
            }

            //Log.d("AAA", modBus.start.toString())
            //Log.d("AAA", modBus.valve1.toString())
            //Log.d("AAA", modBus.valve2.toString())
            //Log.d("AAA", modBus.valve3.toString())
            //Log.d("AAA", modBus.open.toString())
            //Log.d("AAA", modBus.close.toString())

//            Log.d("AAA", modBus.modeAuto.toString())
//            Log.d("AAA", modBus.modeOff.toString())
//            Log.d("AAA", modBus.modeManual.toString())

            //Log.d("AAA", modBus.setTime5.toString())
            //Log.d("AAA", modBus.setTime1.toString())

            //setTime5 = modBus.setTime5.toInt()
            //setTime1 = modBus.setTime1.toInt()

            setTime5 = 5
            setTime1 = 1
          }
          else -> {
            init = true
          }
        }

      }
    }



    btnValve1.setOnClickListener {
      valve1 = !valve1
      GlobalScope.launch(Dispatchers.IO) {
        setValve(connection.valve1, valve1)
      }
      ivAir1.isVisible = false
      ivAir2.isVisible = valve1
      ivAir3.isVisible = false
      btnValve1.isEnabled = true
      btnValve2.isEnabled = !valve1
      btnValve3.isEnabled = !valve1
    }

    btnValve2.setOnClickListener {
      valve2 = !valve2
      GlobalScope.launch(Dispatchers.IO) {
        setValve(connection.valve2, valve2)
      }
      description.setText(R.string.valveTopSeatFlush)
      ivAir1.isVisible = false
      ivAir2.isVisible = false
      ivAir3.isVisible = valve2
      btnValve1.isEnabled = !valve2
      btnValve2.isEnabled = true
      btnValve3.isEnabled = !valve2
    }

    btnValve3.setOnClickListener {
      valve3 = !valve3
      GlobalScope.launch(Dispatchers.IO) {
        setValve(connection.valve3, valve3)
      }
      description.setText(R.string.valveLowerSeatFlush)
      ivAir1.isVisible = valve3
      ivAir2.isVisible = false
      ivAir3.isVisible = false
      btnValve1.isEnabled = !valve3
      btnValve2.isEnabled = !valve3
      btnValve3.isEnabled = true
    }


  }

  private suspend fun setValve(
    valve1: BaseLocator<Boolean>,
    valve2: Boolean
  ) {
    withContext(Dispatchers.IO) {
      connection.getMaster().setValue(valve1, valve2)
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

  private suspend fun descriptionText(@StringRes text: Int) {
    withContext(Dispatchers.Main) {
      APP_ACTIVITY.setTitle(text)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.setting, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.btn_settings -> {
        val bundle = Bundle()
        bundle.putInt(TIME5, setTime5)
        bundle.putInt(TIME1, setTime1)
        APP_ACTIVITY.navController.navigate(R.id.action_startFragment_to_settingsFragment, bundle)
      }
    }
    return super.onOptionsItemSelected(item)
  }

  private fun initialization() {
    connection = Connection
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