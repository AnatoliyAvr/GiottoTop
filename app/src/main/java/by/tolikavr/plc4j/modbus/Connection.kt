package by.tolikavr.plc4j.modbus

import com.serotonin.modbus4j.ModbusFactory
import com.serotonin.modbus4j.code.DataType
import com.serotonin.modbus4j.ip.IpParameters
import com.serotonin.modbus4j.ip.tcp.TcpMaster
import com.serotonin.modbus4j.locator.BaseLocator

object Connection {

  private val ipParameters: IpParameters = IpParameters().apply {
    host = "192.168.122.85"
    port = 502
  }
  private val modbusFactory: ModbusFactory = ModbusFactory()
  var master = modbusFactory.createTcpMaster(ipParameters, true) as TcpMaster

  val loc = BaseLocator.holdingRegister(1, 0, DataType.TWO_BYTE_INT_UNSIGNED)
  val loc1 = BaseLocator.holdingRegister(1, 1, DataType.TWO_BYTE_INT_UNSIGNED)
}