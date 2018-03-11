package battleCity.gameGUI;

import battleCity.gameControl.*;
import battleCity.gameGUI.element.*;
import battleCity.gameNetwork.*;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;

/**
 * Created by Xia on 2017/5/26.
 * GUI class for room
 */
class RoomGUI extends JFrame {
  private StartupGUI startupGUI;
  private GameServer gameServer;
  private GameClient gameClient;
  private ImageButton prepareBtn, exitBtn;
  private JLabel serverInfo, clientInfo;

  RoomGUI(StartupGUI startupGUI, GameServer gameServer) {
    this.startupGUI = startupGUI;
    this.gameServer = gameServer;
    this.setRoomGUI();
    this.prepareBtn.addActionListener(new serverBtnClickedListener());
    this.prepareBtn.setActionCommand("Prepared");
    this.exitBtn.addActionListener(new serverBtnClickedListener());
    this.exitBtn.setActionCommand("Exit");
    this.showPlayer(true, true, false);
    this.setTitle(GUIConfig.roomServerTitle);

    SoundEffect.ROOM.play();
  }

  RoomGUI(StartupGUI startupGUI, GameClient gameClient) {
    this.startupGUI = startupGUI;
    this.gameClient = gameClient;
    this.setRoomGUI();
    this.prepareBtn.addActionListener(new clientBtnClickedListener());
    this.prepareBtn.setActionCommand("Prepared");
    this.exitBtn.addActionListener(new clientBtnClickedListener());
    this.exitBtn.setActionCommand("Exit");
    this.showPlayer(false, true, false);
    this.showPlayer(false, false, false);
    this.setTitle(GUIConfig.roomClientTitle);

    SoundEffect.ROOM.play();
  }

