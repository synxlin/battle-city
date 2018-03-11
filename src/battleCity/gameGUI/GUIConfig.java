package battleCity.gameGUI;

import battleCity.gameControl.*;

/**
 * Created by Xia on 2017/5/26.
 * GUI config class
 */
class GUIConfig {
  static final String startupTitle = "Battle City";
  static final int startupTitleHeight = 80;
  static final int startupButtonHeight = 40;
  static final int startupButtonWidth = 100;
  static final int startupGuiHeight = 350;
  static final int startupGuiWidth = 300;

  static final String gameTitle = "Battle City - Made by Yujun Lin";
  static final String gameServerTitle = "Battle City (Host) - Made by Yujun Lin";
  static final String gameClientTitle = "Battle City (Guest) - Made by Yujun Lin";
  static final int blockSize = 20;
  static final int boardSize = (GameConfig.boardInfoSize - 2) * GUIConfig.blockSize;
  static final int tankSize = blockSize * 2;
  static final int shotSize = 5;
  static final int baseSize = blockSize * 2;

  static final int infoPanelHeight = boardSize;
  static final int infoPanelWidth = 155;
  static final int infoLabelHeight = 40;
  static final int infoEnemyTankSize = 20;
  static final int infoEnemyTankPanelWidth = (infoEnemyTankSize + 5) * 5;
  static final int infoEnemyTankPanelHeight = (infoEnemyTankSize + 5) * 5;
  static final int infoEnemyLabelHeight = infoLabelHeight;
  static final int infoEnemyLabelWidth = infoPanelWidth;
  static final int infoEnemyPanelWidth = infoEnemyLabelWidth;
  static final int infoEnemyPanelHeight = infoEnemyLabelHeight + infoEnemyTankPanelHeight + 10;
  static final int infoPlayerLabelHeight = infoLabelHeight;
  static final int infoPlayerLabelWidth = infoPanelWidth;
  static final int infoSelfTankSize = 45;
  static final int infoLifeScoreHeight = 25;
  static final int infoLifeScoreWidth = infoLifeScoreHeight * 2;
  static final int infoPlayerPanelHeight = infoLifeScoreHeight * 2 + infoPlayerLabelHeight;
  static final int infoPlayerPanelWidth = infoPanelWidth;

  static final int infoButtonHeight = 40;
  static final int infoButtonWidth = 100;
  static final int infoButtonPanelHeight = infoButtonHeight * 2 + 20 + 30;
  static final int infoButtonPanelWidth = infoPanelWidth;

  static final int gameGuiHeight = boardSize;
  static final int gameGuiWidth = boardSize + 5 + infoPanelWidth;

  static final String roomServerTitle = "Room (Host)";
  static final String roomClientTitle = "Room (Guest)";
  static final int roomButtonHeight = 30;
  static final int roomButtonWidth = 50;
  static final int roomIconSize = 40;
  static final int roomInfoWidth = 120;
  static final int roomPanelHeight = 100;
  static final int roomPanelWidth = 250;
  static final int roomGuiHeight = 250;
  static final int roomGuiWidth = 360;

  static final String recordTitle = "Record";
  static final int recordTitleHeight = 60;
  static final int recordTitleWidth = 200;
  static final int recordLabelHeight = 30;
  static final int recordLabelWidth = 100;
  static final int recordButtonHeight = 40;
  static final int recordButtonWidth = 100;
  static final int recordGuiHeight = 350;
  static final int recordGuiWidth = 300;
}
