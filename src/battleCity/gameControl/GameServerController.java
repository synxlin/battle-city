package battleCity.gameControl;

import battleCity.gameControl.state.*;
import battleCity.gameGUI.*;
import battleCity.gameGUI.element.*;

import java.util.*;

/**
 * Created by Xia on 2017/5/26.
 * Game Main Controller
 */
public class GameServerController extends Thread {
  public GameInfo gameInfo;
  private GameGUI gameGUI;
  private long lastGenerateEnemyTime;

  public GameServerController(GameGUI gameGUI) {
    this.gameInfo = new GameInfo();
    this.gameGUI = gameGUI;
    int level = new Random().nextInt(3);
    for (int i = 0; i < GameConfig.boardInfoSize; i++) {
      for (int j = 0; j < GameConfig.boardInfoSize; j++) {
        this.gameInfo.boardInfo[i][j] = CellState.values()[GameConfig.initBoardInfo[level][i][j]];
        this.gameInfo.grassInfo[i][j] = GameConfig.initGrassInfo[level][i][j];
      }
    }
    this.lastGenerateEnemyTime = new Date().getTime() - GameConfig.enemyReviveTimeLapse - 10;
    generateEnemyTank(3, 1);
  }

  public void registerTank(Tank tank) {
    this.gameInfo.tanks.add(0, tank);
    this.setBoardTankRangeState(tank.getX(), tank.getY(), CellState.TANK);
  }

  private void generateEnemyTank(int n, int initHP) {
    long time = new Date().getTime();
    if (this.gameInfo.numEnemyLeft > 0 &&
            time - this.lastGenerateEnemyTime >= GameConfig.enemyReviveTimeLapse) {
      this.lastGenerateEnemyTime = time;
      int t = new Random().nextInt(3);
      n = n % 4;
      for (int i = 0; i < n; i++) {
        initHP = initHP == 0 ? (new Random().nextInt(5) == 4 ? 2 : 1) : initHP;
        EnemyTank tank = new EnemyTank(GameConfig.initXenemy[t], GameConfig.initYenemy[t], initHP);
        this.gameInfo.tanks.add(tank);
        this.setBoardTankRangeState(tank.getX(), tank.getY(), CellState.TANK);
        this.gameInfo.numEnemyLeft--;
        if (this.gameInfo.numEnemyLeft == 0)
          break;
        t = (t + 1) % 3;
      }
    }
  }

  private void setBoardTankRangeState(int x, int y, CellState state) {
    this.gameInfo.boardInfo[y][x] = state;
    this.gameInfo.boardInfo[y][x+1] = state;
    this.gameInfo.boardInfo[y+1][x] = state;
    this.gameInfo.boardInfo[y+1][x+1] = state;
  }

  private void settleDirection() {
    for ( Tank tank: this.gameInfo.tanks )
      tank.setDirection(this.gameInfo);
  }

  private void generateShot() {
    for ( Tank tank: this.gameInfo.tanks ){
      if (tank.isShoot()) {
        Direction direction = tank.getDirection();
        Shot shot = new Shot(direction.moveX(tank.getX()), direction.moveY(tank.getY()),
                direction, tank.isSelf(), tank.getID());
        this.gameInfo.shots.add(shot);
        if (shot.isSelf())
          SoundEffect.SHOOT.play();
      }
    }
  }

