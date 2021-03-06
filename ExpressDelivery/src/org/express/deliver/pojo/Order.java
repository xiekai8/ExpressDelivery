package org.express.deliver.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 订单实体类
 * @author 梁城月
 *
 */
@Entity
@Table(name="preOrder")
public class Order {
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(length = 50)
	private String id;
	/**
	 * 
	 * 下单时间
	 */
	@Column(nullable = false)
	private Date preOrderDate;
	/**
	 * 订单状态״̬
	 */
	@Column(length = 50, nullable = false)
	private String state;
	/**
	 * 接单时间
	 */
	@Column(nullable = false)
	private Date takeOrderDate;
	/**
	 * 接单用户是否删除
	 */
	private boolean takeIsdel=false;
	/**
	 * 发单用户是否删除
	 */
	private boolean peoIsdel=false;
	/**
	 * 订单结束时间
	 */
	private Date orderEndDate;
	/**
	 * 备注
	 */
	@Column(length = 500)
	private String remarks;
	/**
	 * 取件时间
	 */
	private String takeDate;
	/**
	 * 快递名称
	 */
	private String name;
	/**
	 * 取件地址
	 */
	private String takeaddress;
	/**
	 * 送达地址
	 */
	private String preaddress;
	/**
	 * 手机号码
	 */
	private String teltPhone;
	/**
	 * 积分
	 */
	private int grade;
	/**
	 * 下单用户
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "preOrderUserId")
	private User preOrderuUser;
	/**
	 * 接单用户
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "takeOrderUserId")
	private User takeOrderUser;
	/**
	 * 评论列表
	 */
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("id ASC")
	private Set<Comment> comments = new HashSet<Comment>();
	/**
	 * 货物列表
	 */
	/*@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("id ASC")
	private Set<Goods> goods = new HashSet<Goods>();*/

	/**
	 * 获取ID
	 * 
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 *设置id
	 * 
	 * @param id
	 *            id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取下单日期
	 * 
	 * @return 下单日期
	 */
	public Date getPreOrderDate() {
		return preOrderDate;
	}

	/**
	 * 设置下单日期
	 * 
	 * @param preOrderDate
	 *            下单日期
	 */
	public void setPreOrderDate(Date preOrderDate) {
		this.preOrderDate = preOrderDate;
	}

	/**
	 * 获取订单状态
	 * 
	 * @return 订单状态
	 */
	public String getState() {
		return state;
	}

	/**
	 * 设置订单状态
	 * 
	 * @param state 订单状态
	 *            
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * 获取接单时间
	 * 
	 * @return 接单时间
	 */
	public Date getTakeOrderDate() {
		return takeOrderDate;
	}

	/**
	 * 设置接单时间
	 * 
	 * @param takeOrderDate
	 *            接单时间
	 */
	public void setTakeOrderDate(Date takeOrderDate) {
		this.takeOrderDate = takeOrderDate;
	}

	/**
	 * 获取订单结束时间
	 * 
	 * @return 订单结束时间
	 */
	public Date getOrderEndDate() {
		return orderEndDate;
	}

	/**
	 * 设置订单结束时间
	 * 
	 * @param orderEndDate 订单结束时间
	 */
	public void setOrderEndDate(Date orderEndDate) {
		this.orderEndDate = orderEndDate;
	}

	/**
	 * 获取备注
	 * 
	 * @return 备注
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * 设置备注
	 * 
	 * @param remarks
	 *           备注
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * 获取下单用户
	 * 
	 * @return 下单用户
	 */
	@JsonIgnore
	public User getPreOrderuUser() {
		return preOrderuUser;
	}

	/**
	 * 设置下单用户
	 * 
	 * @param preOrderuUser
	 *            下单用户
	 */
	public void setPreOrderuUser(User preOrderuUser) {
		this.preOrderuUser = preOrderuUser;
	}

	/**
	 * 获取接单用户
	 * 
	 * @return 接单用户
	 */
	@JsonIgnore
	public User getTakeOrderUser() {
		return takeOrderUser;
	}

	/**
	 * 设置下单用户
	 * 
	 * @param takeOrderUser
	 *           下单用户
	 */
	public void setTakeOrderUser(User takeOrderUser) {
		this.takeOrderUser = takeOrderUser;
	}

	/**
	 * 设置评论列表
	 * 
	 * @return 评论列表
	 */
	@JsonIgnore
	public Set<Comment> getComments() {
		return comments;
	}

	/**
	 * 设置评论列表
	 * 
	 * @param comments
	 *            评论列表
	 */
	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	/**
	 * 获取货物列表
	 * 
	 * @return 货物列表
	 */
	/*@JsonIgnore
	public Set<Goods> getGoods() {
		return goods;
	}*/

