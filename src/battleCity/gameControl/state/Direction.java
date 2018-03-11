package battleCity.gameControl.state;

import java.io.*;

/**
 * Created by Xia on 2017/5/26.
 * Direction class
 */
public enum Direction implements Serializable {
  UP, DOWN, LEFT, RIGHT, STAY;
  public int moveX(int x) {
    switch (this) {
      case LEFT:
        return x-1;
      case RIGHT:
        return x+1;
      default:
        return x;
    }
  }
  public int moveY(int y) {
    switch (this) {
      case UP:
        return y-1;
      case DOWN:
        return y+1;
      default:
        return y;
    }
  }
}
