package battleCity.gameNetwork;

import battleCity.gameControl.*;
import battleCity.gameControl.state.*;
import battleCity.gameGUI.*;

import java.io.*;
import java.net.*;

/**
 * Created by Xia on 2017/5/26.
 * game client class
 */
public class GameClient {
  public GameInfo gameInfo;
  public GameSocket clientSocket;
  public ClientState state;
  public final Object stateLock = new Object();
  private GameGUI gameGUI;

  public GameClient(String host, int port) throws IOException {
    Socket socket = new Socket();
    try {
      socket.connect(new InetSocketAddress(host, port), 5000);
    } catch (SocketTimeoutException e) {
      e.printStackTrace();
      throw new IOException();
    }

    this.clientSocket = new GameSocket(socket);
    try {
      clientSocket.sendMessage(GameProtocol.JoinPacket());
      String message = clientSocket.receiveMessage();
      if (!GameProtocol.isJoin(message)) {
        clientSocket.close();
        throw new IOException();
      }
      this.state = ClientState.CONNECTED;
    } catch (Exception e) {
      e.printStackTrace();
      clientSocket.close();
      throw new IOException();
    }
  }

  public void waitingForServerPrepared() {
    Thread t = new Thread(() -> {
        while (GameClient.this.state != ClientState.PREPARED && GameClient.this.state !=
                ClientState.ERROR && GameClient.this.state != ClientState.EXIT && GameClient.this
                .state != ClientState.SERVER_PREPARED) {
          try {
            String message = GameClient.this.clientSocket.receiveMessage();
            synchronized (GameClient.this.stateLock) {
              switch (GameClient.this.state) {
                case CONNECTED:
                  if (GameProtocol.isPrepared(message))
                    GameClient.this.state = ClientState.SERVER_PREPARED;
                  else if (GameProtocol.isExit(message))
                    GameClient.this.state = ClientState.EXIT;
                  break;
                case CLIENT_PREPARED:
                  if (GameProtocol.isPrepared(message))
                    GameClient.this.state = ClientState.PREPARED;
                  else if (GameProtocol.isExit(message))
                    GameClient.this.state = ClientState.EXIT;
                  break;
                case EXIT:
                  break;
                default:
                  GameClient.this.state = ClientState.ERROR;
              }
              GameClient.this.stateLock.notifyAll();
            }
          } catch (Exception e) {
            e.printStackTrace();
            synchronized (GameClient.this.stateLock) {
              GameClient.this.state = ClientState.ERROR;
              GameClient.this.stateLock.notifyAll();
            }
          }
        }
        // deal with exit / error
        synchronized (GameClient.this.stateLock) {
          while (GameClient.this.state != ClientState.PREPARED && GameClient.this.state !=
                  ClientState.EXIT && GameClient.this.state != ClientState.ERROR) {
            try {
              GameClient.this.stateLock.wait();
            } catch (InterruptedException e) {
              e.printStackTrace();
              GameClient.this.state = ClientState.ERROR;
              GameClient.this.stateLock.notifyAll();
            }
          }
          if (GameClient.this.state != ClientState.PREPARED) {
            this.close();
          }
        }
    });
    t.start();
  }

  public void setInsideGameGUI(GameGUI gameGUI, GameInfo gameInfo) {
    this.gameGUI = gameGUI; this.gameInfo = gameInfo;
  }

  public void receiveFromServer() {
    Thread t = new Thread(() -> {
      while(GameClient.this.gameInfo.state != GameState.ERROR && GameClient.this.gameInfo.state
              != GameState.WIN_OVER && GameClient.this.gameInfo.state != GameState.LOSE_OVER) {
        try {
          GameInfo rGameInfo = GameClient.this.clientSocket.receiveGameInfo();
          if (rGameInfo.numEnemyLeft == -1) {
            this.clientSocket.sendMessage(GameProtocol.OverPacket());
            synchronized (GameClient.this.gameInfo.stateLock) {
              switch (GameClient.this.gameInfo.state) {
                case WIN:
                  GameClient.this.gameInfo.state = GameState.WIN_OVER;
                  break;
                case LOSE:
                  GameClient.this.gameInfo.state = GameState.LOSE_OVER;
                  break;
              }
              GameClient.this.gameInfo.stateLock.notifyAll();
            }
          } else {
            synchronized (GameClient.this.gameInfo.stateLock) {
              GameClient.this.gameInfo.update(rGameInfo);
              switch (rGameInfo.state) {
                case PLAYING:
                  this.gameGUI.pauseBtn.setEnabled(true);
                  this.gameGUI.restartBtn.setEnabled(false);
                  break;
                case PAUSE:
                  this.gameGUI.pauseBtn.setEnabled(false);
                  this.gameGUI.restartBtn.setEnabled(true);
                  break;
              }
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
          synchronized (GameClient.this.gameInfo.stateLock) {
            GameClient.this.gameInfo.state = GameState.ERROR;
            GameClient.this.gameInfo.stateLock.notifyAll();
          }
        }
      }
    });
    t.start();
  }

  public void setKeyState(Key key, boolean pressed) {
    try {
      this.clientSocket.sendMessage(GameProtocol.KeyPacket(key, pressed));
    } catch (Exception e) {
      e.printStackTrace();
      this.gameInfo.state = GameState.ERROR;
    }
  }

  public String getHost(boolean isRemote) {
    return this.clientSocket.getHost(isRemote);
  }

  public int getPort(boolean isRemote) {
    return this.clientSocket.getPort(isRemote);
  }

  public void close() {
    try {
      this.clientSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
