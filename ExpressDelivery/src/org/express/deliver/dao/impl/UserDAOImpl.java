package org.express.deliver.dao.impl;

import java.util.List;

import org.express.deliver.dao.IUserDAO;
import org.express.deliver.pojo.User;
import org.hibernate.Query;
/**
 * 用户DAO操作接口实现类
 * @author 吴文鑫
 *
 */
public class UserDAOImpl extends BaseDAO implements IUserDAO {

	@SuppressWarnings("unchecked")
	@Override
	public User login(String userName, String password) {
		String hql="FROM User AS u WHERE u.userName=? AND u.password=?";
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, userName);
		query.setString(1, password);
		List<User> lUsers=query.list();
		if (lUsers!=null&&lUsers.size()>0) {
			return lUsers.get(0);
		}
		return null;
	}

	@Override
	public void addUser(User user) {
		sessionFactory.getCurrentSession().save(user);
		
	}

	@Override
	public void delUser(User user) {
		sessionFactory.getCurrentSession().delete(user);
	}

	@Override
	public void modifyUser(User user) {
		sessionFactory.getCurrentSession().update(user);
		
	}

	@Override
	public List<User> queryUserByPaging(int pageNo, int pageSize, String keyword) {
		String hql="FROM User AS u WHERE u.userName LIKE ? OR u.nickName LIKE ?";
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, "%"+keyword+"%");
		query.setString(1, "%"+keyword+"%");
		query.setFirstResult((pageNo-1)*pageSize);
		query.setMaxResults(pageSize);
		return query.list();
	}

}