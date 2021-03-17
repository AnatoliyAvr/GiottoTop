package by.tolikavr.plc4j.ui.start

import android.annotation.SuppressLint
import android.graphics.Color
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
import by.tolikavr.plc4j.utilits.AppPreference
import com.serotonin.modbus4j.exception.ModbusInitException
import kotlinx.coroutines.*


const val TIME5 = "time5"
const val TIME1 = "time1"

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

  @SuppressLint("ResourceAsColor")
  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this).get(StartViewModel::class.java)
    setHasOptionsMenu(true)
    initialization()

    Log.d("AAA", "${AppPreference.getIp()} - ${AppPreference.getPort()}")
    Log.d("AAA", "${arguments?.getString(TIME5)} - ${arguments?.getString(TIME1)}")

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

              description.setTextColor(R.color.gray_dark)
              description.setText(R.string.valveClose)
            } catch (e: ModbusInitException) {
              withContext(Dispatchers.Main) {
                APP_ACTIVITY.setTitle(R.string.error_connection)
              }
              Log.d("AAA", e.printStackTrace().toString())
            }
            init = false
          }
          connection.getMaster().testSlaveNode(1) -> {

            val mode = when {
              connection.getMaster().getValue(connection.modeAuto) -> "Auto"
              connection.getMaster().getValue(connection.modeOff) -> "Off"
              connection.getMaster().getValue(connection.modeManual) -> "Manual"
              else -> ""
            }

            withContext(Dispatchers.Main) {
              when (mode) {
                "Auto" -> {
                  APP_ACTIVITY.setTitle(R.string.modeAuto)
                  btnValve1.visibility = View.GONE
                  btnValve2.visibility = View.VISIBLE
                  btnValve2.setText(R.string.start)
                  btnValve3.visibility = View.GONE
                }
                "Off" -> {
                  APP_ACTIVITY.setTitle(R.string.modeOff)
                  btnValve1.visibility = View.GONE
                  btnValve2.visibility = View.INVISIBLE
                  btnValve3.visibility = View.GONE
                }
                "Manual" -> {
                  APP_ACTIVITY.setTitle(R.string.modeManual)
                  btnValve1.visibility = View.VISIBLE
                  btnValve2.visibility = View.VISIBLE
                  btnValve2.setText(R.string.valve2)
                  btnValve3.visibility = View.VISIBLE
                }
              }
            }
            //        Log.d("AAA", connection.getMaster().getValue(connection.start).toString())
            //        Log.d("AAA", connection.getMaster().getValue(connection.valve1).toString())
            //        Log.d("AAA", connection.getMaster().getValue(connection.valve2).toString())
            //        Log.d("AAA", connection.getMaster().getValue(connection.valve3).toString())
            //
            //        Log.d("AAA", connection.getMaster().getValue(connection.open).toString())
            //        Log.d("AAA", connection.getMaster().getValue(connection.close).toString())
            //
            Log.d("AAA", connection.getMaster().getValue(connection.modeAuto).toString())
            Log.d("AAA", connection.getMaster().getValue(connection.modeOff).toString())
            Log.d("AAA", connection.getMaster().getValue(connection.modeManual).toString())
            //
            Log.d("AAA", connection.getMaster().getValue(connection.setTime5).toString())
            Log.d("AAA", connection.getMaster().getValue(connection.setTime1).toString())


//            setTime5 = connection.getMaster().getValue(connection.setTime5).toInt()
//            setTime1 = connection.getMaster().getValue(connection.setTime1).toInt()

            setTime5 = 5
            setTime1 = 1
          }
          else -> {
            withContext(Dispatchers.IO) {
              description.setTextColor(Color.RED)
              description.setText(R.string.no_connection)
            }
            init = true
          }
        }

      }
    }

    btnValve1.setOnClickListener {
      ivValve.setImageResource(R.drawable.valve2_open)
      description.setText(R.string.valveOpen)
      ivAir1.isVisible = false
      ivAir2.isVisible = true
      ivAir3.isVisible = false
    }

    btnValve2.setOnClickListener {
      ivValve.setImageResource(R.drawable.valve3_top_seat_flush)
      description.setText(R.string.valveTopSeatFlush)
      ivAir1.isVisible = false
      ivAir2.isVisible = false
      ivAir3.isVisible = true
    }

    btnValve3.setOnClickListener {
      ivValve.setImageResource(R.drawable.valve4_lower_seat_flush)
      description.setText(R.string.valveLowerSeatFlush)
      ivAir1.isVisible = true
      ivAir2.isVisible = false
      ivAir3.isVisible = false
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