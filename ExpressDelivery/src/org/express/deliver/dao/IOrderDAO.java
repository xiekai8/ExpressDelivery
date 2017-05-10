package org.express.deliver.dao;

import java.util.List;

import org.express.deliver.pojo.Order;

/**
 * 订单DAO操作接口
 * @author 吴文鑫
 *
 */
public interface IOrderDAO {
	/**
	 * 添加订单
	 * 
	 * @param order
	 *            订单信息
	 */
	public void addOrder(Order order);

	/**
	 * 删除订单
	 * 
	 * @param order
	 *            订单信息
	 */
	public void delOrder(Order order);

	/**
	 * 修改订单
	 * 
	 * @param order
	 *           订单信息
	 */
	public void modifyOrder(Order order);

	/**
	 * 分页查询订单
	 * @param pageNo 当前页
	 * @param pageSize 页面大小
	 * @param keyword 关键字
	 * @return List 订单信息集合
	 */
	public List<Order> queryOrderByPaging(int pageNo, int pageSize,
			String keyword);
}