package me.ngrid.rekt

import com.badlogic.gdx.Files.FileType
import com.badlogic.gdx.{Application, Gdx}
import com.badlogic.gdx.backends.lwjgl.{LwjglApplication, LwjglApplicationConfiguration}
import me.ngrid.rekt.snake.SnakeGame

/**
  *
  */
object SnakeLauncher extends App {
  val cfg = new LwjglApplicationConfiguration()
  cfg.title = "Snake Game"
  cfg.addIcon("snake/snakehead.png", FileType.Internal)
  cfg.width = 640
  cfg.height = 480
  new LwjglApplication(new SnakeGame(), cfg)
  Gdx.app.setLogLevel(Application.LOG_DEBUG)
}

