package me.ngrid.rekt.snake.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
  *
  */
object Snake {
  val SNAKE_MOVEMENT = 32

  def apply(x: Int, y: Int): Snake = Snake(x, y, SnakeDirection.Right, Nil)
}

case class Snake(x: Int = 0, y: Int = 0, direction: SnakeDirection, parts: List[BodyPart]) {
  import Snake._

  def move(boundaries: (Int, Int), apple: Apple): Snake = {
    val (x1, y1) = checkBounds(direction.move((x, y), SNAKE_MOVEMENT), boundaries)

    val body =
      if(x1 == apple.x && y1 == apple.y) BodyPart.body(x, y) +: parts
      else BodyPart.body(x, y) +: parts.dropRight(1)

    copy(
      x = x1,
      y = y1,
      parts = body
    )
  }

  def checkBounds(p: (Int, Int), b: (Int, Int)): (Int, Int) = {

    def check(a: Int, b: Int): Int = {
      if(a < 0) b
      else if(a >= b) 0
      else a
    }

    (check(p._1, b._1), check(p._2, b._2))
  }

  def collide(p: (Int, Int)): Boolean = {
    parts.exists(z => p == (z.x, z.y))
  }

  def redirect(d: SnakeDirection): Snake = {
    if(d != direction && direction != d.opposite) copy(direction = d)
    else this
  }

  def draw(b: SpriteBatch): Unit = {
    parts.foreach(_.draw(b))
    BodyPart.head(x, y).draw(b)
  }
}

object SnakeDirection {
  case object Left extends SnakeDirection {
    override def move (point: (Int, Int), distance: Int) =
      (point._1 - Math.abs(distance), point._2)

    override def opposite = Right
  }

  case object Right extends SnakeDirection {
    override def move (point: (Int, Int), distance: Int) =
      (point._1 + Math.abs(distance), point._2)

    override def opposite = Left
  }

  case object Up extends SnakeDirection {
    override def move (point: (Int, Int), distance: Int) = {
      (point._1, point._2 + Math.abs(distance))
    }

    override def opposite = Down
  }

  case object Down extends SnakeDirection {
    override def move(point: (Int, Int), distance: Int) =
      (point._1, point._2 - Math.abs(distance))

    override def opposite = Up
  }
}

trait SnakeDirection {
  def move (point: (Int, Int), distance: Int): (Int, Int)
  def opposite: SnakeDirection
}

