package mapper;

import po.Item;

public interface ItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Item record);

    int insertSelective(Item record);

    Item selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Item record);

    // 乐观锁
    int updateItemCasLock(Item record);
    // 悲观锁
    Item getItemWithLock();
    // 行级锁
    int updateByRowLock();
}