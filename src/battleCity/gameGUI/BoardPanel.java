package battleCity.gameGUI;

import battleCity.gameControl.*;
import battleCity.gameControl.state.*;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

/**
 * Created by Xia on 2017/5/26.
 * Panel for game board
 */
public class BoardPanel extends JPanel {
  private GameInfo gameInfo;
  private BufferedImage background, wall, steel, sea, grass, base, baseLose;
  private BufferedImage[][] selfTank, enemyTank;

  BoardPanel(GameInfo gameInfo) {
    this.gameInfo = gameInfo;
    this.setPreferredSize(new Dimension(GUIConfig.boardSize, GUIConfig.boardSize));
    this.setFocusable(true);
    try {
      this.background = ImageIO.read(getClass().getResource("/images/board/background.png"));
      this.selfTank = new BufferedImage[2][4];
      this.enemyTank = new BufferedImage[2][4];
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 2; j++) {
          this.selfTank[j][i] = ImageIO.read(getClass().getResource("/images/board/selfTank_" +
                  j + "_" + i + ".png"));
          this.enemyTank[j][i] = ImageIO.read(getClass().getResource("/images/board/enemyTank_" +
                  j + "_" + i + ".png"));
        }
      }
      this.base = ImageIO.read(getClass().getResource("/images/board/base.png"));
      this.baseLose = ImageIO.read(getClass().getResource("/images/board/baseLose.png"));
      this.wall = ImageIO.read(getClass().getResource("/images/board/wall.png"));
      this.steel = ImageIO.read(getClass().getResource("/images/board/steel.png"));
      this.sea = ImageIO.read(getClass().getResource("/images/board/sea.png"));
      this.grass = ImageIO.read(getClass().getResource("/images/board/grass.png"));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  @Override
  public void paintComponent(Graphics gr) {
    super.paintComponent(gr);
    Graphics2D g = (Graphics2D)gr;
    g.drawImage(this.background, 0, 0, GUIConfig.boardSize, GUIConfig.boardSize, null);
    for (int y = 1; y < GameConfig.boardInfoSize-1; y++) {
      for (int x = 1; x < GameConfig.boardInfoSize - 1; x++) {
        switch (this.gameInfo.boardInfo[y][x]) {
          case NORMAL_WALL:
            g.drawImage(this.wall, (x - 1) * GUIConfig.blockSize, (y - 1) * GUIConfig.blockSize,
                    GUIConfig.blockSize, GUIConfig.blockSize, this);
            break;
          case STEEL_WALL:
            g.drawImage(this.steel, (x - 1) * GUIConfig.blockSize, (y - 1) * GUIConfig.blockSize,
                    GUIConfig.blockSize, GUIConfig.blockSize, this);
            break;
          case SEA:
            g.drawImage(this.sea, (x - 1) * GUIConfig.blockSize, (y - 1) * GUIConfig.blockSize,
                    GUIConfig.blockSize, GUIConfig.blockSize, this);
            break;
        }
      }
    }
    if (this.gameInfo.state == GameState.LOSE)
      g.drawImage(this.baseLose, (GameConfig.initXbase[0] - 1) * GUIConfig.blockSize, (GameConfig
              .initYbase[0] - 1) * GUIConfig.blockSize, GUIConfig.baseSize, GUIConfig.baseSize, this);
    else
      g.drawImage(this.base, (GameConfig.initXbase[0] - 1) * GUIConfig.blockSize, (GameConfig
              .initYbase[0] - 1) * GUIConfig.blockSize, GUIConfig.baseSize, GUIConfig.baseSize, this);
    for ( Tank tank: this.gameInfo.tanks ) {
      if (tank.isSelf())
        g.drawImage(this.selfTank[tank.getID()][tank.getDirection().ordinal()], (tank.getX()-1)
                * GUIConfig.blockSize, (tank.getY()-1)*GUIConfig.blockSize, GUIConfig
                .tankSize, GUIConfig.tankSize, this);
      else
        g.drawImage(this.enemyTank[tank.getID()][tank.getDirection().ordinal()], (tank.getX()-1)
                * GUIConfig.blockSize, (tank.getY()-1)*GUIConfig.blockSize, GUIConfig
                .tankSize, GUIConfig.tankSize, this);
    }
    for ( Shot shot: this.gameInfo.shots ) {
      g.setColor(Color.YELLOW);
      g.fillOval(shot.getX() * GUIConfig.blockSize - GUIConfig.shotSize/2, shot.getY() *
                      GUIConfig.blockSize - GUIConfig.shotSize/2, GUIConfig.shotSize, GUIConfig
              .shotSize);
    }
    for (int y = 1; y < GameConfig.boardInfoSize-1; y++)
      for (int x = 1; x < GameConfig.boardInfoSize - 1; x++)
        if (this.gameInfo.grassInfo[y][x] > 0)
          g.drawImage(this.grass, (x - 1) * GUIConfig.blockSize, (y - 1) * GUIConfig.blockSize,
                  GUIConfig.blockSize, GUIConfig.blockSize, this);
  }
}
