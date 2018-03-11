package battleCity.gameControl.state;

import java.io.*;

/**
 * Created by Xia on 2017/5/26.
 * Press condition
 */
public enum PressState implements Serializable {
  PRESSED, NEVER_PRESSED, ONCE_PRESSED;

  @Override
  public String toString() {
    switch (this) {
      case PRESSED:
        return "PRESSED";
      case ONCE_PRESSED:
        return "ONCE";
      default:
        return "NEVER";
    }
  }
}
