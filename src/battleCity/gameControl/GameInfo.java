package battleCity.gameControl;

import battleCity.gameControl.state.*;

import java.util.*;
import java.io.*;

/**
 * Created by Xia on 2017/5/26.
 * game information class
 */
public class GameInfo implements Serializable {
  public int[][] grassInfo;
  public CellState[][] boardInfo;
  public ArrayList<Tank> tanks;
  public ArrayList<Shot> shots;
  public int numEnemyLeft;
  public int[] numNormalEnemyKilled, numStrongEnemyKilled;
  public GameState state;
  public final Object stateLock = new Object[0];

  public GameInfo() {
    this.boardInfo = new CellState[GameConfig.boardInfoSize][GameConfig.boardInfoSize];
    this.grassInfo = new int[GameConfig.boardInfoSize][GameConfig.boardInfoSize];
    this.tanks = new ArrayList<>();
    this.shots = new ArrayList<>();
    this.numEnemyLeft = GameConfig.numEnemy;
    this.state = GameState.PLAYING;
    this.numNormalEnemyKilled = new int[2];
    this.numNormalEnemyKilled[0] = 0; this.numNormalEnemyKilled[1] = 0;
    this.numStrongEnemyKilled = new int[2];
    this.numStrongEnemyKilled[0] = 0; this.numStrongEnemyKilled[1] = 0;
  }

  public void update(GameInfo gameInfo) {
    this.boardInfo = gameInfo.boardInfo;
    this.tanks = gameInfo.tanks;
    this.shots = gameInfo.shots;
    this.numEnemyLeft = gameInfo.numEnemyLeft;
    this.numNormalEnemyKilled = gameInfo.numNormalEnemyKilled;
    this.numStrongEnemyKilled = gameInfo.numStrongEnemyKilled;
    if (this.state != GameState.ERROR)
      this.state = gameInfo.state;
  }
}
