package battleCity.gameGUI;

import battleCity.gameControl.*;
import battleCity.gameControl.state.*;
import battleCity.gameGUI.element.*;
import battleCity.gameNetwork.*;

import java.awt.event.*;

/**
 * Created by Xia on 2017/5/27.
 * GameKeyListener
 */
public class GameKeyListener implements KeyListener {
  private SelfTank tank1, tank2;
  private GameClient tankRemote;
  private boolean isRemote, isServer;

  GameKeyListener(SelfTank tank1, SelfTank tank2){
    this.tank1 = tank1; this.tank2 = tank2;
    this.isRemote = false;
    this.isServer = true;
  }

  GameKeyListener(GameClient tankRemote) {
    this.tankRemote = tankRemote;
    this.isRemote = true;
    this.isServer = false;
  }

  GameKeyListener(SelfTank tank1){
    this.tank1 = tank1;
    this.isRemote = true;
    this.isServer = true;
  }

  public void keyTyped(KeyEvent e) {}

  public void keyPressed(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_LEFT:
        if (this.isServer)
          this.tank1.setKeyState(Key.LEFT, true);
        else
          this.tankRemote.setKeyState(Key.LEFT, true);
        break;
      case KeyEvent.VK_RIGHT:
        if (this.isServer)
          this.tank1.setKeyState(Key.RIGHT, true);
        else
          this.tankRemote.setKeyState(Key.RIGHT, true);
        break;
      case KeyEvent.VK_UP:
        if (this.isServer)
          this.tank1.setKeyState(Key.UP, true);
        else
          this.tankRemote.setKeyState(Key.UP, true);
        break;
      case KeyEvent.VK_DOWN:
        if (this.isServer)
          this.tank1.setKeyState(Key.DOWN, true);
        else
          this.tankRemote.setKeyState(Key.DOWN, true);
        break;
      case KeyEvent.VK_SPACE:
        if (this.isServer)
          this.tank1.setKeyState(Key.SHOOT, true);
        else {
          this.tankRemote.setKeyState(Key.SHOOT, true);
          SoundEffect.SHOOT.play();
        }
        break;
      case KeyEvent.VK_A:
        if (!this.isRemote)
          this.tank2.setKeyState(Key.LEFT, true);
        break;
      case KeyEvent.VK_S:
        if (!this.isRemote)
          this.tank2.setKeyState(Key.DOWN, true);
        break;
      case KeyEvent.VK_W:
        if (!this.isRemote)
          this.tank2.setKeyState(Key.UP, true);
        break;
      case KeyEvent.VK_D:
        if (!this.isRemote)
          this.tank2.setKeyState(Key.RIGHT, true);
        break;
      case KeyEvent.VK_F:
        if (!this.isRemote)
          this.tank2.setKeyState(Key.SHOOT, true);
        break;
    }
  }

  public void keyReleased(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_LEFT:
        if (this.isServer)
          this.tank1.setKeyState(Key.LEFT, false);
        else
          this.tankRemote.setKeyState(Key.LEFT, false);
        break;
      case KeyEvent.VK_RIGHT:
        if (this.isServer)
          this.tank1.setKeyState(Key.RIGHT, false);
        else
          this.tankRemote.setKeyState(Key.RIGHT, false);
        break;
      case KeyEvent.VK_UP:
        if (this.isServer)
          this.tank1.setKeyState(Key.UP, false);
        else
          this.tankRemote.setKeyState(Key.UP, false);
        break;
      case KeyEvent.VK_DOWN:
        if (this.isServer)
          this.tank1.setKeyState(Key.DOWN, false);
        else
          this.tankRemote.setKeyState(Key.DOWN, false);
        break;
      case KeyEvent.VK_SPACE:
        if (this.isServer)
          this.tank1.setKeyState(Key.SHOOT, false);
        else
          this.tankRemote.setKeyState(Key.SHOOT, false);
        break;
      case KeyEvent.VK_A:
        if (!this.isRemote)
          this.tank2.setKeyState(Key.LEFT, false);
        break;
      case KeyEvent.VK_S:
        if (!this.isRemote)
          this.tank2.setKeyState(Key.DOWN, false);
        break;
      case KeyEvent.VK_W:
        if (!this.isRemote)
          this.tank2.setKeyState(Key.UP, false);
        break;
      case KeyEvent.VK_D:
        if (!this.isRemote)
          this.tank2.setKeyState(Key.RIGHT, false);
        break;
      case KeyEvent.VK_F:
        if (!this.isRemote)
          this.tank2.setKeyState(Key.SHOOT, false);
        break;
    }
  }
}
