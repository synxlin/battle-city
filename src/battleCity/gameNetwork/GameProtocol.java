package battleCity.gameNetwork;


import battleCity.gameControl.state.*;

/**
 * Created by Xia on 2017/5/26.
 * Protocol for Communication
 */
public class GameProtocol {
  static String JoinPacket() { return "Hello Tank"; }

  public static String ExitPacket() { return "Exit"; }

  public static String PreparedPacket() { return "Prepared"; }

  public static String KeyPacket(Key key, boolean pressed) {
    return "Key " + key + " " + pressed;
  }

  public static String GameStatePacket(GameState gameState) {
    return "State " + gameState;
  }

  public static String OverPacket() { return "Game Over"; }

  static boolean isJoin(String packet) {
    return packet.equals(GameProtocol.JoinPacket());
  }

  static boolean isExit(String packet) {
    return packet.equals(GameProtocol.ExitPacket());
  }

  static boolean isPrepared(String packet) {
    return packet.equals(GameProtocol.PreparedPacket());
  }

  static boolean isGameStatePacket(String packet) {
    String[] m = packet.split("\\s+");
    return m[0].equals("State");
  }

  static boolean isKeyPacket(String packet) {
    String[] m = packet.split("\\s+");
    return m[0].equals("Key");
  }

  static boolean isOver(String packet) {
    return packet.equals(GameProtocol.OverPacket());
  }

  static Key getKey(String packet) {
    String[] m = packet.split("\\s+");
    switch (m[1]) {
      case "UP":
        return Key.UP;
      case "DOWN":
        return Key.DOWN;
      case "LEFT":
        return Key.LEFT;
      case "RIGHT":
        return Key.RIGHT;
      case "SHOOT":
        return Key.SHOOT;
      default:
          return Key.NOTHING;
    }
  }

  static boolean getKeyPressed(String packet) {
    return packet.split("\\s+")[2].equals("true");
  }

  static GameState getGameState(String packet) {
    String[] m = packet.split("\\s+");
    switch (m[1]) {
      case "PAUSE":
        return GameState.PAUSE;
      default:
        return GameState.PLAYING;
    }
  }
}
