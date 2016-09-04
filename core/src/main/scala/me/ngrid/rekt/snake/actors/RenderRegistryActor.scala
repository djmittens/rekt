package me.ngrid.rekt.snake.actors

import akka.typed.ScalaDSL._
import akka.typed._
import com.badlogic.gdx.graphics.g2d.SpriteBatch

import scala.concurrent.Future
import scala.util.{Failure, Success}

trait Drawable2D {
  def draw(sb: SpriteBatch): Future[Unit]
}

object RenderCommand {
  case class Render(replyTo: ActorRef[RenderCommand]) extends RenderCommand
  case class Rendered(drawable: Drawable2D) extends RenderCommand
}
trait RenderCommand

