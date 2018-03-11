package battleCity.gameGUI;

import battleCity.gameControl.*;
import battleCity.gameControl.state.*;
import battleCity.gameGUI.element.*;
import battleCity.gameNetwork.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Created by Xia on 2017/5/26.
 * Game Main GUI
 */
public class GameGUI extends JFrame {
  private StartupGUI startupGUI;
  private RoomGUI roomGUI;
  private GameServer gameServer;
  private GameClient gameClient;
  private GameInfo gameInfo;
  public ImageButton pauseBtn, restartBtn;
  private boolean isRemote, isServer;

  GameGUI(StartupGUI startupGUI) {
    super(GUIConfig.gameTitle);
    this.isRemote = false; this.isServer = true; this.startupGUI = startupGUI;

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
    this.setPreferredSize(new Dimension(GUIConfig.gameGuiWidth, GUIConfig.gameGuiHeight));
    this.pack(); this.setResizable(false);

    GameServerController gameServerController = new GameServerController(this);
    SelfTank[] player = new SelfTank[2];
    for (int i = 0; i < 2; i++) {
      player[i] = new SelfTank(i, GameConfig.initXplayer[i], GameConfig.initYplayer[i]);
      gameServerController.registerTank(player[i]);
    }
    this.gameInfo = gameServerController.gameInfo;

    BoardPanel boardPanel = new BoardPanel(this.gameInfo);
    boardPanel.addKeyListener(new GameKeyListener(player[0], player[1]));
    this.pauseBtn = new ImageButton("/images/info/pauseBtn.png", "/images/info/pressedPauseBtn" +
            ".png", "/images/info/pressedPauseBtn.png", "/images/info/disabledPauseBtn.png");
    this.pauseBtn.setPreferredSize(new Dimension(GUIConfig.infoButtonWidth, GUIConfig.infoButtonHeight));
    this.pauseBtn.addActionListener(new GameButtonListener());
    this.pauseBtn.setActionCommand("Pause");
    this.restartBtn = new ImageButton("/images/info/restartBtn.png",
            "/images/info/pressedRestartBtn.png", "/images/info/pressedRestartBtn.png",
            "/images/info/disabledRestartBtn.png");
    this.restartBtn.setPreferredSize(new Dimension(GUIConfig.infoButtonWidth, GUIConfig
            .infoButtonHeight));
    this.restartBtn.addActionListener(new GameButtonListener());
    this.restartBtn.setActionCommand("Restart");
    this.restartBtn.setEnabled(false);
    GameInfoPanel gameInfoPanel = new GameInfoPanel(this.gameInfo, this.pauseBtn, this.restartBtn);
    this.add(boardPanel); this.add(gameInfoPanel);

    Dimension actualSize = this.getContentPane().getSize();
    int extraHeight = GUIConfig.gameGuiHeight - actualSize.height;
    this.setPreferredSize(new Dimension(GUIConfig.gameGuiWidth, GUIConfig.gameGuiHeight + extraHeight));
    this.pack(); this.setLocationRelativeTo(null);

    gameServerController.start();
    this.waitForOver();
  }

  GameGUI(StartupGUI startupGUI, RoomGUI roomGUI, GameClient gameClient, GameInfo gameInfo) {
    super(GUIConfig.gameClientTitle);
    this.isRemote = true; this.isServer = false;
    this.startupGUI = startupGUI; this.roomGUI = roomGUI;
    this.gameClient = gameClient; this.gameInfo = gameInfo;

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
    this.setPreferredSize(new Dimension(GUIConfig.gameGuiWidth, GUIConfig.gameGuiHeight));
    this.pack(); this.setResizable(false);

    GameClientController gameClientController = new GameClientController(this, this.gameInfo);

    BoardPanel boardPanel = new BoardPanel(this.gameInfo);
    boardPanel.addKeyListener(new GameKeyListener(this.gameClient));
    this.pauseBtn = new ImageButton("/images/info/pauseBtn.png", "/images/info/pressedPauseBtn" +
            ".png", "/images/info/pressedPauseBtn.png", "/images/info/disabledPauseBtn.png");
    this.pauseBtn.setPreferredSize(new Dimension(GUIConfig.infoButtonWidth, GUIConfig.infoButtonHeight));
    this.pauseBtn.addActionListener(new GameButtonListener());
    this.pauseBtn.setActionCommand("Pause");
    this.restartBtn = new ImageButton("/images/info/restartBtn.png",
            "/images/info/pressedRestartBtn.png", "/images/info/pressedRestartBtn.png",
            "/images/info/disabledRestartBtn.png");
    this.restartBtn.setPreferredSize(new Dimension(GUIConfig.infoButtonWidth, GUIConfig
            .infoButtonHeight));
    this.restartBtn.addActionListener(new GameButtonListener());
    this.restartBtn.setActionCommand("Restart");
    this.restartBtn.setEnabled(false);
    GameInfoPanel gameInfoPanel = new GameInfoPanel(this.gameInfo, this.pauseBtn, this.restartBtn);
    this.add(boardPanel); this.add(gameInfoPanel);
    Dimension actualSize = this.getContentPane().getSize();
    int extraHeight = GUIConfig.gameGuiHeight - actualSize.height;
    this.setPreferredSize(new Dimension(GUIConfig.gameGuiWidth, GUIConfig.gameGuiHeight + extraHeight));
    this.pack(); this.setLocationRelativeTo(null);

    this.gameClient.setInsideGameGUI(this, this.gameInfo);
    gameClientController.start();
    this.waitForOver();
    this.gameClient.receiveFromServer();
  }

