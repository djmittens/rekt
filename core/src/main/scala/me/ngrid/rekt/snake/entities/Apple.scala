package me.ngrid.rekt.snake.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
  *
  */
case class Apple(x: Int, y: Int){
  import com.badlogic.gdx.Gdx.files
  val tex: Texture = new Texture(files.internal("snake/apple.png"))

  def draw(b: SpriteBatch): Unit = b.draw(tex, x, y)
}
