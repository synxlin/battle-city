package battleCity.gameGUI;

import battleCity.gameControl.*;
import battleCity.gameGUI.element.*;
import battleCity.gameNetwork.ClientState;
import battleCity.gameNetwork.ServerState;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

/**
 * Created by Xia on 2017/5/30.
 * result showing GUI
 */
class RecordGUI extends JFrame {
  private GameGUI gameGUI;
  private int score;
  private ArrayList<Result> results;
  private boolean isBackToRoom;
  private File resultFile;
  private JLabel[] names, scores;
  private ImageButton saveBtn, backBtn;
  private boolean isRecord;

  RecordGUI(GameGUI gameGUI, boolean isBackToRoom, int score) {
    super(GUIConfig.recordTitle);
    this.gameGUI = gameGUI;
    this.score = score;
    this.results = new ArrayList<>();
    this.isBackToRoom = isBackToRoom;

    this.resetRecords();
    this.setRecordGUI();
  }

  private void resetRecords() {
    try {
      File file = new File(GameConfig.resultFilePath);
      if (file.exists()) {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
          String uname = scanner.next();
          int uscore = scanner.nextInt();
          this.results.add(new Result(uname, uscore));
        }
        int s = this.results.size();
        if (s < 5) {
          for (int i = s; i < 5; i++)
            this.results.add(new Result("-", 0));
        }
        this.sortRecords();
      }
      else {
        if (!file.createNewFile())
          throw new IOException();
        for (int i = 0; i < 5; i++)
          this.results.add(new Result("-", 0));
      }
      this.resultFile = file;
      this.isRecord = this.score > this.results.get(4).score;
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private void sortRecords() {
    int s = this.results.size();
    (this.results).sort((o1, o2) -> {
      if (o1.score > o2.score)
        return -1;
      else if (o1.score < o2.score)
        return 1;
      else
        return o1.name.compareTo(o2.name);
    });
    if (s > 5) {
      for (int i = 0; i < s - 5; i++)
        this.results.remove(5);
    }
  }

  private void setRecordGUI() {
    try {
    BufferedImage backgroundImage = ImageIO.read(getClass().getResource
            ("/images/result/background.png"));
    this.setLayout(new BorderLayout());
    this.setContentPane(new JLabel(new ImageIcon(backgroundImage)));
    this.setPreferredSize(new Dimension(GUIConfig.recordGuiWidth, GUIConfig.recordGuiHeight));
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    ImageLabel resultTitle = new ImageLabel("/images/result/title.png");
    resultTitle.setPreferredSize(new Dimension(GUIConfig.recordTitleWidth, GUIConfig.recordTitleHeight));
    this.add(resultTitle); this.add(Box.createVerticalStrut(10));
    this.names = new JLabel[5]; this.scores = new JLabel[5];
    for (int i = 0; i < 5; i++) {
      JPanel resultInfoPanel = new JPanel();
      resultInfoPanel.setLayout(new BoxLayout(resultInfoPanel, BoxLayout.X_AXIS));
      JLabel number = new JLabel();
      Font f = new Font(number.getFont().getFontName(), number.getFont().getStyle(), 14);
      number.setText("" + i); number.setForeground(Color.YELLOW); number.setFont(f);
      number.setPreferredSize(new Dimension(GUIConfig.recordLabelHeight, GUIConfig.recordLabelHeight));
      this.names[i] = new JLabel();
      this.names[i].setForeground(Color.YELLOW);this.names[i].setFont(f);
      this.names[i].setText(this.results.get(i).name);
      this.names[i].setPreferredSize(new Dimension(GUIConfig.recordLabelWidth, GUIConfig
              .recordLabelHeight));
      this.scores[i] = new JLabel();
      this.scores[i].setForeground(Color.YELLOW); this.scores[i].setFont(f);
      this.scores[i].setText("" + this.results.get(i).score);
      this.scores[i].setPreferredSize(new Dimension(GUIConfig.recordLabelWidth, GUIConfig
              .recordLabelHeight));
      resultInfoPanel.add(number); resultInfoPanel.add(Box.createHorizontalStrut(10));
      resultInfoPanel.add(this.names[i]); resultInfoPanel.add(Box.createHorizontalStrut(10));
      resultInfoPanel.add(this.scores[i]);
      resultInfoPanel.setOpaque(false);
      this.add(resultInfoPanel); this.add(Box.createVerticalStrut(10));
    }

    JPanel resultInfoPanel = new JPanel();
    resultInfoPanel.setLayout(new BoxLayout(resultInfoPanel, BoxLayout.X_AXIS));
    JLabel number = new JLabel();
    Font f = new Font(number.getFont().getFontName(), number.getFont().getStyle(), 14);
    number.setText("*"); number.setForeground(Color.YELLOW); number.setFont(f);
    number.setPreferredSize(new Dimension(GUIConfig.recordLabelHeight, GUIConfig.recordLabelHeight));
    JLabel playerName = new JLabel();
    playerName.setForeground(Color.YELLOW); playerName.setFont(f);
    playerName.setText("Player");
    playerName.setPreferredSize(new Dimension(GUIConfig.recordLabelWidth, GUIConfig
            .recordLabelHeight));
    JLabel playerScore = new JLabel();
    playerScore.setForeground(Color.YELLOW); playerScore.setFont(f);
    playerScore.setText("" + this.score);
    playerScore.setPreferredSize(new Dimension(GUIConfig.recordLabelWidth,
            GUIConfig.recordLabelHeight));
    resultInfoPanel.add(number); resultInfoPanel.add(Box.createHorizontalStrut(10));
    resultInfoPanel.add(playerName); resultInfoPanel.add(Box.createHorizontalStrut(10));
    resultInfoPanel.add(playerScore);
    resultInfoPanel.setOpaque(false);
    this.add(resultInfoPanel); this.add(Box.createVerticalStrut(10));

    this.saveBtn = new ImageButton("/images/result/saveBtn.png",
            "/images/result/pressedSaveBtn.png", "/images/result/pressedSaveBtn.png",
            "/images/result/disabledSaveBtn.png");
    this.saveBtn.setPreferredSize(new Dimension(GUIConfig.recordButtonWidth, GUIConfig.recordButtonHeight));
    this.saveBtn.addActionListener(new recordButtonListener());
    this.saveBtn.setActionCommand("Save");
    if (!this.isRecord)
      this.saveBtn.setEnabled(false);

    this.backBtn = new ImageButton("/images/result/backBtn.png",
            "/images/result/pressedBackBtn.png", "/images/result/pressedBackBtn.png",
            "/images/result/disabledBackBtn.png");
    this.backBtn.setPreferredSize(new Dimension(GUIConfig.recordButtonWidth, GUIConfig.recordButtonHeight));
    this.backBtn.addActionListener(new recordButtonListener());
    this.backBtn.setActionCommand("Back");

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.add(this.saveBtn); buttonPanel.add(Box.createHorizontalStrut(10));
    buttonPanel.add(this.backBtn);
    buttonPanel.setPreferredSize(new Dimension(GUIConfig.recordGuiWidth, GUIConfig.recordButtonHeight));
    buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    buttonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
    buttonPanel.setOpaque(false);

    this.add(buttonPanel);
    this.pack();
    Dimension actualSize = this.getContentPane().getSize();
    int extraHeight = GUIConfig.recordGuiHeight - actualSize.height;
    this.setPreferredSize(new Dimension(GUIConfig.recordGuiWidth, GUIConfig.recordGuiHeight +
            extraHeight));
    this.pack(); this.setLocationRelativeTo(null);
  }

  private void saveRecords() {
    try {
      PrintStream stream = new PrintStream(this.resultFile);
      for (int i = 0; i < 5; i++) {
        stream.println(this.results.get(i).name);
        stream.println(this.results.get(i).score);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private void updateRecordGUI() {
    for (int i = 0; i < 5; i++) {
      this.names[i].setText(this.results.get(i).name);
      this.scores[i].setText("" + this.results.get(i).score);
    }
  }

  private void goBack() {
    RecordGUI.this.dispose();
    if (this.isBackToRoom) {
      this.gameGUI.resetRoomGUI();
      this.gameGUI.goBackRoom();
    } else
      this.gameGUI.goBackStartUp();
  }

  class Result {
    String name;
    int score;
    Result(String name, int score) {
      this.name = name;
      this.score = score;
    }
  }

  class recordButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      RecordGUI.this.saveBtn.setEnabled(false);
      RecordGUI.this.backBtn.setEnabled(false);
      String cmd = e.getActionCommand();
      switch (cmd) {
        case "Save":
          String playerName = (String) JOptionPane.showInputDialog(RecordGUI.this, "Please " +
                          "input your name:", "Save record", JOptionPane.QUESTION_MESSAGE,
                  null, null, "Player");
          if (playerName == null || playerName.equals("")) {
            JOptionPane.showMessageDialog(RecordGUI.this, "Invalid Name", "Warning", JOptionPane
                    .WARNING_MESSAGE);
            if (RecordGUI.this.isRecord)
              RecordGUI.this.saveBtn.setEnabled(true);
            RecordGUI.this.backBtn.setEnabled(true);
          } else {
            RecordGUI.this.results.add(new Result(playerName, RecordGUI.this.score));
            RecordGUI.this.sortRecords();
            RecordGUI.this.updateRecordGUI();
            RecordGUI.this.saveRecords();
            RecordGUI.this.repaint();
            RecordGUI.this.saveBtn.setEnabled(false);
            RecordGUI.this.backBtn.setEnabled(true);
          }
          break;
          case "Back":
            RecordGUI.this.goBack();
      }
    }
  }
}
