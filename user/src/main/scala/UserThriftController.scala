
import javax.inject.Singleton

import com.aroonpa.thrift.UserService.{AddUser, AuthToUser, Login}
import com.aroonpa.thrift.{OptionalUser, User, UserService}
import com.google.inject.Inject
import com.twitter.finagle.Service
import com.twitter.finatra.thrift.Controller

@Singleton
class UserThriftController @Inject()(redisClient: RedisClient) extends Controller with UserService.ServicePerEndpoint {

  override val addUser: Service[AddUser.Args, User] = handle(AddUser) { args: AddUser.Args =>
    redisClient.setUser(args.user)
  }

  override def authToUser: Service[AuthToUser.Args, OptionalUser] = handle(AuthToUser) { args: AuthToUser.Args =>
    redisClient.getUser(args.token)
  }

  override def login: Service[Login.Args, OptionalUser] = handle(Login) { args: Login.Args =>
    redisClient.login(args.login)

  }
}