  GameGUI(StartupGUI startupGUI, RoomGUI roomGUI, GameServer gameServer) {
    super(GUIConfig.gameServerTitle);
    this.isRemote = true; this.isServer = true;
    this.startupGUI = startupGUI; this.roomGUI = roomGUI; this.gameServer = gameServer;

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
    this.setPreferredSize(new Dimension(GUIConfig.gameGuiWidth, GUIConfig.gameGuiHeight));
    this.pack(); this.setResizable(false);

    GameServerController gameServerController = new GameServerController(this);
    SelfTank[] player = new SelfTank[2];
    for (int i = 0; i < 2; i++) {
      player[i] = new SelfTank(i, GameConfig.initXplayer[i], GameConfig.initYplayer[i]);
      gameServerController.registerTank(player[i]);
    }
    this.gameInfo = gameServerController.gameInfo;

    BoardPanel boardPanel = new BoardPanel(this.gameInfo);
    boardPanel.addKeyListener(new GameKeyListener(player[0]));
    this.pauseBtn = new ImageButton("/images/info/pauseBtn.png", "/images/info/rollPauseBtn" +
            ".png", "/images/info/pressedPauseBtn.png", "/images/info/disabledPauseBtn.png");
    this.pauseBtn.setPreferredSize(new Dimension(GUIConfig.infoButtonWidth, GUIConfig.infoButtonHeight));
    this.pauseBtn.addActionListener(new GameButtonListener());
    this.pauseBtn.setActionCommand("Pause");
    this.restartBtn = new ImageButton("/images/info/restartBtn.png",
            "/images/info/rollRestartBtn.png", "/images/info/pressedRestartBtn.png",
            "/images/info/disabledRestartBtn.png");
    this.restartBtn.setPreferredSize(new Dimension(GUIConfig.infoButtonWidth, GUIConfig
            .infoButtonHeight));
    this.restartBtn.addActionListener(new GameButtonListener());
    this.restartBtn.setActionCommand("Restart");
    this.restartBtn.setEnabled(false);
    GameInfoPanel gameInfoPanel = new GameInfoPanel(this.gameInfo, this.pauseBtn, this.restartBtn);
    this.add(boardPanel); this.add(gameInfoPanel);
    Dimension actualSize = this.getContentPane().getSize();
    int extraHeight = GUIConfig.gameGuiHeight - actualSize.height;
    this.setPreferredSize(new Dimension(GUIConfig.gameGuiWidth, GUIConfig.gameGuiHeight + extraHeight));
    this.pack(); this.setLocationRelativeTo(null);

    this.gameServer.setInsideGameGUI(this, this.gameInfo, player[1]);
    this.gameServer.sendToClient();
    gameServerController.start();
    this.waitForOver();
    this.gameServer.receiveFromClient();
  }

  private boolean isOver() {
    if (GameGUI.this.isRemote)
      return GameGUI.this.gameInfo.state == GameState.ERROR || GameGUI.this.gameInfo.state ==
              GameState.WIN_OVER || GameGUI.this.gameInfo.state == GameState.LOSE_OVER;
    else
      return GameGUI.this.gameInfo.state == GameState.ERROR || GameGUI.this.gameInfo.state ==
              GameState.WIN || GameGUI.this.gameInfo.state == GameState.LOSE;
  }

