package com.aroonpa.service

import java.io.File

import com.google.inject.Inject
import com.twitter.inject.annotations.Flag
import com.twitter.util.{Future, FuturePools}

import scala.sys.process._

class DownloaderService @Inject()(@Flag("cwd") cwd: String, @Flag("videoDir") videoDir: String) {

  def apply(link: String, uid: String): Future[String] = {
    FuturePools.unboundedPool().apply({
      val result = Process(s"youtube-dl -o $uid.%(ext)s $link -f 18", new File(s"$cwd/$videoDir")) #| "grep Destination" !!;
      val strippedResult = (result.replace("[download] Destination:", "").replace("\n", "")).trim
      print(strippedResult)
      strippedResult
    }
    )

  }

}
