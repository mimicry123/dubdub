import java.io.ByteArrayInputStream

import com.aroonpa.thrift.OptionalUser
import com.twitter.io.Buf
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.{TIOStreamTransport, TMemoryBuffer}

object Transport {
  val protocolFactory = new TBinaryProtocol.Factory

  def toBuf(t: OptionalUser): Buf = {
    print(t)
    val transport = new TMemoryBuffer(1024)
    t.write(new TBinaryProtocol(transport))
    val binary: Array[Byte] = transport.getArray
    Buf.ByteArray.Owned(binary)

  }

  def toOptionalUser(buf: Buf, pwd: Option[String] = None): OptionalUser = {
    val bytes = Buf.ByteArray.Owned.extract(buf)
    val buf1 = new ByteArrayInputStream(bytes)
    val factory = new TBinaryProtocol.Factory
    val proto = factory.getProtocol(new TIOStreamTransport(buf1))
    val optionalUser = OptionalUser.decode(proto)
    if (pwd.isEmpty || optionalUser.user.exists(u => u.password == pwd.get)) optionalUser else OptionalUser(None)
  }


}
