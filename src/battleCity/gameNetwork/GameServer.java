package battleCity.gameNetwork;

import battleCity.gameControl.*;
import battleCity.gameControl.state.*;
import battleCity.gameGUI.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Created by Xia on 2017/5/26.
 * Server thread
 */
public class GameServer {
  private ServerSocket serverSocket;
  public GameSocket clientSocket;
  public ServerState state;
  public final Object stateLock = new Object();
  private SelfTank tank;
  private GameInfo gameInfo;
  private GameGUI gameGUI;

  public GameServer(int port) throws IOException {
    this.state = ServerState.WAITING;
    this.serverSocket = new ServerSocket(port);
    this.serverSocket.setSoTimeout(500);
  }

  public String getHost(boolean isRemote) {
    if (isRemote)
      return this.clientSocket.getHost(true);
    else
      return this.serverSocket.getInetAddress().getHostAddress();
  }

  public int getPort(boolean isRemote) {
    if (isRemote) {
      return this.clientSocket.getPort(true);
    } else {
      return this.serverSocket.getLocalPort();
    }
  }

  public void close() {
    if (this.serverSocket.isClosed())
      return;
    try {
      this.serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void waitForClient() {
    Thread t = new Thread(() -> {
      while (GameServer.this.state != ServerState.EXIT && GameServer.this.state != ServerState.ERROR) {
        try {
          Socket client;
          try {
            client = GameServer.this.serverSocket.accept();
          } catch (SocketTimeoutException e) {
            continue;
          }
          GameSocket gclient = new GameSocket(client);
          Thread tt = new Thread(() -> {
              if (waitForClientConnected(gclient))
                waitForClientPrepared();
          });
          tt.start();
        } catch (IOException e) {
          e.printStackTrace();
          GameServer.this.close();
          synchronized (GameServer.this.stateLock) {
            GameServer.this.state = ServerState.ERROR;
            GameServer.this.stateLock.notifyAll();
          }
        }
      }
      GameServer.this.close();
    });
    t.start();
  }

  private boolean waitForClientConnected(GameSocket gclient) {
    try {
      // wait for client join
      if (GameServer.this.state == ServerState.WAITING || GameServer.this.state ==
              ServerState.WAITING_SERVER_PREPARED) {
        String message = gclient.receiveMessage();
        synchronized (GameServer.this.stateLock) {
          switch (GameServer.this.state) {
            case WAITING:
              if (GameProtocol.isJoin(message)) {
                GameServer.this.clientSocket = gclient;
                GameServer.this.clientSocket.sendMessage(GameProtocol.JoinPacket());
                GameServer.this.state = ServerState.CONNECTED;
              } else {
                gclient.close();
                return false;
              }
              break;
            case WAITING_SERVER_PREPARED:
              if (GameProtocol.isJoin(message)) {
                GameServer.this.clientSocket = gclient;
                GameServer.this.clientSocket.sendMessage(GameProtocol.JoinPacket());
                GameServer.this.state = ServerState.SERVER_PREPARED;
                GameServer.this.clientSocket.sendMessage(GameProtocol.PreparedPacket());
              } else {
                gclient.close();
                return false;
              }
              break;
            case EXIT:
              gclient.sendMessage(GameProtocol.ExitPacket());
              gclient.close();
              return false;
            case ERROR:
              gclient.close();
              throw new Exception("ERROR");
            default:
              gclient.close();
              return false;
          }
          GameServer.this.stateLock.notifyAll();
          return true;
        }
      } else {
        gclient.sendMessage(GameProtocol.ExitPacket());
        gclient.close();
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      GameServer.this.close();
      synchronized (GameServer.this.stateLock) {
        GameServer.this.state = ServerState.ERROR;
        GameServer.this.stateLock.notifyAll();
      }
      return false;
    }
  }

  public void waitForClientPrepared() {
    try {
      // wait for client prepared
      while (GameServer.this.state == ServerState.CONNECTED || GameServer.this.state == ServerState
              .SERVER_PREPARED) {
        try {
          String message = GameServer.this.clientSocket.receiveMessage();
          synchronized (GameServer.this.stateLock) {
            switch (GameServer.this.state) {
              case CONNECTED:
                if (GameProtocol.isPrepared(message))
                  GameServer.this.state = ServerState.CLIENT_PREPARED;
                else if (GameProtocol.isExit(message)) {
                  GameServer.this.state = ServerState.WAITING;
                  GameServer.this.stateLock.notifyAll();
                  return;
                }
                break;
              case SERVER_PREPARED:
                if (GameProtocol.isPrepared(message))
                  GameServer.this.state = ServerState.PREPARED;
                else if (GameProtocol.isExit(message)) {
                  GameServer.this.state = ServerState.WAITING;
                  GameServer.this.stateLock.notifyAll();
                  return;
                }
                break;
              case EXIT:
                break;
              default:
                this.clientSocket.close();
                throw new Exception("ERROR");
            }
            GameServer.this.stateLock.notifyAll();
          }
        } catch (Exception e) {
          e.printStackTrace();
          GameServer.this.clientSocket.close();
          throw e;
        }
      }
      synchronized (GameServer.this.stateLock) {
        while (GameServer.this.state != ServerState.PREPARED && GameServer.this.state !=
                ServerState.EXIT && GameServer.this.state != ServerState.ERROR)
          GameServer.this.stateLock.wait();
        switch (GameServer.this.state) {
          case EXIT:
            GameServer.this.clientSocket.sendMessage(GameProtocol.ExitPacket());
          case ERROR:
            GameServer.this.clientSocket.close();
            GameServer.this.close();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      GameServer.this.close();
      synchronized (GameServer.this.stateLock) {
        GameServer.this.state = ServerState.ERROR;
        GameServer.this.stateLock.notifyAll();
      }
    }
  }

  public void setInsideGameGUI(GameGUI gameGUI, GameInfo gameInfo, SelfTank tank) {
    this.tank = tank; this.gameInfo = gameInfo; this.gameGUI = gameGUI;
  }

  public void sendToClient() {
    Thread t = new Thread(() -> {
      while (GameServer.this.gameInfo.state == GameState.PLAYING ||
              GameServer.this.gameInfo.state == GameState.PAUSE) {
        long lastTime = new Date().getTime();
        try {
          GameServer.this.clientSocket.sendGameInfo(GameServer.this.gameInfo);
        } catch (IOException e) {
          e.printStackTrace();
          synchronized (GameServer.this.gameInfo.stateLock) {
            GameServer.this.gameInfo.state = GameState.ERROR;
            GameServer.this.gameInfo.stateLock.notifyAll();
          }
        }
        // sleeping
        long time = new Date().getTime();
        long waitingTime = GameConfig.sendTimeLapse - (time - lastTime);
        if (waitingTime > 0) {
          try {
            Thread.sleep(waitingTime);
          } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
          }
        }
      }
      if (GameServer.this.gameInfo.state != GameState.ERROR) {
        try {
          GameServer.this.clientSocket.sendGameInfo(GameServer.this.gameInfo);
          GameInfo overGameInfo = new GameInfo();
          overGameInfo.numEnemyLeft = -1;
          GameServer.this.clientSocket.sendGameInfo(overGameInfo);
        } catch (IOException e) {
          e.printStackTrace();
          synchronized (GameServer.this.gameInfo.stateLock) {
            GameServer.this.gameInfo.state = GameState.ERROR;
            GameServer.this.gameInfo.stateLock.notifyAll();
          }
        }
      }
    });
    t.start();
  }

  public void receiveFromClient() {
    Thread t = new Thread(() -> {
      while(GameServer.this.gameInfo.state != GameState.ERROR &&
              GameServer.this.gameInfo.state != GameState.WIN_OVER &&
              GameServer.this.gameInfo.state != GameState.LOSE_OVER) {
        try {
          String message = GameServer.this.clientSocket.receiveMessage();
          if (GameProtocol.isKeyPacket(message)) {
            GameServer.this.tank.setKeyState(GameProtocol.getKey(message), GameProtocol
                    .getKeyPressed(message));
          } else if (GameProtocol.isGameStatePacket(message)) {
            switch (GameProtocol.getGameState(message)) {
              case PAUSE:
                GameServer.this.gameGUI.pauseBtn.doClick();
                break;
              case PLAYING:
                GameServer.this.gameGUI.restartBtn.doClick();
                break;
            }
          } else if (GameProtocol.isOver(message)) {
            synchronized (GameServer.this.gameInfo.stateLock) {
              switch (GameServer.this.gameInfo.state) {
                case WIN:
                  GameServer.this.gameInfo.state = GameState.WIN_OVER;
                  break;
                case LOSE:
                  GameServer.this.gameInfo.state = GameState.LOSE_OVER;
                  break;
              }
              GameServer.this.gameInfo.stateLock.notifyAll();
            }
            return;
          }
        } catch (Exception e) {
          e.printStackTrace();
          synchronized (GameServer.this.gameInfo.stateLock) {
            GameServer.this.gameInfo.state = GameState.ERROR;
            GameServer.this.gameInfo.stateLock.notifyAll();
          }
        }
      }
    });
    t.start();
  }
}
