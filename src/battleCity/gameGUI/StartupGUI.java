package battleCity.gameGUI;

import battleCity.gameGUI.element.*;
import battleCity.gameNetwork.*;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Xia on 2017/5/26.
 * GUI class for startup
 */
public class StartupGUI extends JFrame {
  private JButton localBtn, createRoomBtn, enterRoomBtn, exitBtn;

  public StartupGUI() {
    super(GUIConfig.startupTitle);
    try {
      BufferedImage backgroundImage = ImageIO.read(getClass().getResource
              ("/images/startup/background.png"));
      this.setLayout(new BorderLayout());
      this.setContentPane(new JLabel(new ImageIcon(backgroundImage)));
      this.setPreferredSize(new Dimension(GUIConfig.startupGuiWidth, GUIConfig.startupGuiHeight));
      this.setResizable(false);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setLocationRelativeTo(null);
      this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

      JLabel titleLable = new ImageLabel("/images/startup/title.png");
      titleLable.setPreferredSize(new Dimension(GUIConfig.startupGuiWidth, GUIConfig
              .startupTitleHeight));

      this.localBtn = new ImageButton("/images/startup/localBtn.png",
              "/images/startup/rollLocalBtn.png",
              "/images/startup/pressedLocalBtn.png", "/images/startup/disabledLocalBtn.png");
      this.createRoomBtn = new ImageButton("/images/startup/createRoomBtn.png",
              "/images/startup/rollCreateRoomBtn.png",
              "/images/startup/pressedCreateRoomBtn.png",
              "/images/startup/disabledCreateRoomBtn.png");
      this.enterRoomBtn = new ImageButton("/images/startup/enterRoomBtn.png",
              "/images/startup/rollEnterRoomBtn.png",
              "/images/startup/pressedEnterRoomBtn.png",
              "/images/startup/disabledEnterRoomBtn.png");
      this.exitBtn = new ImageButton("/images/startup/exitBtn.png","/images/startup/rollExitBtn.png",
              "/images/startup/pressedExitBtn.png","/images/startup/disabledExitBtn.png");
      this.localBtn.setPreferredSize(new Dimension(GUIConfig.startupButtonWidth, GUIConfig.startupButtonHeight));
      this.createRoomBtn.setPreferredSize(new Dimension(GUIConfig.startupButtonWidth, GUIConfig.startupButtonHeight));
      this.enterRoomBtn.setPreferredSize(new Dimension(GUIConfig.startupButtonWidth, GUIConfig.startupButtonHeight));
      this.exitBtn.setPreferredSize(new Dimension(GUIConfig.startupButtonWidth, GUIConfig.startupButtonHeight));

      this.localBtn.addActionListener(new StartupGUI.btnClickedListener());
      this.localBtn.setActionCommand("Local");
      this.createRoomBtn.addActionListener(new StartupGUI.btnClickedListener());
      this.createRoomBtn.setActionCommand("CreateRoom");
      this.enterRoomBtn.addActionListener(new StartupGUI.btnClickedListener());
      this.enterRoomBtn.setActionCommand("EnterRoom");
      this.exitBtn.addActionListener(new StartupGUI.btnClickedListener());
      this.exitBtn.setActionCommand("Exit");

      ImageLabel titleLabel = new ImageLabel("/images/startup/title.png");
      titleLabel.setPreferredSize(new Dimension(GUIConfig.startupGuiWidth, GUIConfig.startupTitleHeight));

      this.add(titleLabel);
      this.add(this.localBtn); this.add(Box.createVerticalStrut(20));
      this.add(createRoomBtn);this.add(Box.createVerticalStrut(20));
      this.add(this.enterRoomBtn); this.add(Box.createVerticalStrut(20));
      this.add(this.exitBtn);

    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    SoundEffect.STARTUP.play();
  }

  void setButtonsEnabled(boolean enabled) {
    this.localBtn.setEnabled(enabled);
    this.createRoomBtn.setEnabled(enabled);
    this.enterRoomBtn.setEnabled(enabled);
    this.exitBtn.setEnabled(enabled);
  }

  class btnClickedListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      String cmd = e.getActionCommand();
      StartupGUI.this.setButtonsEnabled(false);
      switch (cmd) {
        case "Local":
          StartupGUI.this.setVisible(false);
          SwingUtilities.invokeLater(() -> {
            GameGUI gameGUI = new GameGUI(StartupGUI.this);
            gameGUI.pack();
            gameGUI.setVisible(true);
          });
          break;
        case "CreateRoom":
          String strPort = (String) JOptionPane.showInputDialog(StartupGUI.this, "Please input " +
                          "the room ID (port):", "Create Room", JOptionPane.QUESTION_MESSAGE,
                  null, null, "1080");
          // input check
          if (strPort == null || strPort.equals("")) {
            JOptionPane.showMessageDialog(StartupGUI.this, "Invalid Port", "Warning", JOptionPane
                    .WARNING_MESSAGE);
            StartupGUI.this.setButtonsEnabled(true);
            return;
          } else {
            try {
              int port = Integer.parseInt(strPort);
              GameServer gameServer = new GameServer(port);
              StartupGUI.this.setVisible(false);
              SwingUtilities.invokeLater(() -> {
                RoomGUI roomGUI = new RoomGUI(StartupGUI.this, gameServer);
                roomGUI.waitingForClient(false);
                roomGUI.pack();
                roomGUI.setVisible(true);
              });
            } catch (NumberFormatException e1) {
              e1.printStackTrace();
              JOptionPane.showMessageDialog(StartupGUI.this, "Invalid Port", "Warning", JOptionPane
                      .WARNING_MESSAGE);
              StartupGUI.this.setButtonsEnabled(true);
              return;
            } catch (IOException e2) {
              e2.printStackTrace();
              JOptionPane.showMessageDialog(StartupGUI.this, "Cannot create room", "Warning",
                      JOptionPane.WARNING_MESSAGE);
              StartupGUI.this.setButtonsEnabled(true);
              return;
            }
          }
          break;
        case "EnterRoom":
          // Input host ip
          String ip = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" +
                  "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
          Pattern ipPattern = Pattern.compile(ip);
          String host = (String) JOptionPane.showInputDialog(StartupGUI.this, "Please input the" +
                          "  Room Server Host:", "Enter Room", JOptionPane.QUESTION_MESSAGE,
                  null, null, "127.0.0.1");
          // input check
          if (host == null || host.equals("")) {
            JOptionPane.showMessageDialog(StartupGUI.this, "Invalid Host", "Warning", JOptionPane
                    .WARNING_MESSAGE);
            StartupGUI.this.setButtonsEnabled(true);
            return;
          } else {
            Matcher mat = ipPattern.matcher(host);
            if (!mat.matches()) {
              JOptionPane.showMessageDialog(StartupGUI.this, "Invalid Host", "Warning",
                      JOptionPane.WARNING_MESSAGE);
              StartupGUI.this.setButtonsEnabled(true);
              return;
            }
          }
          // Input port
          String strPort2 = (String) JOptionPane.showInputDialog(StartupGUI.this, "Please input" +
                          " " +
                          "the room ID (port):", "Enter Room", JOptionPane.QUESTION_MESSAGE,
                    null, null, "1080");
          // input check
          if (strPort2 == null || strPort2.equals("")) {
            JOptionPane.showMessageDialog(StartupGUI.this, "Invalid Port", "Warning", JOptionPane
                    .WARNING_MESSAGE);
            StartupGUI.this.setButtonsEnabled(true);
            return;
          } else {
            try {
              int port = Integer.parseInt(strPort2);
              GameClient gameClient = new GameClient(host, port);
              StartupGUI.this.setVisible(false);
              SwingUtilities.invokeLater(() -> {
                RoomGUI roomGUI = new RoomGUI(StartupGUI.this, gameClient);
                roomGUI.waitingForServer();
                roomGUI.pack();
                roomGUI.setVisible(true);
              });
            } catch (NumberFormatException e1) {
              e1.printStackTrace();
              JOptionPane.showMessageDialog(StartupGUI.this, "Invalid Port", "Warning", JOptionPane
                      .WARNING_MESSAGE);
              StartupGUI.this.setButtonsEnabled(true);
              return;
            } catch (IOException e2) {
              e2.printStackTrace();
              JOptionPane.showMessageDialog(StartupGUI.this, "Cannot enter room", "Warning",
                      JOptionPane.WARNING_MESSAGE);
              StartupGUI.this.setButtonsEnabled(true);
              return;
            }
          }
          break;
        case "Exit":
          dispose();
          System.exit(0);
      }
    }
  }
}
