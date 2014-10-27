package nl.flotsam

import java.io.{BufferedInputStream, File, FileInputStream}
import javax.xml.bind.DatatypeConverter._

package object iterm2 {

  def imagecat(str: String): Unit = imagecat(new File(str))

  def imagecat(file: File): Unit = imagecat({
    val length = file.length().toInt
    val buffer: Array[Byte] = Array.ofDim[Byte](length)
    val stream = new BufferedInputStream(new FileInputStream(file))
    try {
      stream.read(buffer, 0, length)
      buffer
    } finally {
      stream.close()
    }
  }, Some(file.getName))

  def imagecat(buffer: Array[Byte], name: Option[String] = None): Unit = {
    val meta = List(
      name.map("name" -> _),
      Some("size" -> buffer.length),
      Some("inline" -> 1)
    ).flatten
    val args = (for {
      (key, value) <- meta
    } yield s"$key=$value").mkString(";")
    println(s"\033]1337;File=$args:${printBase64Binary(buffer)}\07")
  }

}
