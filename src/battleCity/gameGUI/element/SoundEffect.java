package battleCity.gameGUI.element;

import java.io.BufferedInputStream;
import javax.sound.sampled.*;

/**
 * Created by Xia on 2017/5/30.
 * sound effect enumeration
 */
public enum SoundEffect {
  STARTUP("/sound/startup.wav"), ROOM("/sound/room.wav"), SHOOT("/sound/shoot.wav"), BOOM
          ("/sound/boom.wav"), WIN("/sound/win.wav"), LOSE("/sound/lose.wav");

  private Clip clip;

  SoundEffect(String soundFilePath) {
    try {
      this.clip = AudioSystem.getClip();
      AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
              new BufferedInputStream(getClass().getResourceAsStream(soundFilePath)));
      this.clip.open(audioInputStream);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  public void play() {
    new Thread(() -> {
      if (SoundEffect.this.clip.isRunning())
        SoundEffect.this.clip.stop();
      SoundEffect.this.clip.setFramePosition(0);
      SoundEffect.this.clip.start();
    }).start();
  }
}
