package com.lytx.webservice.batch;

import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

public class EventQueue {
	private int maxQueueSize;
	private LinkedList queueData;
	private Object mutex;
	private static Logger iLog = Logger.getLogger(EventQueue.class);
	private static Date lastdate;;

	public EventQueue() {
		maxQueueSize = 0;
		queueData = new LinkedList();
		mutex = this;
	}

	public EventQueue(int maxSize) {
		maxQueueSize = 0;
		queueData = new LinkedList();
		maxQueueSize = maxSize;
		mutex = this;
	}

	public int size() {
		int i;
		synchronized (mutex) {
			i = queueData.size();
		}

		return i;
	}

	public boolean isEmpty() {
		boolean flag;
		synchronized (mutex) {
			flag = queueData.isEmpty();
		}
		return flag;
	}

	public Object dequeue() {
		Object obj1;
		synchronized (mutex) {
			Object first = null;
			if (size() > 0)
				first = queueData.removeFirst();
			obj1 = first;
		}

		return obj1;
	}

	public void clearqueue() {
		synchronized (mutex) {
			queueData.clear();
		}

	}

	public Object dequeue(Object obj) {
		Object found = null;
		synchronized (mutex) {
			if (size() > 0)
				found = queueData.remove();
		}
		return found;
	}

	public void enqueue(Object obj) throws IndexOutOfBoundsException {
		synchronized (mutex) {
			if (maxQueueSize > 0 && size() >= maxQueueSize) {
				//iLog.error("MainThread:"+MainThread.runtimes);
				throw new IndexOutOfBoundsException("Queue is full. Element not added.");
			}

			queueData.add(obj);
		}
	}

}
