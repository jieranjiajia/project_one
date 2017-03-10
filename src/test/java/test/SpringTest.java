package test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.study.model.User;
import org.study.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/spring.xml"})
public class SpringTest {
	
	public static final Logger logger = LoggerFactory.getLogger(SpringTest.class);
	
	@Resource
	private UserService userService;
	
	@Test
	public void test() {
		
		User testUser = new User("2","lisi", "lsjoe2131w",new Date());
		userService.put(testUser);
		
	}
	
	@Test
	public void test1() {
		String key = "1";
		User user = userService.getUserByKey(key);
		logger.info("----测试查询user{}",user);
	}

	@Test
	public void test2() {
		List<User> users = new ArrayList<User>(400000);
		for(int i=0; i< 100000;i++) {
			users.add(new User(i+"a","1564", "564165",new Date()));
			users.add(new User(i+"b","lisi644", "1614",new Date()));
			users.add(new User(i+"c","lisi5641564", "1465",new Date()));
			users.add(new User(i+"d","lisi12361", "13656",new Date()));
		}
		userService.putList(users);
	}
	
	@Test
	public void test3() {
		User u = userService.getUsers("users");
		logger.info("----测试查询user{}",u);
	}
}
