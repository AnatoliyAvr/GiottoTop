package by.tolikavr.plc4j

class Temp {
/*  lateinit var tvText: TextView
  lateinit var edText: EditText
  lateinit var btn: Button

  tvText = findViewById(R.id.tv_text)
  edText = findViewById(R.id.ed_text)
  btn = findViewById(R.id.btn)
  btn.isEnabled = false

  val ipParameters = IpParameters().apply {
    host = "192.168.122.85"
    port = 502
  }

  val modbusFactory = ModbusFactory()
  val master = modbusFactory
    .createTcpMaster(ipParameters, true) as TcpMaster

  val loc = BaseLocator.holdingRegister(1, 0, Da taType.TWO_BYTE_INT_UNSIGNED)
  val loc1 = BaseLocator.holdingRegister(1, 1, DataType.TWO_BYTE_INT_UNSIGNED)

  var i = 0
  edText.setText("0")

  btn.setOnClickListener {
    if (edText.text.isNotEmpty()) {
      i = Integer.parseInt(edText.text.toString())
    }
    thread {
      master.setValue(loc1, i)
    }
  }

  thread {
    var init = true
    while (true) {
      Thread.sleep(500)

      if (init) {
        try {
          master.init()
        } catch (e: ModbusInitException) {
          runOnUiThread {
            tvText.text = e.printStackTrace().toString()
          }
        }
        init = false
      } else if (master.testSlaveNode(1)) {
        Log.d("AAA", master.testSlaveNode(1).toString())
        Log.d("AAA", master.getValue(loc).toString())

        val locVal = master.getValue(loc1).toString()
        runOnUiThread {
          btn.isEnabled = true
          tvText.text = locVal
        }
      } else {
        runOnUiThread {
          btn.isEnabled = false
        }
        init = true
      }
    }
  }*/
}