  private void settleShot() {
    // hit each other
    for (int i = 0; i < this.gameInfo.shots.size() - 1; i++) {
      boolean isHit = false;
      for ( int j = i+1; j < this.gameInfo.shots.size(); j++) {
        Shot shot1= this.gameInfo.shots.get(i);
        Shot shot2 = this.gameInfo.shots.get(j);
        if (shot1.getX() == shot2.getX() && shot1.getY() == shot2.getY()) {
          this.gameInfo.shots.remove(j);
          isHit = true;
          j--;
        }
        switch (shot1.getDirection()) {
          case UP:
            if (shot2.getDirection() == Direction.DOWN && shot1.getX() == shot2.getX() &&
                    shot1.getY() - shot2.getY() == 1) {
              this.gameInfo.shots.remove(j);
              isHit = true;
              j--;
            }
            break;
          case DOWN:
            if (shot2.getDirection() == Direction.UP && shot1.getX() == shot2.getX() &&
                    shot2.getY() - shot1.getY() == 1) {
              this.gameInfo.shots.remove(j);
              isHit = true;
              j--;
            }
          case LEFT:
            if (shot2.getDirection() == Direction.RIGHT && shot1.getY() == shot2.getY() &&
                    shot1.getX() - shot2.getX() == 1) {
              this.gameInfo.shots.remove(j);
              isHit = true;
              j--;
            }
          case RIGHT:
            if (shot2.getDirection() == Direction.LEFT && shot1.getY() == shot2.getY() &&
                    shot2.getX() - shot1.getX() == 1) {
              this.gameInfo.shots.remove(j);
              isHit = true;
              j--;
            }
        }
      }
      if (isHit) {
        this.gameInfo.shots.remove(i);
        i--;
      }
    }

    // hit wall
    Iterator<Shot> shotIterator = this.gameInfo.shots.iterator();
    while (shotIterator.hasNext()) {
      Shot shot = shotIterator.next();
      int shotX = shot.getX(), shotY = shot.getY();
      boolean shotFlag = false;
      // normal wall
      if (this.gameInfo.boardInfo[shotY][shotX] == CellState.NORMAL_WALL) {
        this.gameInfo.boardInfo[shotY][shotX] = CellState.EMPTY;
        shotFlag = true;
      }
      if (this.gameInfo.boardInfo[shotY][shotX+1] == CellState.NORMAL_WALL) {
        this.gameInfo.boardInfo[shotY][shotX+1] = CellState.EMPTY;
        shotFlag = true;
      }
      if (this.gameInfo.boardInfo[shotY+1][shotX] == CellState.NORMAL_WALL) {
        this.gameInfo.boardInfo[shotY+1][shotX] = CellState.EMPTY;
        shotFlag = true;
      }
      if (this.gameInfo.boardInfo[shotY+1][shotX+1] == CellState.NORMAL_WALL) {
        this.gameInfo.boardInfo[shotY+1][shotX+1] = CellState.EMPTY;
        shotFlag = true;
      }
      // steel wall
      if (this.gameInfo.boardInfo[shotY][shotX] == CellState.STEEL_WALL)
        shotFlag = true;
      if (this.gameInfo.boardInfo[shotY][shotX+1] == CellState.STEEL_WALL)
        shotFlag = true;
      if (this.gameInfo.boardInfo[shotY+1][shotX] == CellState.STEEL_WALL)
        shotFlag = true;
      if (this.gameInfo.boardInfo[shotY+1][shotX+1] == CellState.STEEL_WALL)
        shotFlag = true;
      if (shotFlag)
        shotIterator.remove();
    }
  }

