package by.tolikavr.plc4j.modbus

import com.serotonin.modbus4j.ModbusFactory
import com.serotonin.modbus4j.code.DataType
import com.serotonin.modbus4j.ip.IpParameters
import com.serotonin.modbus4j.ip.tcp.TcpMaster
import com.serotonin.modbus4j.locator.BaseLocator

object Connection {

  private lateinit var master: TcpMaster

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

  val modeAuto = BaseLocator.coilStatus(1, 6)!!
  val modeOff = BaseLocator.coilStatus(1, 7)!!
  val modeManual = BaseLocator.coilStatus(1, 8)!!

  val setTime5 = BaseLocator.holdingRegister(1, 0, DataType.TWO_BYTE_INT_UNSIGNED)!!
  val setTime1 = BaseLocator.holdingRegister(1, 2, DataType.TWO_BYTE_INT_UNSIGNED)!!
}