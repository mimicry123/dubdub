import java.util.UUID

import com.aroonpa.thrift._
import com.google.inject.Inject
import com.twitter.finagle.Redis
import com.twitter.finagle.redis.util.StringToBuf
import com.twitter.inject.annotations.Flag
import com.twitter.io.Buf
import com.twitter.util.Future

class RedisClient @Inject()(@Flag("redisHostPort") redisHostPort: String) {
  val redisClient = Redis.newRichClient("localhost:6379")

  def setUser(inputUser: User): Future[User] = {

    val uuid = UUID.randomUUID()
    val token = BearerTokenGenerator.generateToken.toString
    val user = inputUser.copy(token = token)
    val validation = redisClient.get(StringToBuf(inputUser.userId)).map((f: Option[Buf]) => if (!f.isEmpty) throw new UserIdAlreadyCreated("user id is already created"))
      .flatMap(f => redisClient.get(StringToBuf(inputUser.email)).map((f: Option[Buf]) => if (!f.isEmpty) throw new UserEmailAlreadyCreated("user email is already created")))
    validation.flatMap(f => addUser(uuid, user, token))
  }

  private def addUser(uuid: UUID, user: User, token: String) = {
    redisClient.set(StringToBuf(uuid.toString), Transport.toBuf(OptionalUser(Some(user))))
      .onSuccess(f => redisClient.set(StringToBuf(user.email), StringToBuf(uuid.toString))
        .onSuccess(f => redisClient.set(StringToBuf(user.userId), StringToBuf(uuid.toString)))
        .onSuccess(f => redisClient.set(StringToBuf(token.toString), Transport.toBuf(OptionalUser(Some(user))))))
      .onSuccess(f => redisClient.get(StringToBuf(uuid.toString)).map(e => print(s"got it $e")))
      .map(f => user)
  }

  def getUser(token: String): Future[OptionalUser] = {
    val futureOptionalBuf: Future[Option[Buf]] = redisClient.get(StringToBuf(token))
    futureOptionalBuf.map(_.map(Transport.toOptionalUser(_)).getOrElse(OptionalUser(None)))
  }


  def login(login: Login): Future[OptionalUser] = {
    val futureOptionalBuf: Future[Option[Buf]] = redisClient.get(StringToBuf(login.userId)).flatMap(getUserWithUUID(_))
    futureOptionalBuf.map(_.map(Transport.toOptionalUser(_, Some(login.password))).getOrElse(OptionalUser(None))).map(a => if (a.user.isEmpty) throw new AuthFailed("auth failed") else a)
  }

  def getUserWithUUID(a: Option[Buf]): Future[Option[Buf]] = {
    a.map(redisClient.get(_)).getOrElse(Future.value(None))
  }
}

import java.security.SecureRandom

/*
 * Generates a Bearer Token with the length of 32 characters according to the
 * specification RFC6750 (http://http://tools.ietf.org/html/rfc6750)
 */
object BearerTokenGenerator {

  val TOKEN_LENGTH = 32
  val TOKEN_CHARS =
    "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-"
  val secureRandom = new SecureRandom()

  def generateToken: String =
    generateToken(TOKEN_LENGTH)

  def generateToken(tokenLength: Int): String =
    if (tokenLength == 0) "" else TOKEN_CHARS(secureRandom.nextInt(TOKEN_CHARS.length())) +
      generateToken(tokenLength - 1)

}