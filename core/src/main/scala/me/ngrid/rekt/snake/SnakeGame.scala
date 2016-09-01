package me.ngrid.rekt.snake

import com.badlogic.gdx.Game
import me.ngrid.rekt.snake.screens.GameScreen

/**
  *
  */
class SnakeGame extends Game{
  override def create(): Unit = {
    setScreen(GameScreen)
  }
}
