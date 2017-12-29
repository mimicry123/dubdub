import com.twitter.finatra.thrift.ThriftServer
import com.twitter.finatra.thrift.filters.{LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.thrift.routing.ThriftRouter

object UserServerMain extends UserServer

class UserServer extends ThriftServer {

  override val defaultFinatraThriftPort: String = ":9090"

  override val  defaultAdminPort: Int = 8080

  override def configureThrift(router: ThriftRouter): Unit = {
    router
      .filter[LoggingMDCFilter]
      .filter[TraceIdMDCFilter]
      .add[UserThriftController]
  }
}