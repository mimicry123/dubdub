
import com.twitter.finatra.thrift.Controller
import com.twitter.util.Future
import javax.inject.Singleton

import com.aroonpa.thrift.{User, UserService}
import com.aroonpa.thrift.UserService.AddUser
import com.twitter.finagle.Service

@Singleton
class UserThriftController extends Controller with UserService.ServicePerEndpoint {

  override val addUser: Service[AddUser.Args, User] = handle(AddUser) { args: AddUser.Args =>
    print("Hitting the user endpoint")
    Future.value(User("summa",""))
  }
}