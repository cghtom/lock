package controller;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import po.Item;
import po.MSG;
import redis.clients.jedis.Jedis;
import service.ItemService;
import service.LockService;

/**
 * 为了模拟高并发，这里用线程池创建1000个线程，抢100张票
 * 为了异步获取线程的执行结果，这里使用了future模式
 * @author shenzhanwang 
 *
 */
@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	@Autowired
	LockService lockService;
	
	ThreadPoolExecutor es = new ThreadPoolExecutor(100, 200, 0L, TimeUnit.SECONDS,new LinkedBlockingQueue());
	
	// 不加锁，演示超卖
	@RequestMapping(value="/withoutLock",method = RequestMethod.GET)
	@ResponseBody
	public MSG withoutLock() throws Exception{
		// 保存1000个线程的返回值
		Future<Integer>[] futures=new Future[1000];
		for ( int i = 0; i <1000; i++) {
			// Callable任务可以拿到一个Future对象作为返回结果,run方法没有返回值
			 futures[i] = es.submit(new Callable<Integer>() {
					@Override
					public Integer call() throws Exception {
						int result=itemService.updateItemUnlock();
						return result;
					}  
					});
		}
		// 统计结果
		int success = 0;
		for (int j=0;j<1000;j++){
			if(futures[j].get()==1){
				success++;
			}
		}
		// 此处线程池内所有任务已异步执行完毕
		Item rest=itemService.getItemById((long)1);
		return  new MSG("有"+success+"人抢到票",rest);
	}
	
	// redis锁，演示不超卖。100人全部抢到票
	@RequestMapping(value = "/pessLock", method = RequestMethod.GET)
	@ResponseBody
	public MSG pessLock() throws Exception {
		// 保存1000个线程的返回值
		Future<Integer>[] futures = new Future[1000];
		for (int i = 0; i < 1000; i++) {
			// Callable任务可以拿到一个Future对象作为返回结果,run方法没有返回值
			futures[i] = es.submit(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					Jedis conn = new Jedis("localhost");
					String identifier = lockService.acquireLockWithTimeout(conn, "mylock", 2000, 1000);
					if ( identifier != null ) {
						System.out.println(identifier);
						int result = itemService.updateItemUnlock();
						lockService.releaseLock(conn, "mylock", identifier);
						return result;
					} else {
						return 0;
					}
					
				}
			});
		}
		// 统计结果
		int success = 0;
		for (int j = 0; j < 1000; j++) {
			if (futures[j].get() == 1) {
				success++;
			}
		}
		// 此处线程池内所有任务已异步执行完毕
		Item rest = itemService.getItemById((long) 1);
		return new MSG("有" + success + "人抢到票", rest);
	}
	


}