  private void waitForOver() {
    Thread t = new Thread(() -> {
      synchronized (GameGUI.this.gameInfo.stateLock) {
        while (!GameGUI.this.isOver()) {
          try {
            GameGUI.this.gameInfo.stateLock.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
            GameGUI.this.gameInfo.state = GameState.ERROR;
          }
        }
        int score;
        if (isRemote) {
          if (isServer)
            score = this.gameInfo.numNormalEnemyKilled[0] + 3 * this.gameInfo
                    .numStrongEnemyKilled[0];
          else
            score = this.gameInfo.numNormalEnemyKilled[1] + 3 * this.gameInfo
                    .numStrongEnemyKilled[1];
        } else {
          int score_1 = this.gameInfo.numNormalEnemyKilled[0] + 3 * this.gameInfo
                  .numStrongEnemyKilled[0];
          int score_2 = this.gameInfo.numNormalEnemyKilled[1] + 3 * this.gameInfo
                  .numStrongEnemyKilled[1];
          score = (score_1 > score_2) ? score_1 : score_2;
        }
        switch (GameGUI.this.gameInfo.state) {
          case WIN:
          case WIN_OVER:
            SoundEffect.WIN.play();
            JOptionPane.showMessageDialog(GameGUI.this, "Congratulations! You are the WINNER!",
                      "WINNING", JOptionPane.INFORMATION_MESSAGE);
            if (isRemote)
              goToResult(true, score);
            else
              goToResult(false, score);
            break;
          case LOSE:
          case LOSE_OVER:
            SoundEffect.LOSE.play();
            JOptionPane.showMessageDialog(GameGUI.this, "Sorry, you LOSE!",
                      "LOSING", JOptionPane.INFORMATION_MESSAGE);
            if (isRemote)
              goToResult(true, score);
            else
              goToResult(false, score);
            break;
          default:
            if (GameGUI.this.isRemote) {
              if (GameGUI.this.isServer) {
                try {
                  GameGUI.this.gameServer.clientSocket.close();
                } catch (IOException e) {
                  e.printStackTrace();
                }
                GameGUI.this.gameServer.close();
              } else
                GameGUI.this.gameClient.close();
            }
            JOptionPane.showMessageDialog(GameGUI.this, "Something went wrong", "Warning", JOptionPane
                    .WARNING_MESSAGE);
            GameGUI.this.goBackStartUp();
        }
      }
    });
    t.start();
  }

  class GameButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      String cmd = e.getActionCommand();
      switch (cmd) {
        case "Pause":
          GameGUI.this.pauseBtn.setEnabled(false);
          GameGUI.this.restartBtn.setEnabled(true);
          if (!GameGUI.this.isServer) {
            try {
              GameGUI.this.gameClient.clientSocket.sendMessage(GameProtocol.GameStatePacket(GameState
                      .PAUSE));
            } catch (IOException e1) {
              e1.printStackTrace();
              synchronized (GameGUI.this.gameInfo.stateLock) {
                GameGUI.this.gameInfo.state = GameState.ERROR;
                GameGUI.this.gameInfo.stateLock.notifyAll();
              }
            }
          } else {
            synchronized (GameGUI.this.gameInfo.stateLock) {
              if (GameGUI.this.gameInfo.state == GameState.PLAYING)
                GameGUI.this.gameInfo.state = GameState.PAUSE;
              GameGUI.this.gameInfo.stateLock.notifyAll();
            }
          }
          break;
        case "Restart":
          GameGUI.this.pauseBtn.setEnabled(true);
          GameGUI.this.restartBtn.setEnabled(false);
          if (!GameGUI.this.isServer) {
            try {
              GameGUI.this.gameClient.clientSocket.sendMessage(GameProtocol.GameStatePacket(GameState
                      .PLAYING));
            } catch (IOException e1) {
              e1.printStackTrace();
              synchronized (GameGUI.this.gameInfo.stateLock) {
                GameGUI.this.gameInfo.state = GameState.ERROR;
                GameGUI.this.gameInfo.stateLock.notifyAll();
              }
            }
          } else {
            synchronized (GameGUI.this.gameInfo.stateLock) {
              if (GameGUI.this.gameInfo.state == GameState.PAUSE)
                GameGUI.this.gameInfo.state = GameState.PLAYING;
              GameGUI.this.gameInfo.stateLock.notifyAll();
            }
          }
          break;
      }
    }
  }

  void resetRoomGUI() {
    if (this.isServer) {
      this.gameServer.state = ServerState.CONNECTED;
      this.roomGUI.waitingForClient(true);
    } else {
      this.gameClient.state = ClientState.CONNECTED;
      this.roomGUI.showPlayer(false, true, false);
      this.roomGUI.showPlayer(false, false, false);
      this.roomGUI.waitingForServer();
    }
  }

  private void goToResult(boolean isBackToRoom, int score) {
    this.setVisible(false);
    SwingUtilities.invokeLater(() -> {
      RecordGUI recordGUI = new RecordGUI(GameGUI.this, isBackToRoom, score);
      recordGUI.pack();
      recordGUI.setVisible(true);
    });
  }

  void goBackStartUp() {
    this.dispose();
    this.startupGUI.setButtonsEnabled(true);
    this.startupGUI.setVisible(true);
  }

  void goBackRoom() {
    this.dispose();
    this.roomGUI.setButtonsEnabled(true);
    this.roomGUI.setVisible(true);
    SoundEffect.ROOM.play();
  }
}
