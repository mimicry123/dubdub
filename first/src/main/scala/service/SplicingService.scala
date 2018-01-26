package com.aroonpa.service

import java.io.File

import com.google.inject.Inject
import com.twitter.inject.annotations.Flag
import com.twitter.util.{Future, FuturePools}

import scala.sys.process.Process

class SplicingService @Inject()(@Flag("cwd") cwd: String,
                                @Flag("audioDir") audioDir: String,
                                @Flag("videoDir") videoDir: String,
                                @Flag("dubbingDir") dubbedDir: String) {

  def apply(audioFileName: String, videoFileName: String): Future[String] = {
    FuturePools.unboundedPool().apply({
      val result = Process(s"ffmpeg -i $videoDir/$videoFileName.mp4 -i $audioDir/$audioFileName -c:v copy -map 0:v:0 -map 1:a:0 $dubbedDir/$audioFileName.$videoFileName.mp4", new File(s"$cwd")) !
      val res = s"$audioFileName.$videoFileName.mp4"
      print(res)
      res
    })
  }

}
