package service;

import redis.clients.jedis.Jedis;

public interface LockService {
	
	public String acquireLockWithTimeout(Jedis conn, String lockName, long acquireTimeout, long lockTimeout);
	
	public boolean releaseLock(Jedis conn, String lockName, String identifier);
}
