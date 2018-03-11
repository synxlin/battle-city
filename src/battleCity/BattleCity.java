package battleCity;

import battleCity.gameGUI.*;

import javax.swing.*;

/**
 * Created by Xia on 2017/5/26.
 * Entrance
 */
public class BattleCity extends JFrame {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      StartupGUI startupGUI = new StartupGUI();
      startupGUI.pack();
      startupGUI.setVisible(true);
    });
  }
}
