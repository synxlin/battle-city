package battleCity.gameNetwork;

import battleCity.gameControl.*;

import java.util.*;
import java.io.*;
import java.net.*;

/**
 * Created by Xia on 2017/5/26.
 * game socket class
 */
public class GameSocket {
  private Socket socket;
  private ObjectOutputStream objectOutputStream = null;
  private ObjectInputStream objectInputStream = null;

  GameSocket(Socket socket) throws IOException {
    this.socket = socket;
    this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
    this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
  }

  String receiveMessage() throws IOException, ClassNotFoundException {
    return (String)this.objectInputStream.readObject();
  }

  public GameInfo receiveGameInfo() throws IOException, ClassNotFoundException {
    String message = (String) this.objectInputStream.readObject();
    byte [] data = Base64.getDecoder().decode( message );
    ObjectInputStream objInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
    GameInfo gameInfo  = (GameInfo)objInputStream.readObject();
    objInputStream.close();
    return gameInfo;
  }

  synchronized public void sendMessage(String message) throws IOException {
    //System.out.println("Send Message: " + message);
    this.objectOutputStream.writeObject(message);
    this.objectOutputStream.flush();
  }

  synchronized void sendGameInfo(GameInfo gameInfo) throws IOException {
    String message;
    synchronized (gameInfo.stateLock) {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream objOutputStream = new ObjectOutputStream(byteArrayOutputStream);
      objOutputStream.writeObject(gameInfo);
      objOutputStream.close();
      message = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }
    this.objectOutputStream.writeObject(message);
    this.objectOutputStream.flush();
  }

  synchronized public void close() throws IOException {
    this.objectOutputStream.flush();
    this.socket.close();
  }

  String getHost(boolean isRemote) {
    if (isRemote)
      return this.socket.getInetAddress().getHostAddress();
    else
      return this.socket.getLocalAddress().getHostAddress();
  }

  int getPort(boolean isRemote) {
    if (isRemote)
      return this.socket.getPort();
    else
      return this.socket.getLocalPort();
  }
}
