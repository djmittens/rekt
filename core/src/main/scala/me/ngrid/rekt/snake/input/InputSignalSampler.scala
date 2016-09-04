package me.ngrid.rekt.snake.input

import com.badlogic.gdx.Input
import me.ngrid.rekt.snake.entities.SnakeDirection

/**
  *
  */
case class InputSignalSampler(directionEvents: List[SnakeDirection] = Nil) {
  def poll(i: Input): InputSignalSampler = {
    queryInputForDirection(i).
      map(e => copy(e +: directionEvents)).
      getOrElse(this)
  }

  def sampleDirection: Option[SnakeDirection] = directionEvents.headOption

  def queryInputForDirection(input: Input): Option[SnakeDirection] = {
    if(input.isKeyPressed(Input.Keys.LEFT)) Some(SnakeDirection.Left)
    else if(input.isKeyPressed(Input.Keys.RIGHT)) Some(SnakeDirection.Right)
    else if(input.isKeyPressed(Input.Keys.UP)) Some(SnakeDirection.Up)
    else if(input.isKeyPressed(Input.Keys.DOWN)) Some(SnakeDirection.Down)
    else None
  }

}
