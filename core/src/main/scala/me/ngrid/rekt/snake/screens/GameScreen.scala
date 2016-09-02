package me.ngrid.rekt.snake.screens

import com.badlogic.gdx.{Graphics, Input, ScreenAdapter}
import com.badlogic.gdx.graphics.{Color, Texture}
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.utils.Drawable

/**
  *
  */
object GameScreen extends ScreenAdapter {

  import com.badlogic.gdx.Gdx._
  import com.badlogic.gdx.graphics.GL20
  val MOVE_TIME = 0.5F

  var batch: SpriteBatch = _

  var timer = MOVE_TIME
  var snake = Snake(0, 0)

  var apple: Apple = _
  var appleShown = false

  override def show(): Unit = {
    batch = new SpriteBatch()
  }

  override def render(delta: Float): Unit = {

    snake = snake.redirect(queryInputForDirection(snake.direction))

    timer = if (timer <= 0) {
      snake = snake.move(graphics.getWidth -> graphics.getHeight, apple)
      MOVE_TIME
    }
    else timer - delta

    checkAppleSnakeCollision()
    placeApple()
    clearScreen()
    draw()
  }


  def queryInputForDirection(default: Direction): Direction = {
    if(input.isKeyPressed(Input.Keys.LEFT)) Left
    else if(input.isKeyPressed(Input.Keys.RIGHT)) Right
    else if(input.isKeyPressed(Input.Keys.UP)) Up
    else if(input.isKeyPressed(Input.Keys.DOWN)) Down
    else default
  }

  def placeApple(): Unit = {
    if(!appleShown) {
      do {
        val x = MathUtils.random(graphics.getWidth / Snake.SNAKE_MOVEMENT - 1) * Snake.SNAKE_MOVEMENT
        val y = MathUtils.random(graphics.getHeight / Snake.SNAKE_MOVEMENT - 1) * Snake.SNAKE_MOVEMENT
        apple = Apple(x, y)
        appleShown = true
      } while (apple.x == snake.x && apple.x == snake.y)
    }
  }

  def clearScreen(): Unit = {
    val c = Color.BLACK
    gl.glClearColor(c.r, c.g, c.b, c.a)
    gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
  }

  def draw(): Unit = {
    batch.begin()
    snake.draw(batch)
    apple.draw(batch)
    batch.end()
  }

  def checkAppleSnakeCollision(): Unit = {
    if(appleShown && apple.x == snake.x && apple.y == snake.y) appleShown = false
  }
}

object Snake {
  val SNAKE_MOVEMENT = 32

  def apply(x: Int, y: Int): Snake = Snake(x, y, Left, List(BodyPart.head(x, y)))
}

case class Snake(x: Int = 0, y: Int = 0, direction: Direction, parts: List[BodyPart]) {
  import Snake._

  def move(boundaries: (Int, Int), apple: Apple): Snake = {
    val head = BodyPart.head(checkBounds(direction.move((x, y), SNAKE_MOVEMENT), boundaries))

    val body =
      if(head.x == apple.x && head.y == apple.y) BodyPart.body(x, y) +: parts.tail
      else BodyPart.body(x, y) +: parts.tail.dropRight(1)

    copy(
      x = head.x,
      y = head.y,
      parts = head +: body
    )
  }

  def checkBounds(p: (Int, Int), b: (Int, Int)): (Int, Int) = {
    val x = if(p._1 >= b._1 || p._1 < 0) 0 else p._1
    val y = if(p._2 >= b._2 || p._2 < 0) 0 else p._2

    (x, y)
  }

  def redirect(d: Direction): Snake =
    if(d != direction) copy(direction = d)
    else this

  def draw(b: SpriteBatch): Unit = parts.foreach(_.draw(b))
}

case class Apple(x: Int, y: Int){
  import com.badlogic.gdx.Gdx.files
  val tex: Texture = new Texture(files.internal("snake/apple.png"))

  def draw(b: SpriteBatch): Unit = b.draw(tex, x, y)
}

object BodyPart {
  import com.badlogic.gdx.Gdx.files

  def head(pos: (Int, Int)):BodyPart = head(pos._1, pos._2)
  def head(x: Int, y: Int):BodyPart = BodyPart(x, y, new Texture(files.internal("snake/snakehead.png")))

  def body(x: Int, y: Int) = BodyPart(x, y, new Texture(files.internal("snake/snakebody.png")))
}

case class BodyPart(x: Int, y: Int, tex: Texture) {
  def draw(batch: SpriteBatch): Unit = batch.draw(tex, x, y)
}

trait Direction {
  def move (point: (Int, Int), distance: Int): (Int, Int)
}

case object Left extends Direction {
  override def move (point: (Int, Int), distance: Int) =
    (point._1 - Math.abs(distance), point._2)
}

case object Right extends Direction {
  override def move (point: (Int, Int), distance: Int) = {
    (point._1 + Math.abs(distance), point._2)
  }
}

case object Up extends Direction {
  override def move (point: (Int, Int), distance: Int) = {
    (point._1, point._2 + Math.abs(distance))
  }
}

case object Down extends Direction {
  override def move(point: (Int, Int), distance: Int) =
    (point._1, point._2 - Math.abs(distance))
}

