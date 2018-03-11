package battleCity.gameControl.state;

import java.io.*;

/**
 * Created by Xia on 2017/5/26.
 * enumeration of key 'a' 'w' 's' 'd' etc.
 */
public enum Key implements Serializable {
  UP, DOWN, LEFT, RIGHT, SHOOT, NOTHING;

  @Override
  public String toString() {
    switch (this) {
      case UP:
        return "UP";
      case DOWN:
        return "DOWN";
      case LEFT:
        return "LEFT";
      case RIGHT:
        return "RIGHT";
      case SHOOT:
        return "SHOOT";
      default:
        return "NOTHING";
    }
  }
}
