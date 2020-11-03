package wiki.IceCream.yuq.demo.dao;

import com.icecreamqaq.yudb.YuDao;
import com.icecreamqaq.yudb.jpa.annotation.Dao;
import com.icecreamqaq.yudb.jpa.hibernate.HibernateDao;
import wiki.IceCream.yuq.demo.entity.QqGroup;

@Dao
public interface QQGroupDao extends YuDao<QqGroup,Integer> {

    public QqGroup findByGroupId(long id);

}
