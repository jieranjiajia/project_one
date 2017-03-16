package org.study.excel;

import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * 用于excel导出测试的pojo类
 * @author ou_qu_sheng
 *
 */
public class ExcelModel {
	
	@Excel(name="学号")
	private Integer id;
	@Excel(name="姓名", width=30)
	private String userName;
	@Excel(name="性别",replace={"男_0","女_1"},suffix="生")
	private String sex;

	public ExcelModel(){}
	
	public ExcelModel(Integer id, String userName, String sex) {
		this.id = id;
		this.userName = userName;
		this.sex = sex;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}
