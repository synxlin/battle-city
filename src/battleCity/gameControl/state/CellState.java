package battleCity.gameControl.state;

import java.io.*;

/**
 * Created by Xia on 2017/5/27.
 * enumeration of cell state
 */
public enum CellState implements Serializable {
  EMPTY, NORMAL_WALL, STEEL_WALL, SEA, TANK
}
