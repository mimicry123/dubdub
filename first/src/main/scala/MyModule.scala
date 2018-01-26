package com.aroonpa

import com.aroonpa.thrift.UserService
import com.google.inject.Provides
import com.twitter.finagle.ThriftMux
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.thrift.ClientId
import com.twitter.inject.TwitterModule
import com.twitter.util.TimeConversions._
import org.apache.thrift.protocol.TBinaryProtocol.Factory

object MyModule1 extends TwitterModule {
  val dest = flag(name = "client.dest", default = "defaultDestIfNoneProvided", help = "The client dest to use.")
  val label = flag(name = "client.label", default = "defaultLabelIfNoneProvided", help = "The client label to use.")
  flag(name = "cwd", default = "/users/arunviswanathan", help = "Home directory")
  flag(name = "audioDir", default = "audio", help = "Directory to store audio")
  flag(name = "videoDir", default = "mediadrop/mediadrop/data/media", help = "Directory to store videos")
  flag(name = "dubbingDir", default = "dubbed", help = "Directory to store dubbed videos")
  flag(name = "elasticHost", default = "localhost", help = "Host for elastic search")
  flag(name = "elasticPort", default = "9200", help = "Port for elastic search")


@Provides
def provideUserClient(): UserService.FinagledClient = {
  val builder = ClientBuilder().stack(ThriftMux.client.withClientId(ClientId("first"))).hosts("localhost:9090").timeout(5.seconds)
  new UserService.FinagledClient(builder.build(), new Factory())
}


}