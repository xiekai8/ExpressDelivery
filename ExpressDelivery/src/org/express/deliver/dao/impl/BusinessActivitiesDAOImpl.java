package org.express.deliver.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.express.deliver.dao.IBusinessActivitiesDAO;
import org.express.deliver.pojo.BusinessActivities;
import org.hibernate.Query;
/**
 * 商家活动DAO操作接口实现类
 * 
 * @author 吴文鑫
 * 
 */
public class BusinessActivitiesDAOImpl extends BaseDAO implements
		IBusinessActivitiesDAO {
	/**
	 * 添加商家活动
	 * 
	 * @param businessActivities 商家活动
	 *            
	 * 
	 */
	@Override
	public void addBusinessActivities(BusinessActivities businessActivities)
			throws Exception {
		sessionFactory.getCurrentSession().save(businessActivities) ;

	}

	/**
	 * 删除商家活动
	 * 
	 * @param businessActivities
	 *            商家活动
	 */
	@Override
	public void delBusinessActivities(BusinessActivities businessActivities)
			throws Exception {
		sessionFactory.getCurrentSession().delete(businessActivities);
	}

	/**
	 * 修改商家活动
	 * 
	 * @param businessActivities
	 *  商家活动
	 */
	@Override
	public void modifyBusinessActivities(BusinessActivities businessActivities)
			throws Exception {
		sessionFactory.getCurrentSession().update(businessActivities);
	}

	/**
	 * 获取商家活动列表
	 * 
	 * @return 商家活动列表
	 */
	@Override
	public List<BusinessActivities> queryAllBusinessActivities()
			throws Exception {
		String hql = "FROM BusinessActivities";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return query.list();
	}

	/**
	 * 根据ID查询商家活动
	 * 
	 * @param businessActivitiesID
	 * 商家活动ID
	 * @return 商家活动
	 */
	@Override
	public BusinessActivities queryBusinessActivitiesByID(
			String businessActivitiesID) throws Exception {

		return (BusinessActivities) sessionFactory.getCurrentSession().get(
				BusinessActivities.class, businessActivitiesID);
	}

	/**
	 * 获取商家活动分页列表
	 * 
	 * @param pageNo 当前页
	 * @param pageSize页面大小
	 * @param Keyword关键字
	 * @return 商家活动分页列表
	 */
	@Override
	public Map<String, Object> queryBusinessActivitiesByPaging(int pageNo,
			int pageSize, String keyword) throws Exception {
		String hql = "FROM BusinessActivities AS b WHERE b.businessName LIKE ? OR "
				+ "b.businessAddress LIKE ? OR b.activeContent LIKE ? ORDER BY b.addDate DESC";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, "%" + keyword + "%");
		query.setString(1, "%" + keyword + "%");
		query.setString(2, "%" + keyword + "%");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("count",query.list().size());
		query.setFirstResult((pageNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		map.put("businessActivities", query.list());
		return map;
	}

	@Override
	public int queryAllBusinessActivitiesAcount() {
		String hql = "SELECT COUNT(b) FROM BusinessActivities AS b";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return ((Long)query.uniqueResult()).intValue();
	}

	@Override
	public List<BusinessActivities> queryBusinessActivities() {
		String hql="FROM BusinessActivities AS b ORDER BY b.activeEndDate DESC";
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		query.setFirstResult(0);
		query.setMaxResults(3);
		return query.list();
	}

}
