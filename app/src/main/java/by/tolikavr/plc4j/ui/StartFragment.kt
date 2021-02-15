package by.tolikavr.plc4j.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.tolikavr.plc4j.R
import by.tolikavr.plc4j.databinding.StartFragmentBinding
import by.tolikavr.plc4j.utilits.APP_ACTIVITY


class StartFragment : Fragment() {

  private var _binding: StartFragmentBinding? = null
  private val mBinding get() = _binding!!
  private lateinit var viewModel: StartViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = StartFragmentBinding.inflate(layoutInflater, container, false)
    val view = mBinding.root
    return view
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this).get(StartViewModel::class.java)

    //mBinding.tvMode.setText(R.string.modeAuto)
  }


  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }

}