package battleCity.gameGUI.element;

import javax.swing.*;
import java.awt.*;
import java.net.*;

/**
 * Created by Xia on 2017/5/26.
 * ImageButton class is child class of JButton
 */
public class ImageButton extends JButton {
  public ImageButton(String imagePath, String rolloverImagePath, String pressedImagePath, String
          disabledImagePath) {
    super();
    URL img, rImg, pImg, dImg;
    img = getClass().getResource(imagePath);
    rImg = getClass().getResource(rolloverImagePath);
    pImg = getClass().getResource(pressedImagePath);
    dImg = getClass().getResource(disabledImagePath);
    this.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.setAlignmentY(Component.CENTER_ALIGNMENT);
    this.setBorder(null); this.setBorderPainted(false);
    this.setMargin(new Insets(0,0,0,0));
    this.setContentAreaFilled(false);
    try {
    this.setIcon(new ImageIcon(img)); this.setRolloverIcon(new ImageIcon(rImg));
    this.setPressedIcon(new ImageIcon(pImg)); this.setDisabledIcon(new ImageIcon(dImg));
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}
