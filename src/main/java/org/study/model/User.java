package org.study.model;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String userId;
	private String userName;
	private String password;
	
	private Date birthDay;
	
	public User(){}
	
	public User(String userId, String userName, String password, Date date) {
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.birthDay = date;
	}
	
	@JSONField(name="ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public String toString() {
		return JSON.toJSONString(this);
	}
	
	
	public static void main(String[] args) {
		User user = new User("1321","zadg","afesd", new Date());
		System.out.println(user);
	}
}
