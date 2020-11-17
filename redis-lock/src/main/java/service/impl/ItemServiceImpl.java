package service.impl;

import java.util.List;

import mapper.ItemMapper;
import po.Item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import service.ItemService;

@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,timeout=5)
@Service("itemService")
public class ItemServiceImpl implements ItemService{
	@Autowired
	public ItemMapper itemMapper;

	@Override
	public int updateItemUnlock() {
		//先读取库存，扣减再更新
		Item item=itemMapper.selectByPrimaryKey((long)1);
		System.out.println(item);
		Integer number=item.getNumber()-1;
		item.setNumber(number);
		int result=itemMapper.updateByPrimaryKeySelective(item);
		System.out.println(result);
		return result;
	}

	@Override
	public int updateItemCasLock() {
		//先查询库存和版本号，再扣减，更新，每个扣减一次版本号加1
		Item item=itemMapper.selectByPrimaryKey((long)1);
		Integer number=item.getNumber()-1;
		item.setNumber(number);
		return itemMapper.updateItemCasLock(item);
	}

	@Override
	public int updateItemPessLock() {
		//查询时添加排它锁，扣减库存更新进去
		Item item = itemMapper.getItemWithLock();
		Integer number = item.getNumber()-1;
		item.setNumber(number);
		return itemMapper.updateByPrimaryKeySelective(item);
	}

	@Override
	public int updateItemRowLock() {
		return itemMapper.updateByRowLock();
	}

	@Override
	public Item getItemById(Long id) {
		return itemMapper.selectByPrimaryKey(id);
	}
	
	
}