  private void setRoomGUI() {
    this.exitBtn = new ImageButton("/images/room/exitBtn.png","/images/room/rollExitBtn" +
            ".png", "/images/room/pressedExitBtn.png","/images/room/disabledExitBtn.png");
    this.prepareBtn = new ImageButton("/images/room/prepareBtn.png",
            "/images/room/rollPrepareBtn.png", "/images/room/pressedPrepareBtn.png",
            "/images/room/disabledPrepareBtn.png");
    this.exitBtn.setPreferredSize(new Dimension(GUIConfig.roomButtonWidth, GUIConfig
            .roomButtonHeight));
    this.prepareBtn.setPreferredSize(new Dimension(GUIConfig.roomButtonWidth, GUIConfig
            .roomButtonHeight));
    ImageLabel serverIcon = new ImageLabel("/images/room/server.png");
    ImageLabel clientIcon = new ImageLabel("/images/room/client.png");
    serverIcon.setPreferredSize(new Dimension(GUIConfig.roomInfoWidth, GUIConfig.roomIconSize));
    clientIcon.setPreferredSize(new Dimension(GUIConfig.roomInfoWidth, GUIConfig.roomIconSize));
    this.serverInfo = new JLabel("<html><body><br>?<br><br></body></html>", JLabel.CENTER);
    Font f = new Font(this.serverInfo.getFont().getFontName(), this.serverInfo.getFont().getStyle
            (), 14);
    this.serverInfo.setForeground(Color.WHITE);this.serverInfo.setFont(f);
    this.clientInfo = new JLabel("<html><body><br>?<br><br></body></html>", JLabel.CENTER);
    this.clientInfo.setForeground(Color.WHITE); this.clientInfo.setFont(f);

    JPanel scPanel = new JPanel();
    scPanel.setPreferredSize(new Dimension(GUIConfig.roomPanelWidth, GUIConfig.roomPanelHeight));
    scPanel.setMaximumSize(scPanel.getPreferredSize());
    scPanel.setLayout(new GridBagLayout());
    scPanel.add(serverIcon, new MyGridBagConstraints(0, 0, 1, 2).setFill(GridBagConstraints
            .BOTH).setAnchor(GridBagConstraints.CENTER));
    scPanel.add(clientIcon, new MyGridBagConstraints(1, 0, 1,2).setFill(GridBagConstraints
            .BOTH).setAnchor(GridBagConstraints.CENTER));
    scPanel.add(this.serverInfo, new MyGridBagConstraints(0, 2,1,3).setFill
            (GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
    scPanel.add(this.clientInfo, new MyGridBagConstraints(1, 2,1,3).setFill
            (GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
    scPanel.setOpaque(false);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setPreferredSize(new Dimension(GUIConfig.roomButtonWidth*2+20, GUIConfig
            .roomButtonHeight));
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.add(this.prepareBtn); buttonPanel.add(Box.createHorizontalStrut(20));
    buttonPanel.add(this.exitBtn);
    buttonPanel.setOpaque(false);
    try {
      BufferedImage backgroundImage = ImageIO.read(getClass().getResource
              ("/images/room/background.png"));
      this.setLayout(new BorderLayout());
      this.setContentPane(new JLabel(new ImageIcon(backgroundImage)));
      this.setPreferredSize(new Dimension(GUIConfig.roomGuiWidth, GUIConfig.roomGuiHeight));
      this.setResizable(false);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setLocationRelativeTo(null);
      this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
      this.add(Box.createVerticalStrut(40));
      this.add(scPanel); this.add(buttonPanel);
      this.pack();
      Dimension actualSize = this.getContentPane().getSize();
      int extraHeight = GUIConfig.roomGuiHeight - actualSize.height;
      this.setPreferredSize(new Dimension(GUIConfig.roomGuiWidth, GUIConfig.roomGuiHeight +
              extraHeight));
      this.pack(); this.setLocationRelativeTo(null);
    } catch ( IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  void showPlayer(boolean isServer, boolean isServerPlayer, boolean isPrepared) {
    String prepared = isPrepared ? "Prepared" : "Not Prepared";
    if (isServer) {
      if (isServerPlayer) {
        String host = this.gameServer.getHost(false);
        int port = this.gameServer.getPort(false);
        this.serverInfo.setText("<html><body>" + host + "<br>" + port + "<br>" + prepared +
                "</body></html>");
      } else {
        String host = this.gameServer.getHost(true);
        int port = this.gameServer.getPort(true);
        this.clientInfo.setText("<html><body>" + host + "<br>" + port + "<br>" + prepared +
                "</body></html>");
      }
    } else {
      if (isServerPlayer) {
        String host = this.gameClient.getHost(true);
        int port = this.gameClient.getPort(true);
        this.serverInfo.setText("<html><body>" + host + "<br>" + port + "<br>" + prepared +
                "</body></html>");
      } else {
        String host = this.gameClient.getHost(false);
        int port = this.gameClient.getPort(false);
        this.clientInfo.setText("<html><body>" + host + "<br>" + port + "<br>" + prepared +
                "</body></html>");
      }
    }
  }

  void setButtonsEnabled(boolean enabled) {
    this.prepareBtn.setEnabled(enabled);
    this.exitBtn.setEnabled(enabled);
  }

  void waitingForClient(boolean isConnected) {
    if (isConnected) {
      Thread t = new Thread(() -> RoomGUI.this.gameServer.waitForClientPrepared());
      t.start();
    } else
      this.gameServer.waitForClient();

    Thread tt = new Thread(() -> {
      while(RoomGUI.this.gameServer.state != ServerState.EXIT && RoomGUI.this.gameServer.state
      != ServerState.ERROR && RoomGUI.this.gameServer.state != ServerState.PREPARED) {
        synchronized (RoomGUI.this.gameServer.stateLock) {
          while ((RoomGUI.this.gameServer.state != ServerState.CONNECTED) && (RoomGUI.this.gameServer.state !=
                  ServerState.SERVER_PREPARED) && (RoomGUI.this.gameServer.state != ServerState.EXIT) &&
                  (RoomGUI.this.gameServer.state != ServerState.ERROR)) {
            try {
              RoomGUI.this.gameServer.stateLock.wait();
            } catch (InterruptedException e) {
              e.printStackTrace();
              RoomGUI.this.gameServer.state = ServerState.ERROR;
            }
          }
          switch (RoomGUI.this.gameServer.state) {
            case CONNECTED:
              RoomGUI.this.showPlayer(true, true, false);
              RoomGUI.this.showPlayer(true, false, false);
              break;
            case SERVER_PREPARED:
              RoomGUI.this.showPlayer(true, false, false);
              RoomGUI.this.showPlayer(true, true, true);
              break;
          }

          while ((RoomGUI.this.gameServer.state != ServerState.PREPARED) && (RoomGUI.this.gameServer.state != ServerState.EXIT) &&
                  (RoomGUI.this.gameServer.state != ServerState.ERROR) && (RoomGUI.this
                  .gameServer.state != ServerState.WAITING) && (RoomGUI.this.gameServer.state !=
                  ServerState.CLIENT_PREPARED)) {
            try {
              RoomGUI.this.gameServer.stateLock.wait();
            } catch (InterruptedException e) {
              e.printStackTrace();
              RoomGUI.this.gameServer.state = ServerState.ERROR;
            }
          }
          switch (RoomGUI.this.gameServer.state) {
            case WAITING:
              RoomGUI.this.showPlayer(true, true, false);
              RoomGUI.this.clientInfo.setText("<html><body><br>?<br><br></body></html>");
              RoomGUI.this.setButtonsEnabled(true);
              break;
            case CLIENT_PREPARED:
              RoomGUI.this.showPlayer(true, true, false);
              RoomGUI.this.showPlayer(true, false, true);
              RoomGUI.this.setButtonsEnabled(true);
              while (RoomGUI.this.gameServer.state == ServerState.CLIENT_PREPARED) {
                try {
                  RoomGUI.this.gameServer.stateLock.wait();
                } catch (InterruptedException e) {
                  e.printStackTrace();
                  RoomGUI.this.gameServer.state = ServerState.ERROR;
                }
              }
              break;
          }
          switch (RoomGUI.this.gameServer.state) {
            case PREPARED:
              RoomGUI.this.setVisible(false);
              SwingUtilities.invokeLater(() -> {
                GameGUI gameGUI = new GameGUI(RoomGUI.this.startupGUI, RoomGUI.this, RoomGUI.this
                        .gameServer);
                gameGUI.pack();
                gameGUI.setVisible(true);
              });
              break;
            case EXIT:
              RoomGUI.this.goBackStartUp();
              break;
            case ERROR:
              JOptionPane.showMessageDialog(RoomGUI.this, "Something went wrong", "Warning", JOptionPane
                      .WARNING_MESSAGE);
              RoomGUI.this.goBackStartUp();
              break;
          }
        }
      }
    });
    tt.start();
  }

  void waitingForServer() {
    this.gameClient.waitingForServerPrepared();

    Thread t = new Thread(() -> {
      synchronized (RoomGUI.this.gameClient.stateLock) {
        while (RoomGUI.this.gameClient.state == ClientState.CONNECTED) {
          try {
            RoomGUI.this.gameClient.stateLock.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
            RoomGUI.this.gameClient.state = ClientState.ERROR;
          }
        }
        switch (RoomGUI.this.gameClient.state) {
          case CLIENT_PREPARED:
            RoomGUI.this.showPlayer(false, true, false);
            RoomGUI.this.showPlayer(false, false, true);
            break;
          case SERVER_PREPARED:
            RoomGUI.this.showPlayer(false, false, false);
            RoomGUI.this.showPlayer(false, true, true);
            break;
          case PREPARED:
            RoomGUI.this.showPlayer(false, false, true);
            RoomGUI.this.showPlayer(false, true, true);
            break;
        }

        while (RoomGUI.this.gameClient.state != ClientState.PREPARED && RoomGUI.this.gameClient.state !=
                ClientState.EXIT && RoomGUI.this.gameClient.state != ClientState.ERROR) {
          try {
            RoomGUI.this.gameClient.stateLock.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
            RoomGUI.this.gameClient.state = ClientState.ERROR;
            RoomGUI.this.gameClient.stateLock.notifyAll();
          }
        }

        switch (RoomGUI.this.gameClient.state) {
          case PREPARED:
            try {
              GameInfo gameInfo = this.gameClient.clientSocket.receiveGameInfo();
              RoomGUI.this.setVisible(false);
              SwingUtilities.invokeLater(() -> {
                GameGUI gameGUI = new GameGUI(RoomGUI.this.startupGUI, RoomGUI.this, RoomGUI.this
                        .gameClient, gameInfo);
                gameGUI.pack();
                gameGUI.setVisible(true);
              });
            } catch (Exception e) {
              e.printStackTrace();
              RoomGUI.this.gameClient.close();
              JOptionPane.showMessageDialog(RoomGUI.this, "Something went wrong", "Warning", JOptionPane
                      .WARNING_MESSAGE);
              RoomGUI.this.goBackStartUp();
            }
            break;
          case EXIT:
            RoomGUI.this.goBackStartUp();
            break;
          case ERROR:
            JOptionPane.showMessageDialog(RoomGUI.this, "Something went wrong", "Warning", JOptionPane
                    .WARNING_MESSAGE);
            RoomGUI.this.goBackStartUp();
            break;
        }
      }
    });
    t.start();
  }

  private void goBackStartUp() {
    this.dispose();
    this.startupGUI.setButtonsEnabled(true);
    this.startupGUI.setVisible(true);
  }

  class serverBtnClickedListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      String cmd = e.getActionCommand();
      RoomGUI.this.setButtonsEnabled(false);
      switch (cmd) {
        case "Prepared":
          synchronized (RoomGUI.this.gameServer.stateLock) {
            switch (RoomGUI.this.gameServer.state) {
              case WAITING:
                RoomGUI.this.gameServer.state = ServerState.WAITING_SERVER_PREPARED;
                RoomGUI.this.showPlayer(true, true, true);
                break;
              case CONNECTED:
                RoomGUI.this.gameServer.state = ServerState.SERVER_PREPARED;
                RoomGUI.this.showPlayer(true, true, true);
                try {
                  RoomGUI.this.gameServer.clientSocket.sendMessage(GameProtocol.PreparedPacket());
                } catch (Exception e1) {
                  e1.printStackTrace();
                  RoomGUI.this.gameServer.state = ServerState.ERROR;
                }
                break;
              case CLIENT_PREPARED:
                RoomGUI.this.gameServer.state = ServerState.PREPARED;
                RoomGUI.this.showPlayer(true, true, true);
                try {
                  RoomGUI.this.gameServer.clientSocket.sendMessage(GameProtocol.PreparedPacket());
                } catch (Exception e1) {
                  e1.printStackTrace();
                  RoomGUI.this.gameServer.state = ServerState.ERROR;
                }
                break;
              default:
                RoomGUI.this.gameServer.state = ServerState.ERROR;
                break;
            }
            RoomGUI.this.gameServer.stateLock.notifyAll();
          }
          break;
        case "Exit":
          synchronized (RoomGUI.this.gameServer.stateLock) {
            switch (RoomGUI.this.gameServer.state) {
              case WAITING:
              case CONNECTED:
              case CLIENT_PREPARED:
                RoomGUI.this.gameServer.state = ServerState.EXIT;
                break;
              default:
                RoomGUI.this.gameServer.state = ServerState.ERROR;
                break;
            }
            RoomGUI.this.gameServer.stateLock.notifyAll();
          }
          break;
      }
    }
  }

  class clientBtnClickedListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      String cmd = e.getActionCommand();
      RoomGUI.this.setButtonsEnabled(false);
      switch (cmd) {
        case "Prepared":
          synchronized (RoomGUI.this.gameClient.stateLock) {
            switch (RoomGUI.this.gameClient.state) {
              case CONNECTED:
                RoomGUI.this.gameClient.state = ClientState.CLIENT_PREPARED;
                RoomGUI.this.showPlayer(false, false, true);
                try {
                  RoomGUI.this.gameClient.clientSocket.sendMessage(GameProtocol.PreparedPacket());
                } catch (Exception e1) {
                  e1.printStackTrace();
                  RoomGUI.this.gameClient.state = ClientState.ERROR;
                }
                break;
              case SERVER_PREPARED:
                RoomGUI.this.gameClient.state = ClientState.PREPARED;
                RoomGUI.this.showPlayer(false, false, true);
                try {
                  RoomGUI.this.gameClient.clientSocket.sendMessage(GameProtocol.PreparedPacket());
                } catch (Exception e1) {
                  e1.printStackTrace();
                  RoomGUI.this.gameClient.state = ClientState.ERROR;
                }
                break;
              default:
                RoomGUI.this.gameClient.state = ClientState.ERROR;
                break;
            }
            RoomGUI.this.gameClient.stateLock.notifyAll();
          }
          break;
        case "Exit":
          synchronized (RoomGUI.this.gameClient.stateLock) {
            switch (RoomGUI.this.gameClient.state) {
              case CONNECTED:
              case SERVER_PREPARED:
                RoomGUI.this.gameClient.state = ClientState.EXIT;
                try {
                  RoomGUI.this.gameClient.clientSocket.sendMessage(GameProtocol.ExitPacket());
                } catch (Exception e1) {
                  RoomGUI.this.gameClient.state = ClientState.ERROR;
                }
                break;
              default:
                RoomGUI.this.gameClient.state = ClientState.ERROR;
                break;
            }
            RoomGUI.this.gameClient.stateLock.notifyAll();
          }
          break;
      }
    }
  }
}