  private void settleDeath() {
    Iterator<Shot> shotIterator = this.gameInfo.shots.iterator();
    while (shotIterator.hasNext()) {
      Shot shot = shotIterator.next();
      int shotX = shot.getX(), shotY = shot.getY();
      Direction direction = shot.getDirection();
      boolean isSelf = shot.isSelf(); int shotID = shot.getID();
      boolean shotFlag = false;
      Iterator<Tank> tankIterator = this.gameInfo.tanks.iterator();
      switch (direction) {
        case UP:
          while (tankIterator.hasNext()) {
            Tank tank = tankIterator.next();
            if (tank.isSelf() != isSelf) {
              int tankX = tank.getX(), tankY = tank.getY();
              if ((shotY == tankY || shotY - 1 == tankY) && (shotX == tankX || shotX + 1 == tankX ||
                      shotX - 1 == tankX)) {
                shotFlag = true;
                if (tank.underAttacked()) {
                  this.setBoardTankRangeState(tankX, tankY, CellState.EMPTY);
                  if (tank.isDead()) {
                    tankIterator.remove();
                    if (!tank.isSelf()) {
                      if (tank.initHP == 1)
                        this.gameInfo.numNormalEnemyKilled[shotID]++;
                      else
                        this.gameInfo.numStrongEnemyKilled[shotID]++;
                    }
                  }
                  else
                    this.setBoardTankRangeState(tank.getX(), tank.getY(), CellState.TANK);
                }
              }
            }
          }
          break;
        case DOWN:
          while (tankIterator.hasNext()) {
            Tank tank = tankIterator.next();
            if (tank.isSelf() != isSelf) {
              int tankX = tank.getX(), tankY = tank.getY();
              if ((shotY == tankY || shotY + 1 == tankY) && (shotX == tankX || shotX + 1 == tankX ||
                      shotX - 1 == tankX)) {
                shotFlag = true;
                if (tank.underAttacked()) {
                  this.setBoardTankRangeState(tankX, tankY, CellState.EMPTY);
                  if (tank.isDead()) {
                    tankIterator.remove();
                    if (!tank.isSelf()) {
                      if (tank.initHP == 1)
                        this.gameInfo.numNormalEnemyKilled[shotID]++;
                      else
                        this.gameInfo.numStrongEnemyKilled[shotID]++;
                    }
                  }
                  else
                    this.setBoardTankRangeState(tank.getX(), tank.getY(), CellState.TANK);
                }
              }
            }
          }
          break;
        case LEFT:
          while (tankIterator.hasNext()) {
            Tank tank = tankIterator.next();
            if (tank.isSelf() != isSelf) {
              int tankX = tank.getX(), tankY = tank.getY();
              if ((shotX == tankX || shotX - 1 == tankX) && (shotY == tankY || shotY + 1 == tankY ||
                      shotY - 1 == tankY)) {
                shotFlag = true;
                if (tank.underAttacked()) {
                  this.setBoardTankRangeState(tankX, tankY, CellState.EMPTY);
                  if (tank.isDead()) {
                    tankIterator.remove();
                    if (!tank.isSelf()) {
                      if (tank.initHP == 1)
                        this.gameInfo.numNormalEnemyKilled[shotID]++;
                      else
                        this.gameInfo.numStrongEnemyKilled[shotID]++;
                    }
                  }
                  else
                    this.setBoardTankRangeState(tank.getX(), tank.getY(), CellState.TANK);
                }
              }
            }
          }
          break;
        case RIGHT:
          while (tankIterator.hasNext()) {
            Tank tank = tankIterator.next();
            if (tank.isSelf() != isSelf) {
              int tankX = tank.getX(), tankY = tank.getY();
              if ((shotX == tankX || shotX + 1 == tankX) && (shotY == tankY || shotY + 1 == tankY ||
                      shotY - 1 == tankY)) {
                shotFlag = true;
                if (tank.underAttacked()) {
                  this.setBoardTankRangeState(tankX, tankY, CellState.EMPTY);
                  if (tank.isDead()) {
                    tankIterator.remove();
                    if (!tank.isSelf()) {
                      if (tank.initHP == 1)
                        this.gameInfo.numNormalEnemyKilled[shotID]++;
                      else
                        this.gameInfo.numStrongEnemyKilled[shotID]++;
                    }
                  }
                  else
                    this.setBoardTankRangeState(tank.getX(), tank.getY(), CellState.TANK);
                }
              }
            }
          }
          break;
      }
      if (shotFlag)
        shotIterator.remove();
    }
  }

  private void settleShotMove() {
    for (Shot shot: this.gameInfo.shots) {
      shot.move();
    }
  }

