package battleCity.gameControl.state;

import java.io.*;

/**
 * Created by Xia on 2017/5/26.
 * Game state
 */
public enum GameState implements Serializable {
  PLAYING, WIN, LOSE, WIN_OVER, LOSE_OVER, PAUSE, ERROR;

  @Override
  public String toString() {
    switch (this) {
      case PLAYING:
        return "PLAYING";
      case WIN:
        return "WIN";
      case LOSE:
        return "LOSE";
      case WIN_OVER:
        return "WIN_OVER";
      case LOSE_OVER:
        return "LOSE_OVER";
      case PAUSE:
        return "PAUSE";
      default:
        return "ERROR";
    }
  }
}
