package org.express.deliver.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.express.deliver.dao.IGoodsDAO;
import org.express.deliver.pojo.Goods;
import org.hibernate.Query;
/**
 * 货物DAO操作接口实现类
 * @author 吴文鑫
 * 
 */
public class GoodsDAOImpl extends BaseDAO implements IGoodsDAO {

	@Override
	public void addGoods(Goods goods) throws Exception {
		sessionFactory.getCurrentSession().save(goods);

	}

	@Override
	public void delGoods(Goods goods) throws Exception {
		sessionFactory.getCurrentSession().delete(goods);
	}

	@Override
	public void modifyGoods(Goods goods) throws Exception {
		sessionFactory.getCurrentSession().update(goods);

	}

	@Override
	public List<Goods> queryAllGoods() throws Exception {
		String hql = "FROM Goods";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return query.list();
	}

	@Override
	public Goods queryGoodsByID(String goodsID) throws Exception {

		return (Goods) sessionFactory.getCurrentSession().get(Goods.class,
				goodsID);
	}

	@Override
	public Map<String, Object> queryGoodsByPaging(int pageNo, int pageSize,
			String keyword) throws Exception {
		String hql = "FROM Goods AS g WHERE g.orderNumber LIKE ? OR "
				+ "g.takeNo LIKE ? OR g.pickupAddress LIKE ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, "%" + keyword + "%");
		query.setString(1, "%" + keyword + "%");
		query.setString(2, "%" + keyword + "%");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("count", query.list().size());
		// 分页查询
		query.setFirstResult((pageNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		map.put("goodss", query.list());
		return map;
	}

	@Override
	public int queryAllGoodsAcount() {
		String hql = "SELECT COUNT(g) FROM Goods AS g";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return ((Long)query.uniqueResult()).intValue();
	}

}
