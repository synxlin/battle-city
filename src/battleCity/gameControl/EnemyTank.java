package battleCity.gameControl;

import battleCity.gameControl.state.*;

import java.io.*;
import java.util.*;

/**
 * Created by Xia on 2017/5/26.
 * enemy tank class
 */
class EnemyTank extends Tank implements Serializable {
  private long lastTurnTime;

  EnemyTank(int initX, int initY, int initHP) {
    super();
    this.isSelf = false; this.isMoved = true;
    this.x = initX; this.initX = initX;
    this.y = initY; this.initY = initY;
    this.HP = initHP; this.initHP = initHP;
    this.numLife = 1;
    this.direction = Direction.DOWN;
    this.lastTurnTime = new Date().getTime() - GameConfig.enemyTurnTimeLapse;
  }

  void setDirection(GameInfo gameInfo) {
    long time = new Date().getTime();
    if (time - this.lastTurnTime > GameConfig.enemyTurnTimeLapse) {
      this.lastTurnTime = time;
      int r = new Random().nextInt(4);
      this.direction = Direction.values()[r];
    }
    this.isMoved = true;
  }
}
