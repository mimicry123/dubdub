import com.aroonpa.thrift.{User, UserService}
import com.google.inject.Inject
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.filters.{CommonFilters, ExceptionMappingFilter}
import com.twitter.finatra.http.request.RequestUtils
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.finatra.http.{Controller, HttpServer}
import com.twitter.finatra.request.FormParam
import com.twitter.inject.Logging
import com.twitter.util.Future
import service.{DownloaderService, SplicingService, StorageService}
import views._



object QuickLookServer extends HttpServer {
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

class HelloController @Inject()(downloader: DownloaderService,
                                storageService: StorageService,
                                splicingService: SplicingService,
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

  post("/upload") { request: FormPostRequest =>
    val videoId = randomAlpha(5)
    val futureResult: Future[String] = downloader(request.video, videoId)
    userService.addUser(User("aa","aa")).map(print(_))
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
