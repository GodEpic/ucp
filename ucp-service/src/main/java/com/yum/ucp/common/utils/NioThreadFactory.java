package com.yum.ucp.common.utils;

import java.util.concurrent.ThreadFactory;

/**
 * Created by David.Wu on 2015/10/20.
 */
public class NioThreadFactory implements ThreadFactory {

 final static NioThreadFactory nioThreadFactory = new NioThreadFactory();

  public static NioThreadFactory getInstance(){
    return nioThreadFactory;
  }

  private int i = 0;

  @Override
  public Thread newThread(Runnable r) {
    Thread thread = new Thread(r);
    thread.setName("Nio request thread "  + i);

    i++;
    return thread;
  }
}
