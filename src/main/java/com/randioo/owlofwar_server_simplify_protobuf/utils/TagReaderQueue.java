package com.randioo.owlofwar_server_simplify_protobuf.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

public class TagReaderQueue<T> {
	private LinkedBlockingQueue<T> writeQueue = new LinkedBlockingQueue<>();
	private Vector<T> hasReadList = new Vector<>();

	public void add(T t) {
		writeQueue.offer(t);
	}

	public List<T> getAll() {
		List<T> readyList = new ArrayList<>();
		writeQueue.drainTo(readyList);
		for (T t : readyList) {
			hasReadList.add(t);
		}
		return readyList;
	}

	public T get() {
		T t = writeQueue.poll();
		if (t != null) {
			hasReadList.add(t);
		}
		return t;
	}

	public void clear() {
		writeQueue.clear();
		hasReadList.clear();
	}

	public List<T> getHasReadList() {
		return hasReadList;
	}

}
