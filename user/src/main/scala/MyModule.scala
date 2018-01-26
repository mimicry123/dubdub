import com.twitter.inject.TwitterModule

object MyModule extends TwitterModule{

  flag(name = "redisHostPort", default = "localhost:6379", help = "Redis host port")

}