  private void settleTankMove() {
    for (Tank tank: this.gameInfo.tanks) {
      if (tank.isMoved()) {
        Direction direction = tank.getDirection();
        int X = tank.getX(), Y = tank.getY();
        int moveX = direction.moveX(tank.getX()), moveY = direction.moveY(tank.getY());
        switch (direction) {
          case UP:
            if (this.gameInfo.boardInfo[moveY][moveX] == CellState.EMPTY &&
                    this.gameInfo.boardInfo[moveY][moveX+1] == CellState.EMPTY) {
              this.setBoardTankRangeState(X, Y, CellState.EMPTY);
              tank.move();
              this.setBoardTankRangeState(moveX, moveY, CellState.TANK);
            }
            break;
          case DOWN:
            if (this.gameInfo.boardInfo[moveY+1][moveX] == CellState.EMPTY &&
                    this.gameInfo.boardInfo[moveY+1][moveX+1] == CellState.EMPTY) {
              this.setBoardTankRangeState(X, Y, CellState.EMPTY);
              tank.move();
              this.setBoardTankRangeState(moveX, moveY, CellState.TANK);
            }
            break;
          case LEFT:
            if (this.gameInfo.boardInfo[moveY][moveX] == CellState.EMPTY &&
                    this.gameInfo.boardInfo[moveY+1][moveX] == CellState.EMPTY) {
              this.setBoardTankRangeState(X, Y, CellState.EMPTY);
              tank.move();
              this.setBoardTankRangeState(moveX, moveY, CellState.TANK);
            }
            break;
          case RIGHT:
            if (this.gameInfo.boardInfo[moveY][moveX+1] == CellState.EMPTY &&
                    this.gameInfo.boardInfo[moveY+1][moveX+1] == CellState.EMPTY) {
              this.setBoardTankRangeState(X, Y, CellState.EMPTY);
              tank.move();
              this.setBoardTankRangeState(moveX, moveY, CellState.TANK);
            }
            break;
        }
      }
    }
  }

  private void settleOver() {
    boolean isBaseOver = false;
    for (int i = 0; i < GameConfig.initXbase.length; i++)
      if (this.gameInfo.boardInfo[GameConfig.initYbase[i]][GameConfig.initXbase[i]] == CellState
              .EMPTY)
        isBaseOver = true;
    if (isBaseOver)
        this.gameInfo.state = GameState.LOSE;
    else {
      int numSelf = 0, numEnemy = 0;
      for ( Tank tank: this.gameInfo.tanks )
        if (tank.isSelf())
          numSelf++;
        else
          numEnemy++;
      if (numSelf==0)
        this.gameInfo.state = GameState.LOSE;
      else if (numEnemy == 0 && this.gameInfo.numEnemyLeft == 0) {
        this.gameInfo.state = GameState.WIN;
      }
    }
  }

  public void run() {
    while (this.gameInfo.state == GameState.PLAYING || this.gameInfo.state == GameState.PAUSE) {
      long lastTime = new Date().getTime();
      synchronized (this.gameInfo.stateLock) {
        if (this.gameInfo.state == GameState.PLAYING) {
          // generate enemy
          this.generateEnemyTank(1, 0);
          // settle direction
          this.settleDirection();
          // generate shot
          this.generateShot();
          // settle shot
          this.settleShot();
          // settle death
          this.settleDeath();
          // settle move
          this.settleShotMove();
          this.settleTankMove();
          // settle shot to simulate speed
          for (int i = 0; i < GameConfig.speedShot - 1; i++) {
            this.gameGUI.repaint();
            try {
              Thread.sleep(GameConfig.freshTimeLapse / GameConfig.speedShot);
            } catch (InterruptedException e) {
              e.printStackTrace();
              System.exit(1);
            }
            this.settleShot();
            this.settleDeath();
            this.settleShotMove();
          }
          // settle over
          this.settleOver();
          // settle repaint
          this.gameGUI.repaint();
        }
      }
      // sleeping
      long time = new Date().getTime();
      long waitingTime = GameConfig.freshTimeLapse - (time - lastTime);
      if (waitingTime > 0) {
        try {
          Thread.sleep(waitingTime);
        } catch (InterruptedException e) {
          e.printStackTrace();
          System.exit(1);
        }
      }
    }
    synchronized (this.gameInfo.stateLock) {
      this.gameInfo.stateLock.notifyAll();
    }
  }
}
