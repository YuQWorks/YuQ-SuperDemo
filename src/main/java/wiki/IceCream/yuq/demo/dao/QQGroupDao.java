package wiki.IceCream.yuq.demo.dao;

import com.icecreamqaq.yudb.jpa.hibernate.HibernateDao;
import wiki.IceCream.yuq.demo.entity.QqGroup;

public class QQGroupDao extends HibernateDao<QqGroup, Integer> {

    public QqGroup getGroupByGroupId(long id) {
        return search("from QqGroup where groupId=?", id);
    }

}
