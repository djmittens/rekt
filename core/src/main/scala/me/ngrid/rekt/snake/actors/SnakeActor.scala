package me.ngrid.rekt.snake.actors

import akka.typed._
import akka.typed.ScalaDSL._
import akka.typed.patterns.Receptionist
import me.ngrid.rekt.snake.entities.{Snake, SnakeDirection}

/**
  *
  */
object SnakeActor {

  import SnakeCommand._

  def moving(state: State): Behavior[SnakeCommand] = {
      import state._

      Total[SnakeCommand] {
        case Tick(delta) =>
          // check if we make next move
          // check if apple collides with new head position
          Same

        case GetSnakeState(replyTo) =>
          replyTo ! SnakeState(snake)
          Same

        case SwitchDirection(direction) =>
          if (direction != nextDirection && direction.opposite != snake.direction)
            moving(state.copy(nextDirection = direction))
          else Same

        case RenderCommand.Render(replyTo) =>
          replyTo ! RenderCommand.Rendered(state.snake)
          Same
      }
  }

  def still(state: State): Behavior[SnakeCommand] = Partial[SnakeCommand] {
    case Tick(delta) =>
      Same // Ignore

    case GetSnakeState(replyTo) =>
      replyTo ! SnakeState(state.snake)
      Same

    case RenderCommand.Render(replyTo) =>
      replyTo ! RenderCommand.Rendered(state.snake)
      Same
  }

  sealed case class State(receptionist: ActorRef[Receptionist.Command],
                          snake: Snake,
                          timeToNextMove: Int,
                          nextDirection: SnakeDirection) {

  }

}

object SnakeCommand {

  case class Tick(delta: Int) extends SnakeCommand

  case object StartMoving extends SnakeCommand

  case object StopMoving extends SnakeCommand

  case class GetSnakeState(replyTo: ActorRef[SnakeCommand]) extends SnakeCommand

  case class SnakeState(snake: Snake) extends SnakeCommand

  case class SwitchDirection(direction: SnakeDirection) extends SnakeCommand
}

trait SnakeCommand
