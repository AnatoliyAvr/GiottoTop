package by.tolikavr.plc4j.modbus

import by.tolikavr.plc4j.model.MbValue
import com.serotonin.modbus4j.ModbusFactory
import com.serotonin.modbus4j.code.DataType
import com.serotonin.modbus4j.ip.IpParameters
import com.serotonin.modbus4j.ip.tcp.TcpMaster
import com.serotonin.modbus4j.locator.BaseLocator

object Connection {

  private lateinit var master: TcpMaster

  //  fun initialization(setHost: String? = "192.168.1.10", setPort: Int = 502) {
  fun initialization(setHost: String? = "192.168.122.85", setPort: Int = 502) {
    val ipParameters: IpParameters = IpParameters().apply {
      host = setHost
      port = setPort
    }
    val modbusFactory = ModbusFactory()
    master = modbusFactory.createTcpMaster(ipParameters, true) as TcpMaster
    master.init()
  }

  fun getMaster() = master

  val start = BaseLocator.coilStatus(1, 0)!!
  val valve1 = BaseLocator.coilStatus(1, 1)!!
  val valve2 = BaseLocator.coilStatus(1, 2)!!
  val valve3 = BaseLocator.coilStatus(1, 3)!!

  val open = BaseLocator.coilStatus(1, 4)!!
  val close = BaseLocator.coilStatus(1, 5)!!

  val modeAuto = BaseLocator.coilStatus(1, 8)!!
  val modeOff = BaseLocator.coilStatus(1, 9)!!
  val modeManual = BaseLocator.coilStatus(1, 10)!!

  val setTime5 = BaseLocator.holdingRegister(1, 1, DataType.TWO_BYTE_INT_UNSIGNED)!!
  val setTime1 = BaseLocator.holdingRegister(1, 2, DataType.TWO_BYTE_INT_UNSIGNED)!!

  fun initMB() {
    MbValue.getStart = master.getValue(start)
    MbValue.getValve1 = master.getValue(valve1)
    MbValue.getValve2 = master.getValue(valve2)
    MbValue.getValve3 = master.getValue(valve3)
    MbValue.getOpen = master.getValue(open)
    MbValue.getClose = master.getValue(close)
    MbValue.getModeAuto = master.getValue(modeAuto)
    MbValue.getModeOff = master.getValue(modeOff)
    MbValue.getModeManual = master.getValue(modeManual)
    MbValue.getSetTime5 = master.getValue(setTime5)
    MbValue.getSetTime1 = master.getValue(setTime1)
  }
}