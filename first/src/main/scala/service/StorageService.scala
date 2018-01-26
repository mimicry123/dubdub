package com.aroonpa.service

import java.io.{File, FileOutputStream}

import com.google.inject.Inject
import com.twitter.inject.annotations.Flag
import com.twitter.util.{Future, FuturePools}

import scala.sys.process._

class StorageService @Inject()(@Flag("cwd") cwd: String) {

  def apply(fileName: String, dir: String,
            data: scala.Array[scala.Byte],
            convertExt: Option[String] = None): Future[String] = {
    FuturePools.unboundedPool().apply({
      val file = new FileOutputStream(s"$cwd/$dir/${fileName}")
      file.write(data)
      file.close()
      val res = convertExt.map(e => {
        val result = Process(s"ffmpeg -i $fileName -vn -ab 128k -ar 44100 -y $fileName.$e", new File(s"$cwd/$dir")) !;
        print(result)
        s"$fileName.$e"
      }).getOrElse("")
      print(res)
      res
    })
  }
}
