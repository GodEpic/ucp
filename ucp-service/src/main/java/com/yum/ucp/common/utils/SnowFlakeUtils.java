package com.yum.ucp.common.utils;

import java.text.SimpleDateFormat;

/**
 * 雪花算法 获取ID
 * @author Zachary
 *
 */
public class SnowFlakeUtils {
	// 机器码
	private final long workerId;

	// 数据中心
	private final long datacenterId;

	// 序列号
	private long sequence = 0L;

	// 时间起始标记点，作为基准，一般取系统的最近时间
	private final long twepoch = DateUtils.parseDate("2019-08-15 00:00:00", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).getTime();
	private final long workerIdBits = 5L;
	// 节点ID长度
	private final long datacenterIdBits = 5L;

	// 数据中心ID长度
	private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

	// 最大支持机器节点数0~31，一共32个
	private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

	// 最大支持数据中心节点数0~31，一共32个
	private final long sequenceBits = 12L;

	// 序列号12位
	private final long workerIdShift = sequenceBits;

	// 机器节点左移17位
	private final long datacenterIdShift = sequenceBits + workerIdBits;

	// 数据中心节点左移22位
	private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

	// 时间毫秒数左移22位
	private final long sequenceMask = -1L ^ (-1L << sequenceBits);

	// 最大为4095
	private long lastTimestamp = -1L;

	private SnowFlakeUtils(long workerId, long datacenterId) {
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
		}
		this.workerId = workerId;
		this.datacenterId = datacenterId;
	}

	public synchronized long nextId() {
		long timestamp = timeGen();
		// 获取当前毫秒数
		// 如果服务器时间有问题(时钟后退) 报错。
		if (timestamp < lastTimestamp) {
			throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
		}
		// 如果上次生成时间和当前时间相同,在同一毫秒内
		if (lastTimestamp == timestamp) {
			// sequence自增，因为sequence只有12bit，所以和sequenceMask相与一下，去掉高位
			sequence = (sequence + 1) & sequenceMask;
			// 判断是否溢出,也就是每毫秒内超过4095，当为4096时，与sequenceMask相与，sequence就等于0
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
				// 自旋等待到下一毫秒
			}
		} else {
			sequence = 0L;
			// 如果和上次生成时间不同,重置sequence，就是下一毫秒开始，sequence计数重新从0开始累加
		}
		lastTimestamp = timestamp;

		long longStr = ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
		return longStr;
	}

	private long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	private long timeGen() {
		return SystemClockUtils.now();
	}

	private static SnowFlakeUtils instance = null;

	private static SnowFlakeUtils getInstance() {
		if (instance == null) {
			synchronized (SnowFlakeUtils.class) {
				if (instance == null) {
					instance = new SnowFlakeUtils(getWorkId(), getDataCenterId());
				}
			}
		}
		return instance;
	}

	private static long getWorkId() {
		return 10L;
	}

	private static long getDataCenterId() {
		return 25L;
	}
	
	public static long getNextId(){
		SnowFlakeUtils snowFlake = getInstance();
		return snowFlake.nextId();
	}
}
