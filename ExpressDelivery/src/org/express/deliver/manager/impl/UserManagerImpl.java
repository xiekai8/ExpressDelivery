package org.express.deliver.manager.impl;

import java.util.List;

import org.express.deliver.dao.IUserDAO;
import org.express.deliver.manager.IUserManager;
import org.express.deliver.pojo.User;

public class UserManagerImpl implements IUserManager {
	private IUserDAO  userDAO;
	
	public IUserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(IUserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public User login(String userName, String password) {
		return userDAO.login(userName, password);
	}

	@Override
	public List<User> queryUserByPaging(int pageNo, int pageSize, String keyword) {
		// TODO Auto-generated method stub
		return userDAO.queryUserByPaging(pageNo, pageSize, keyword);
	}

	@Override
	public void addUser(User user) {
		userDAO.addUser(user);
		
	}

}