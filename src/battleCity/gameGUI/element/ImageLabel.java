package battleCity.gameGUI.element;

import javax.swing.*;
import java.awt.*;
import java.net.*;

/**
 * Created by Xia on 2017/5/6.
 * ImageLabel class is child class of JLabel
 */
public class ImageLabel extends JLabel {
  public ImageLabel(String imagePath) {
    super();
    URL url = getClass().getResource(imagePath);
    try {
      this.setIcon(new ImageIcon(url));
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    this.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.setAlignmentY(Component.CENTER_ALIGNMENT);
    this.setBorder(null);
    this.setHorizontalAlignment(JLabel.CENTER);
    this.setVerticalAlignment(JLabel.CENTER);
  }
}