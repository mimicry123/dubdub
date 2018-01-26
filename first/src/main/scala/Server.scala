package com.aroonpa

import java.sql.Date

import com.aroonpa.thrift._
import com.google.inject.Inject
import com.twitter.finagle.http.Request
import com.twitter.finagle.{Http, Service, http}
import com.twitter.finatra.http.filters.{CommonFilters, ExceptionMappingFilter}
import com.twitter.finatra.http.request.RequestUtils
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.finatra.http.{Controller, HttpServer}
import com.twitter.finatra.request.FormParam
import com.twitter.inject.Logging
import com.twitter.util.{Await, Future}
import com.aroonpa.service.{DownloaderService, SearchService, SplicingService, StorageService}
import views._


object EntryServer extends HttpServer {
  override val modules = Seq(MyModule1)
  statsReceiver.counter("requests_counter").incr()

  override protected def configureHttp(router: HttpRouter): Unit = {
    router
      .filter[CommonFilters]
      .filter[ExceptionMappingFilter[Request]]
      .add[HelloController]
  }
}

case class FormPostRequest(@FormParam video: String)

case class LoginForm(@FormParam email: String,
                     @FormParam password: String)

case class SearchForm(@FormParam search: String)

case class SignUpForm(@FormParam email: String,
                      @FormParam password: String,
                      @FormParam userid: String)

case class Media(id: Int, type1: String, slug: String, reviewed: Int,
                 encoded: Int, publishable: Int, created_on: Date,
                 modified_on: Date, title: String, duration: Int, views: Int,
                 likes: Int, dislikes: Int, popularity_points: Int,
                 popularity_likes: Int, popularity_dislikes: Int, author_name: String,
                 author_email: String, subtitle: String, publish_on: Date)

case class MediaFiles(id: Int, media_id: Int, storage_id: Int, type1: String,
                      container: String, display_name: String, unique_id: String,
                      size: Int, created_on: Date, modified_on: Date)

class HelloController @Inject()(downloader: DownloaderService,
                                storageService: StorageService,
                                splicingService: SplicingService,
                                searchService: SearchService,
                                userService: UserService.FinagledClient) extends Controller with Logging {

  get("/hello/:id") { request: Request => {
    "success" + request.params("id")
  }
  }
  get("/songList") { request: Request => {
    //return songlist

  }
  }
  get("/user/:first/:last") { request: Request =>
    val firstName = request.params("first")
    val lastName = request.params("last")
    UserView(firstName, lastName)
  }

  get("/upload") { request: Request =>
    DubbingView("dummy")
  }

  get("/:*") { request: Request =>
    response.ok.fileOrIndex(
      request.params("*"),
      "index.html")
  }
  post("/:*") { request: Request =>
    response.ok.fileOrIndex(
      request.params("*"),
      "index.html")
  }

  def authAndPrint(user: User): Future[OptionalUser] = {
    print("before" + user)
    val fuser = userService.login(Login("arunneel", "cisco"))
    fuser.map(e => {
      print("After user  " + e)
      e
    })
  }

  post("/upload") { request: FormPostRequest =>
    val videoId = randomAlpha(5)
    print(videoId)
    val futureResult: Future[String] = downloader(request.video, videoId)

    val client: Service[http.Request, http.Response] = Http.newService("localhost:9200")
    val request1 = http.Request(http.Method.Get, "/_nodes/stats/jvm")
    request1.host = "localhost:9200"
    val response: Future[http.Response] = client(request1)
    Await.result(response.onSuccess { rep: http.Response =>
      println("GET success: " + rep.getContentString())
    })

    PlayingView(request.video, videoId)
  }


  post("/wave/:ext/:videoId") { request: Request =>
    val f = RequestUtils.multiParams(request).values.foreach(assert => {
      print(assert.filename.get)
      storageService(assert.filename.get, "audio",
        assert.data,
        if (request.params("ext") != "wav") Some("wav") else None)
        .map(a => splicingService(a, request.params("videoId"))).map(a => CompletedView())
    })

  }

  get("/login") { request: Request =>
    LoginView()

  }

  get("/signup") { request: Request =>
    SignUp()
  }

  get("/search") { request: Request =>
    SearchView()
  }

  post("/search") { request: SearchForm =>
    searchService.searchKey(request.search).map(a => SearchView(a.mkString(",")))
  }

  post("/signup") { request: SignUpForm =>
    userService.addUser(User(request.userid, request.password, "", request.email))
      .map(f => SearchView())
      .handle({
        case e: UserIdAlreadyCreated => SignUp("User id already created-- try again")
        case e: UserEmailAlreadyCreated => SignUp("User email created-- try again")
      })


  }

  post("/login") { request: LoginForm =>
    userService.login(Login(request.email, request.password))
      .map(f => SearchView())
      .handle({
        case e: Throwable => {
          print("Exception occurred: " + e)
          LoginView("Authentication failed -- try again")
        }
      })


  }

  def randomAlpha(length: Int): String = {
    val chars = ('a' to 'z') ++ ('A' to 'Z')
    randomStringFromCharList(length, chars)
  }

  def randomStringFromCharList(length: Int, chars: Seq[Char]): String = {
    val sb = new StringBuilder
    for (i <- 1 to length) {
      val randomNum = util.Random.nextInt(chars.length)
      sb.append(chars(randomNum))
    }
    sb.toString
  }


}
