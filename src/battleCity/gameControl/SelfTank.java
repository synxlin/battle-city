package battleCity.gameControl;

import battleCity.gameControl.state.*;

import java.io.*;
import java.util.*;

/**
 * Created by Xia on 2017/5/26.
 * self tank class
 */
public class SelfTank extends Tank implements Serializable {
  private KeyState keyState, shootState;
  private int id;

  public SelfTank(int id, int initX, int initY) {
    super();
    this.id = id; this.isSelf = true; this.isMoved = false;
    this.x = initX; this.initX = initX;
    this.y = initY; this.initY = initY;
    this.initHP = 1; this.HP = initHP;
    this.numLife = GameConfig.numSelfLife;
    this.direction = Direction.UP;
    this.keyState = new KeyState(Key.NOTHING, false);
    this.shootState = new KeyState(Key.SHOOT, false);
  }

  // id
  public int getID() { return this.id; }
  // key
  public void setKeyState(Key key, boolean pressed) {
    if (this.keyState.getKey() == key)
      this.keyState.setState(pressed);
    else if (key == Key.SHOOT)
      this.shootState.setState(pressed);
    else if (key != Key.NOTHING && pressed)
      this.keyState = new KeyState(key, true);
  }
  // shoot
  boolean isShoot() {
    long time = new Date().getTime();
    long shootTimeLapse = time - this.lastShootTime;
    if (shootTimeLapse > GameConfig.shootTimeLapse && this.shootState.getState() != PressState
            .NEVER_PRESSED) {
      this.shootState.settleState();
      this.lastShootTime = time;
      return true;
    }
    this.shootState.settleState();
    return false;
  }
  // move
  void setDirection(GameInfo gameInfo) {
    Direction keyDirection = this.keyState.toDirection();
    this.keyState.settleState();
    if (keyDirection != Direction.STAY) {
      this.direction = keyDirection;
      this.isMoved = true;
    }
  }
}
