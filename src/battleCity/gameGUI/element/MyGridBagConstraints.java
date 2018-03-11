package battleCity.gameGUI.element;

import java.awt.*;

/**
 * Created by Xia on 2017/6/4.
 * My GridBagConstraints
 */
public class MyGridBagConstraints extends GridBagConstraints {

  public MyGridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight)
  {
    this.gridx = gridx;
    this.gridy = gridy;
    this.gridwidth = gridwidth;
    this.gridheight = gridheight;
  }

  public MyGridBagConstraints setAnchor(int anchor)
  {
    this.anchor = anchor;
    return this;
  }

  public MyGridBagConstraints setFill(int fill)
  {
    this.fill = fill;
    return this;
  }
}
