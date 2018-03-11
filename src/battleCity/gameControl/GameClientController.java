package battleCity.gameControl;

import battleCity.gameControl.state.*;
import battleCity.gameGUI.*;

import java.util.*;

/**
 * Created by Xia on 2017/5/27.
 */
public class GameClientController extends Thread {
  private GameInfo gameInfo;
  private GameGUI gameGUI;

  public GameClientController(GameGUI gameGUI, GameInfo gameInfo) {
    this.gameInfo = gameInfo;
    this.gameGUI = gameGUI;
  }

  public void run() {
    while (this.gameInfo.state == GameState.PLAYING || this.gameInfo.state == GameState.PAUSE) {
      long lastTime = new Date().getTime();
      // repaintGUI
      synchronized (this.gameInfo.stateLock) {
        this.gameGUI.repaint();
      }
      // sleeping
      long time = new Date().getTime();
      try {
        Thread.sleep(GameConfig.freshTimeLapse - (time - lastTime));
      } catch (InterruptedException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }
  }
}
