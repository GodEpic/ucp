package com.yum.ucp.common.utils;

import org.apache.http.nio.conn.NHttpClientConnectionManager;

import java.util.concurrent.TimeUnit;

/**
 * Created by David.Wu on 2015/10/21.
 */
public class IdleConnectionEvictor extends Thread {

  private final NHttpClientConnectionManager connMgr;

  private volatile boolean shutdown;

  public IdleConnectionEvictor(NHttpClientConnectionManager connMgr) {
    super();
    this.connMgr = connMgr;
  }

  @Override
  public void run() {
    try {
      while (!shutdown) {
        synchronized (this) {
          wait(5000);
          // Close expired connections
          connMgr.closeExpiredConnections();
          // Optionally, close connections
          // that have been idle longer than 5 sec
          connMgr.closeIdleConnections(5, TimeUnit.SECONDS);
        }
      }
    } catch (InterruptedException ex) {
      // terminate
    }
  }

  public void shutdown() {
    shutdown = true;
    synchronized (this) {
      notifyAll();
    }
  }

}