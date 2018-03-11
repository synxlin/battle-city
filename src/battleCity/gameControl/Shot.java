package battleCity.gameControl;

import battleCity.gameControl.state.*;

import java.io.*;

/**
 * Created by Xia on 2017/5/26.
 * Shot class
 */
public class Shot implements Serializable {
  private int x, y;
  private Direction direction;
  private boolean isSelf;
  private int id;

  Shot(int x, int y, Direction direction, boolean isSelf, int id) {
    this.x = x; this.y = y; this.direction = direction;
    this.isSelf = isSelf; this.id = id;
  }

  void move() {
    this.x = this.direction.moveX(this.x);
    this.y = this.direction.moveY(this.y);
  }

  public int getX() { return this.x; }

  public int getY() { return this.y; }

  Direction getDirection() { return this.direction; }

  boolean isSelf() { return this.isSelf; }

  int getID() { return this.id; }
}
