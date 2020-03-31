package com.nian.service;

public interface RedisRedPacketService {

	/**
	 * 保存redis抢红包列表
	 * @param redPacketId 抢红包编号
	 * @param unitAmount ????
	 */
	public void saveUserRedPacketByRedis(Long redPacketId, Double unitAmount);
}
