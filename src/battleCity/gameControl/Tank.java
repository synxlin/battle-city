package battleCity.gameControl;

import battleCity.gameControl.state.*;

import java.io.*;
import java.util.*;

/**
 * Created by Xia on 2017/5/26.
 * general tank class
 */
public class Tank implements Serializable {
  boolean isSelf, isMoved;
  int x, y, initX, initY, HP, initHP, numLife;
  Direction direction;
  long lastShootTime;

  Tank() {
    this.lastShootTime = new Date().getTime() - 1000;
    this.isMoved = true;
  }

  // identity
  public boolean isSelf() { return this.isSelf; }

  public int getID() { return this.HP - 1; }
  // move
  void move() {
    this.x = this.direction.moveX(this.x);
    this.y = this.direction.moveY(this.y);
  }

  public int getX() { return this.x; }

  public int getY() { return this.y; }

  void setDirection(GameInfo gameInfo) { this.isMoved = true; }

  public Direction getDirection() { return this.direction; }

  boolean isMoved() {
    boolean isMoved = this.isMoved;
    this.isMoved = false;
    return isMoved;
  }
  // shoot
  boolean isShoot() {
    long time = new Date().getTime();
    long shootTimeLapse = time - this.lastShootTime;
    if (shootTimeLapse > GameConfig.shootTimeLapse) {
      this.lastShootTime = time;
      return true;
    }
    return false;
  }
  // hp
  boolean underAttacked() {
    this.HP--;
    if (this.HP <= 0) {
      this.numLife--;
      this.HP = this.initHP;
      this.x = this.initX; this.y = this.initY; this.isMoved = false;
      this.direction = Direction.UP; // only selfTank will return to origin
      return true;
    }
    return false;
  }

  boolean isDead() { return this.numLife <= 0; }

  public int getNumLife() { return this.numLife; }
}
