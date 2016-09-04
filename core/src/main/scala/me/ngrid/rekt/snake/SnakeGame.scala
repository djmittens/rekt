package me.ngrid.rekt.snake

import akka.typed._
import akka.typed.ScalaDSL._
import com.badlogic.gdx.Game
import me.ngrid.rekt.snake.actors.RegistryCommand
import me.ngrid.rekt.snake.screens.GameScreen

class SnakeGame extends Game {
  override def create(): Unit = {
    setScreen(GameScreen)
  }
}

object SnakeGame {
  import GameCommand._
  val start: Behavior[GameCommand] = ContextAware[GameCommand] { ctx =>
    FullTotal[GameCommand] {
      case PreStart =>

    }
  }

  def running(snake: ActorRef[SnakeCommand],
              apple: ActorRef[AppleCommand],
              renderRegistry: ActorRef[RegistryCommand]
             ): Behavior[GameCommand] = {
    case Tick(delta) =>
      snake ! Snake
  }
}

object GameCommand {
  case object LoadGame extends GameCommand
  case class Tick(delta: Int) extends GameCommand
  case object Render extends GameCommand
  case object RenderFinished extends GameCommand
  case class RenderFailed(e: Throwable) extends GameCommand
}
trait GameCommand
