package com.example.auctionapp.wxapi.vo;

import java.util.concurrent.ConcurrentHashMap;

public class SemaphoreMap {

	/**
	 * 消息排重的一个线程安全hashmap
	 */
	private static ConcurrentHashMap<String, String> eventMap = new ConcurrentHashMap<String, String>();

	public static ConcurrentHashMap<String, String> getSemaphore() {
		return eventMap;
	}

}
