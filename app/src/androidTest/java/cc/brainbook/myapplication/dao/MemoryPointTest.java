package cc.brainbook.myapplication.dao;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class MemoryPointTest extends AbstractDaoTestLongPk<MemoryPointDao, MemoryPointEntity> {

    public MemoryPointTest() {
        super(MemoryPointDao.class);
    }

    @Override
    protected MemoryPointEntity createEntity(Long key) {
        MemoryPointEntity entity = new MemoryPointEntity();
        entity.setId(key);
        return entity;
    }

}
