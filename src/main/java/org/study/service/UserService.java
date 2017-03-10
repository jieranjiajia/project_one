package org.study.service;

import java.util.List;

import org.study.model.User;

/**
 * 面向接口编程
 * @author ou_qu_sheng
 *
 */
public interface UserService {

	/**
	 * 查询用户
	 * @param key
	 * @return
	 */
	public User getUserByKey(String key);
	
	/**
	 * 添加
	 * @param user
	 * @return
	 */
	public boolean saveUser(User user);
	
	/**
	 * 更新
	 * @param user
	 * @return
	 */
	public boolean updateUser(User user);
	
	/**
	 * 删除
	 * @param user
	 * @return
	 */
	public boolean deleteUser(String key);
	
	//-----------下面的方法是通过redisTemplate进行操作的方法
	public void put(User user);
	
	public User get(User user);
	
	public void delete(User user);
	
	public void update(User user);
	
	public void putList(List<User> users);
	
	public User getUsers(String key);
}
