package service

import com.twitter.finagle.Mysql
import com.twitter.finagle.mysql.{Client, Transactions}


object ProductionSql {

    val client: Client with Transactions = Mysql.client
      .withCredentials("mediadrop_user", "yoursecretpassword")
      .withDatabase("mediadrop")
      .newRichClient("localhost:3306")

    val insertOne = client.prepare("INSERT INTO media(id, type, slug, reviewed, encoded, publishable, created_on, modified_on, title, duration, views, likes, dislikes, popularity_points, popularity_likes, popularity_dislikes, author_name, author_email, subtitle, publish_on) VALUES(?, ?, ?, ?,?,?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?)")

    val insertTwo = client.prepare("INSERT INTO media_files (id,media_id,storage_id,type,container,display_name,unique_id,size,created_on,modified_on)VALUES(?,?,?,?,?,?,?,?,?,?);")


}