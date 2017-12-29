package views

import com.twitter.finatra.response.Mustache

@Mustache("user")
case class UserView(firstName: String, lastName: String)

@Mustache("dubbing")
case class DubbingView(lastName:String)

@Mustache("playing")
case class PlayingView(link:String, videoId: String)

@Mustache("complete")
case class CompletedView()

@Mustache("login")
case class LoginView()