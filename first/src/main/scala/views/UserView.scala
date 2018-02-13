package views

import com.aroonpa.{Media, SearchResult}
import com.github.mustachejava.util.InternalArrayList
import com.twitter.finatra.response.Mustache

@Mustache("user")
case class
UserView(firstName: String, lastName: String)

@Mustache("dubbing")
case class DubbingView(override val userid: String) extends Header

@Mustache("playing")
case class PlayingView(link: String, videoId: String)

@Mustache("complete")
case class CompletedView()

@Mustache("login")
case class LoginView(error: String = "")

@Mustache("signup")
case class SignUp(error: String = "")

@Mustache("search")
case class SearchView(results: java.util.List[Media] = new InternalArrayList(), error: String = "")

trait Header {
  val userid: String
}
