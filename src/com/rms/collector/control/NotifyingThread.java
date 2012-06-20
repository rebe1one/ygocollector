package com.rms.collector.control;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class NotifyingThread implements Runnable {
	  private final Set<ThreadCompleteListener> listeners
	                   = new CopyOnWriteArraySet<ThreadCompleteListener>();
	  public final NotifyingThread addListener(final ThreadCompleteListener listener) {
	    listeners.add(listener);
	    return this;
	  }
	  public final NotifyingThread removeListener(final ThreadCompleteListener listener) {
	    listeners.remove(listener);
	    return this;
	  }
	  private final void notifyListeners() {
	    for (ThreadCompleteListener listener : listeners) {
	      listener.notifyOfThreadComplete(this);
	    }
	  }
	  @Override
	  public final void run() {
	    try {
	      doRun();
	    } finally {
	      notifyListeners();
	    }
	  }
	  public abstract void doRun();
	}