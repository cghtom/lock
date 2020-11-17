package service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import service.LockService;

@Service
public class LockServiceImpl implements LockService {
	 public String acquireLockWithTimeout(
		        Jedis conn, String lockName, long acquireTimeout, long lockTimeout)
		    {
		        String identifier = UUID.randomUUID().toString();
		        String lockKey = "lock:" + lockName;
		        // 持有锁的超时时间1秒
		        int lockExpire = (int)(lockTimeout / 1000);
		        // 计算申请锁超时的时刻
		        long end = System.currentTimeMillis() + acquireTimeout;
		        // 只要还不超时就循环申请
		        while (System.currentTimeMillis() < end) {
		        	// 将setnx操作和设置过期时间合并到一个命令，保持加锁的原子化，提高可靠性
		        	String result = conn.set(lockKey, identifier, "NX", "PX", lockTimeout);
		        	if (result != null && result.equalsIgnoreCase("OK")) {
		            	// 加锁成功
		                return identifier;
		            }
		            // ttl当 key 存在但没有设置剩余生存时间时，返回 -1 
		            if (conn.ttl(lockKey) == -1) {
		            	// 没设置过期时间就设置一下
		                conn.expire(lockKey, lockExpire);
		            }

		            try {
		                Thread.sleep(1);
		            }catch(InterruptedException ie){
		                Thread.currentThread().interrupt();
		            }
		        }

		        // null indicates that the lock was not acquired
		        return null;
		    }

		    public boolean releaseLock(Jedis conn, String lockName, String identifier) {
		        String lockKey = "lock:" + lockName;

		        while (true){
		        	// 监控lockKey，被修改事务提交失败
		            conn.watch(lockKey);
		            if (identifier.equals(conn.get(lockKey))){
		                Transaction trans = conn.multi();
		                trans.del(lockKey);
		                List<Object> results = trans.exec();
		                if (results == null){
		                    continue;
		                }
		                System.out.println("解锁成功");
		                // 解锁成功
		                return true;
		            }

		            conn.unwatch();
		            break;
		        }

		        return false;
		    }
}
