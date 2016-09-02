package me.ngrid.rekt.snake.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
  *
  */
object BodyPart {
  import com.badlogic.gdx.Gdx.files

  def head(pos: (Int, Int)):BodyPart = head(pos._1, pos._2)
  def head(x: Int, y: Int):BodyPart = BodyPart(x, y, new Texture(files.internal("snake/snakehead.png")))

  def body(x: Int, y: Int) = BodyPart(x, y, new Texture(files.internal("snake/snakebody.png")))
}

case class BodyPart(x: Int, y: Int, tex: Texture) {
  def draw(batch: SpriteBatch): Unit = batch.draw(tex, x, y)
}