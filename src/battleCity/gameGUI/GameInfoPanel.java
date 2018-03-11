package battleCity.gameGUI;

import battleCity.gameControl.*;
import battleCity.gameGUI.element.*;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

/**
 * Created by Xia on 2017/5/26.
 * Game Info Panel
 */
public class GameInfoPanel extends JPanel {
  private GameInfo gameInfo;

  private BufferedImage background;
  private JLabel[] enemyTank, nLife;
  private JLabel[] scores;

  GameInfoPanel(GameInfo gameInfo, JButton pauseBtn, JButton restartBtn) {
    this.gameInfo = gameInfo;
    this.setPreferredSize(new Dimension(GUIConfig.infoPanelWidth, GUIConfig.infoPanelHeight));
    this.setFocusable(true);
    try {
      this.background = ImageIO.read(getClass().getResource("/images/info/background.png"));

      ImageLabel[] selfTank = new ImageLabel[2];
      ImageLabel[] player = new ImageLabel[2];
      this.nLife = new JLabel[2]; this.scores = new JLabel[2];
      this.enemyTank = new ImageLabel[GameConfig.numEnemy];
      for (int i = 0; i < 2; i++) {
        selfTank[i] = new ImageLabel("/images/info/selfTank_" + i + ".png");
        selfTank[i].setPreferredSize(new Dimension(GUIConfig.infoSelfTankSize, GUIConfig
                .infoSelfTankSize));
        player[i] = new ImageLabel("/images/info/player_" + i + ".png");
        player[i].setPreferredSize(new Dimension(GUIConfig.infoPlayerLabelWidth, GUIConfig
                .infoPlayerLabelHeight));
        this.nLife[i] = new JLabel();
        this.nLife[i].setText("4");
        this.nLife[i].setPreferredSize(new Dimension(GUIConfig.infoLifeScoreWidth, GUIConfig
                .infoLifeScoreHeight));
        this.scores[i] = new JLabel();
        this.scores[i].setText("0");
        this.scores[i].setPreferredSize(new Dimension(GUIConfig.infoLifeScoreWidth, GUIConfig
                .infoLifeScoreHeight));
      }
      for (int i = 0; i < GameConfig.numEnemy; i++) {
        this.enemyTank[i] = new ImageLabel("/images/info/enemyTank.png");
        this.enemyTank[i].setPreferredSize(new Dimension(GUIConfig.infoEnemyTankSize, GUIConfig
                .infoEnemyTankSize));
        this.enemyTank[i].setHorizontalAlignment(JLabel.CENTER);
        this.enemyTank[i].setVerticalAlignment(JLabel.CENTER);
      }

      ImageLabel enemyLabel = new ImageLabel("/images/info/enemy.png");
      enemyLabel.setPreferredSize(new Dimension(GUIConfig.infoEnemyLabelWidth, GUIConfig.infoEnemyLabelHeight));
      JPanel enemyTankPanel = new JPanel();
      enemyTankPanel.setLayout(new GridLayout(5 ,5));
      for (int i = 0; i < GameConfig.numEnemy; i++) {
        enemyTankPanel.add(this.enemyTank[i]);
      }
      enemyTankPanel.setPreferredSize(new Dimension(GUIConfig.infoEnemyTankPanelWidth, GUIConfig
              .infoEnemyTankPanelHeight));
      enemyTankPanel.setOpaque(false);
      enemyTankPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
      enemyTankPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
      JPanel enemyPanel = new JPanel();
      enemyPanel.setLayout(new BoxLayout(enemyPanel, BoxLayout.Y_AXIS));
      enemyPanel.add(enemyLabel); enemyPanel.add(enemyTankPanel);
      enemyPanel.setPreferredSize(new Dimension(GUIConfig.infoEnemyPanelWidth, GUIConfig
              .infoEnemyPanelHeight));
      enemyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
      enemyPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
      enemyPanel.setOpaque(false);

      JPanel[] playerInfoPanel = new JPanel[2];
      for (int i = 0; i < 2; i++) {
        playerInfoPanel[i] = new JPanel();
        playerInfoPanel[i].setLayout(new GridBagLayout());
        playerInfoPanel[i].add(player[i], new MyGridBagConstraints(0,0, 3, 1).setAnchor
                (GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
        playerInfoPanel[i].add(selfTank[i], new MyGridBagConstraints(0, 1, 1, 1).setAnchor(
                GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
        JLabel life = new JLabel("Life");
        Font f = new Font(life.getFont().getFontName(), life.getFont().getStyle(), 14);
        Color c = new Color(125, 70, 65);
        life.setFont(f); life.setForeground(c);
        life.setPreferredSize(new Dimension(GUIConfig.infoLifeScoreWidth, GUIConfig.infoLifeScoreHeight));
        playerInfoPanel[i].add(life, new MyGridBagConstraints(1, 1, 1, 1).setAnchor(
                GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH));
        playerInfoPanel[i].add(this.nLife[i], new MyGridBagConstraints(2, 1, 1, 1).setAnchor(
                GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH));
        JLabel score = new JLabel("Score"); score.setFont(f); score.setForeground(c);
        score.setPreferredSize(new Dimension(GUIConfig.infoLifeScoreWidth, GUIConfig.infoLifeScoreHeight));
        playerInfoPanel[i].add(score, new MyGridBagConstraints(1, 2, 1, 1).setAnchor(
                GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH));
        playerInfoPanel[i].add(this.scores[i], new MyGridBagConstraints(2, 2, 1, 1).setAnchor(
                GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH));
        playerInfoPanel[i].setPreferredSize(new Dimension(GUIConfig.infoPlayerPanelWidth, GUIConfig
                .infoPlayerPanelHeight));
        playerInfoPanel[i].setOpaque(false);
        playerInfoPanel[i].setAlignmentX(Component.CENTER_ALIGNMENT);
        playerInfoPanel[i].setAlignmentY(Component.CENTER_ALIGNMENT);
      }

      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
      buttonPanel.add(pauseBtn); buttonPanel.add(Box.createVerticalStrut(10));
      buttonPanel.add(restartBtn);
      buttonPanel.setPreferredSize(new Dimension(GUIConfig.infoButtonPanelWidth, GUIConfig
              .infoButtonPanelHeight));
      buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
      buttonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
      buttonPanel.setOpaque(false);

      this.setAlignmentX(Component.CENTER_ALIGNMENT);
      this.setAlignmentY(Component.CENTER_ALIGNMENT);
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      this.add(enemyPanel); this.add(playerInfoPanel[0]); this.add(playerInfoPanel[1]);
      this.add(Box.createVerticalStrut(20));
      this.add(buttonPanel);

    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(this.background, 0, 0, GUIConfig.infoPanelWidth, GUIConfig.infoPanelHeight,
            null);
    for (int i = this.gameInfo.numEnemyLeft; i < GameConfig.numEnemy; i++)
      this.enemyTank[i].setVisible(false);
    this.nLife[0].setText("0");
    this.nLife[0].setPreferredSize(new Dimension(GUIConfig.infoLifeScoreWidth,
            GUIConfig.infoLifeScoreHeight));
    this.nLife[1].setText("0");
    this.nLife[1].setPreferredSize(new Dimension(GUIConfig.infoLifeScoreWidth,
            GUIConfig.infoLifeScoreHeight));
    for ( Tank tank: this.gameInfo.tanks )
      if (tank.isSelf()) {
        int id = tank.getID();
        this.nLife[id].setText("" + tank.getNumLife());
        this.nLife[id].setPreferredSize(new Dimension(GUIConfig.infoLifeScoreWidth,
                GUIConfig.infoLifeScoreHeight));
        this.scores[id].setText("" + ((this.gameInfo.numNormalEnemyKilled[id] + 3 * this.gameInfo
                .numStrongEnemyKilled[id])));
        this.scores[id].setPreferredSize(new Dimension(GUIConfig.infoLifeScoreWidth,
                GUIConfig.infoLifeScoreHeight));
      }
  }
}
