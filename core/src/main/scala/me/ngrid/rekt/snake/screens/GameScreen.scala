package me.ngrid.rekt.snake.screens

import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import me.ngrid.rekt.snake.entities.{Apple, Snake}
import me.ngrid.rekt.snake.input.InputSignalSampler

/**
  *
  */
object GameScreen extends ScreenAdapter {

  import com.badlogic.gdx.Gdx._
  import com.badlogic.gdx.graphics.GL20

  val MOVE_TIME = 0.25F
  val GRID_CELL = 32

  var batch: SpriteBatch = _
  var shapeRenderer: ShapeRenderer = _

  var timer = MOVE_TIME
  var sammy = Snake(0, 0)

  var apple: Apple = _
  var appleShown = false

  var inputSampler: InputSignalSampler = InputSignalSampler()
  var playing = true

  override def show(): Unit = {
    batch = new SpriteBatch()
    shapeRenderer = new ShapeRenderer()
  }

  override def render(delta: Float): Unit = {
    inputSampler = inputSampler.poll(input)

    timer = if (playing && timer <= 0) {
      sammy = inputSampler.
        sampleDirection.
        map(sammy.redirect).
        getOrElse(sammy).
        move(graphics.getWidth -> graphics.getHeight, apple)

      playing = !sammy.collide(sammy.x -> sammy.y)

      inputSampler = InputSignalSampler()
      MOVE_TIME
    }
    else timer - delta

    checkAppleSnakeCollision()
    placeApple()
    clearScreen()
    drawGrid()
    draw()
  }

  def placeApple(): Unit = {
    if(!appleShown) {
      var x: Int = 0
      var y: Int = 0

      do {
        x = MathUtils.random(graphics.getWidth / Snake.SNAKE_MOVEMENT - 1) * Snake.SNAKE_MOVEMENT
        y = MathUtils.random(graphics.getHeight / Snake.SNAKE_MOVEMENT - 1) * Snake.SNAKE_MOVEMENT

      } while (sammy.collide(x -> y))

      apple = Apple(x, y)
      appleShown = true
    }
  }

  def clearScreen(): Unit = {
    val c = Color.BLACK
    gl.glClearColor(c.r, c.g, c.b, c.a)
    gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
  }

  def draw(): Unit = {
    batch.begin()
    sammy.draw(batch)
    apple.draw(batch)
    batch.end()
  }

  def drawGrid(): Unit = {
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
    for{
      x <- 0.to(graphics.getWidth, GRID_CELL)
      y <- 0.to(graphics.getHeight, GRID_CELL)
    } {
      shapeRenderer.rect(x,y, GRID_CELL, GRID_CELL)
    }
    shapeRenderer.end()
  }

  def checkAppleSnakeCollision(): Unit = {
    if(appleShown && apple.x == sammy.x && apple.y == sammy.y) appleShown = false
  }
}

