package battleCity.gameControl.state;

import java.io.Serializable;

/**
 * Created by Xia on 2017/5/26.
 * state class of key
 */
public class KeyState implements Serializable {
  private Key key;
  private PressState state;

  public KeyState(Key key, boolean pressed) {
    this.key = key;
    this.state = pressed ? PressState.PRESSED : PressState.NEVER_PRESSED;
  }

  public Key getKey() { return this.key; }

  public PressState getState() { return this.state; }

  public void setState(boolean pressed) {
    if (pressed) {
      this.state = PressState.PRESSED;
    } else if (this.state == PressState.PRESSED){
      this.state = PressState.ONCE_PRESSED;
    }
  }

  public void settleState() {
    if (this.state != PressState.PRESSED)
      this.state = PressState.NEVER_PRESSED;
  }

  public Direction toDirection() {
    if (this.state == PressState.NEVER_PRESSED)
      return Direction.STAY;
    switch (this.key) {
      case UP:
        return Direction.UP;
      case DOWN:
        return Direction.DOWN;
      case LEFT:
        return Direction.LEFT;
      case RIGHT:
        return Direction.RIGHT;
      default:
        return Direction.STAY;
    }
  }

  @Override
  public String toString() {
    return key.toString() + " - " + state.toString();
  }
}
