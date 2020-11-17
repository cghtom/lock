package service;


import po.Item;

public interface ItemService {
	
	Item getItemById(Long id);
	/**
	 * 不加锁直接扣库存，演示第二类丢失更新，出现超卖
	 * @return
	 */
	int updateItemUnlock();
	/**
	 * 使用乐观锁更新库存
	 * @return
	 */
	int updateItemCasLock();
	/**
	 * 使用悲观锁更新库存
	 */
	int updateItemPessLock();
	/**
	 * 使用行级锁更新库存
	 * @return
	 */
	int updateItemRowLock();
}
