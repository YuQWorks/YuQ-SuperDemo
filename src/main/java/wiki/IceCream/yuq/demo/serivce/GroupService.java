package wiki.IceCream.yuq.demo.serivce;

import com.icecreamqaq.yudb.jpa.annotation.Transactional;
import wiki.IceCream.yuq.demo.dao.QQGroupDao;
import wiki.IceCream.yuq.demo.entity.QqGroup;

import javax.inject.Inject;

public class GroupService {

    @Inject
    private QQGroupDao dao;

    @Transactional
    public QqGroup getGroupByGroupId(long id){
        return dao.findByGroupId(id);
    }

    @Transactional
    public void save(QqGroup group){
        dao.save(group);
    }
    @Transactional
    public void update(QqGroup group){
        dao.update(group);
    }

}
