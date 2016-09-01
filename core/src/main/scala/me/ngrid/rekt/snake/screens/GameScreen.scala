package me.ngrid.rekt.snake.screens

import com.badlogic.gdx.{Graphics, Input, ScreenAdapter}
import com.badlogic.gdx.graphics.{Color, Texture}
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
  *
  */
object GameScreen extends ScreenAdapter {

  import com.badlogic.gdx.Gdx._
  import com.badlogic.gdx.graphics.GL20

  var batch: SpriteBatch = _
  var snakeHead: Texture = _
  val MOVE_TIME = 1F

  var timer = MOVE_TIME

  var snake = Snake(direction = Left)

  override def show(): Unit = {
    batch = new SpriteBatch()
    snakeHead = new Texture(files.internal("snake/snakehead.png"))
  }

  override def render(delta: Float): Unit = {

    snake = snake.redirect(queryInputForDirection(snake.direction))

    timer = if (timer <= 0) {
      snake = snake.move(graphics.getWidth -> graphics.getHeight)
      MOVE_TIME
    }
    else timer - delta

    val c = Color.BLACK
    gl.glClearColor(c.r, c.g, c.b, c.a)
    gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    batch.begin()
    batch.draw(snakeHead, snake.x, snake.y)
    batch.end()
  }

  def queryInputForDirection(default: Direction): Direction = {
    if(input.isKeyPressed(Input.Keys.LEFT)) Left
    else if(input.isKeyPressed(Input.Keys.RIGHT)) Right
    else if(input.isKeyPressed(Input.Keys.UP)) Up
    else if(input.isKeyPressed(Input.Keys.DOWN)) Down
    else default
  }
}

case class Snake(x: Int = 0, y: Int = 0, direction: Direction) {
  val SNAKE_MOVEMENT = 32
  def move(boundaries: (Int, Int)): Snake = {
    val(x1, y1) = checkBounds(direction.move((x, y), SNAKE_MOVEMENT), boundaries)
    copy(x = x1, y = y1)
  }

  def checkBounds(p: (Int, Int), b: (Int, Int)): (Int, Int) = {
    val x = if(p._1 >= b._1 || p._1 < 0) 0 else p._1
    val y = if(p._2 >= b._2 || p._2 < 0) 0 else p._2

    (x, y)
  }

  def redirect(d: Direction): Snake =
    if(d != direction) copy(direction = d)
    else this
}

trait Direction {
  def move(point: (Int, Int), distance: Int): (Int, Int)
}

case object Left extends Direction {
  override def move(point: (Int, Int), distance: Int) =
    (point._1 - Math.abs(distance), point._2)
}

case object Right extends Direction {
  override def move(point: (Int, Int), distance: Int) =
    (point._1 + Math.abs(distance), point._2)
}

case object Up extends Direction {
  override def move(point: (Int, Int), distance: Int) =
    (point._1, point._2 + Math.abs(distance))
}

case object Down extends Direction {
  override def move(point: (Int, Int), distance: Int) =
    (point._1, point._2 - Math.abs(distance))
}