	/**
	 * 设置货物列表
	 * 
	 * @param goods
	 *            货物列表
	 */
	/*public void setGoods(Set<Goods> goods) {
		this.goods = goods;
	}*/
	/**
	 * 获取取货时间
	 * @return 取货时间
	 */
	public String getTakeDate() {
		return takeDate;
	}
	/**
	 * 设置取货时间
	 * @param takeDate 取货时间
	 */
	public void setTakeDate(String takeDate) {
		this.takeDate = takeDate;
	}
	/**
	 * 获取快递名称 
	 * @return 快递名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置快递名称
	 * @param name 快递名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取取货地址
	 * @return
	 */
	public String getTakeaddress() {
		return takeaddress;
	}
	/**
	 * 设置取货地址
	 * @param takeaddress 取货地址
	 */
	public void setTakeaddress(String takeaddress) {
		this.takeaddress = takeaddress;
	}
	/**
	 * 获取送达地址
	 * @return 送达地址
	 */
	public String getPreaddress() {
		return preaddress;
	}
	/**
	 * 设置送达地址
	 * @param preaddresString 送达地址
	 */
	public void setPreaddress(String preaddress) {
		this.preaddress = preaddress;
	}
	/**
	 * 获取积分
	 * @return 积分
	 */
	public int getGrade() {
		return grade;
	}
	/**
	 * 设置积分
	 * @param grade 积分
	 */
	public void setGrade(int grade) {
		this.grade = grade;
	}
	/**
	 * 获取手机号码
	 * @return 手机号码
	 */

	public String getTeltPhone() {
		return teltPhone;
	}
	/**
	 * 设置手机号码
	 * @param teltPhone 手机号码
	 */
	public void setTeltPhone(String teltPhone) {
		this.teltPhone = teltPhone;
	}

	public boolean isTakeIsdel() {
		return takeIsdel;
	}

	public void setTakeIsdel(boolean takeIsdel) {
		this.takeIsdel = takeIsdel;
	}

	public boolean isPeoIsdel() {
		return peoIsdel;
	}

	public void setPeoIsdel(boolean peoIsdel) {
		this.peoIsdel = peoIsdel;
	}

	/**
	 * 将 List<Order>集合转换为json数组
	 * @param oList  List<Order>集合
	 * @return  json数组
	 */
	public static String getOrderListJson(List<Order> oList) {
		StringBuffer sBuffer=new StringBuffer();
		sBuffer.append("[");
		for (Order order : oList) {
			sBuffer.append("{");
			sBuffer.append("\"preOrderUserName\":");
			sBuffer.append("\""+order.getPreOrderuUser().getUserName()+ "\"");
			sBuffer.append(",");
			sBuffer.append("\"id\":");
			sBuffer.append("\""+order.getId()+"\"");
			sBuffer.append(",");
			sBuffer.append("\"orderEndDate\":");
			sBuffer.append("\""+order.getOrderEndDate()+"\"");
			sBuffer.append(",");
			sBuffer.append("\"takeDate\":");
			sBuffer.append("\""+order.getTakeDate()+"\"");
			sBuffer.append(",");
			sBuffer.append("\"preOrderDate\":");
			sBuffer.append("\""+order.getPreOrderDate()+"\"");
			sBuffer.append(",");
			sBuffer.append("\"remarks\":");
			sBuffer.append("\""+order.getRemarks()+"\"");
			sBuffer.append(",");
			sBuffer.append("\"state\":");
			sBuffer.append("\""+order.getState()+"\"");
			sBuffer.append(",");
			sBuffer.append("\"takeOrderDate\":");
			sBuffer.append("\""+order.getTakeOrderDate()+"\"");
			sBuffer.append(",");
			sBuffer.append("\"name\":");
			sBuffer.append("\""+order.getName()+"\"");
			sBuffer.append(",");
			sBuffer.append("\"preaddress\":");
			sBuffer.append("\""+order.getPreaddress()+"\"");
			sBuffer.append(",");
			sBuffer.append("\"takeaddress\":");
			sBuffer.append("\""+order.getTakeaddress()+"\"");
			sBuffer.append(",");
			sBuffer.append("\"teltPhone\":");
			sBuffer.append("\""+order.getTeltPhone()+"\"");
			sBuffer.append(",");
			sBuffer.append("\"grade\":");
			sBuffer.append("\""+order.getGrade()+"\"");
			sBuffer.append(",");
			sBuffer.append("\"takeOrderUserName\":");
			sBuffer.append("\""+"\"");
			sBuffer.append("}");
			sBuffer.append(",");
		}
		sBuffer.replace(sBuffer.length()-1, sBuffer.length(), "");
		sBuffer.append("]");
		return sBuffer.toString();
	}

}
