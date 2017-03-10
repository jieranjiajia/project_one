package test;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 练习java的正则
 * @author ou_qu_sheng
 *
 */
public class PatternTest {

	
	
	
	
	public static void main(String[] args) {
		PatternTest.checkStament();
	}
	
	/**
	 * 检验IP
	 * @param ip
	 * @return
	 */
	public static boolean checkIp(String ip) {
		String reg1 = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		Pattern ptn = Pattern.compile(reg1);
		return ptn.matcher(ip).matches();
	}
	
	
	public static void checkStament() {
		String str = "温馨提示:尊敬的<%CUSTOMERNAME%>先生/女士,您本期的还款金额为<%TOTAL_O_D_AMT%>元,收款方户名:深圳市佰仟金融服务有限公司,帐号:755920947910920XXXXXX.开户行:招商银行深圳四海支行/安联支行.询电:075584379009.";
		String target = "customername";
		String target1 = "TOTAL_O_D_AMT";
		str = str.replaceAll("(?i)<%"+target+"%>", "替换");
		str = str.replaceAll("(?i)<%"+target1+"%>", "545646.0030");
		System.out.println(str);
	}
	
	//查询出map的结果
	/*	List<Map<String, Object>> result2Map = smsTemplateDao.getResult2Map(sqlContent, null);
		if(null != result2Map && result2Map.size() > 0) {
			Map<String, Object> map = result2Map.get(0);
			for(Map.Entry<String, Object> entry : map.entrySet()) {
				String column = entry.getKey();  //数据查询出来封装的map的键是大写的
				String value = entry.getValue().toString();
				//匹配大写的字符
				if(smsTemplate.indexOf(column) != -1) {
					smsTemplate = smsTemplate.replace("<%"+column+"%>", value);
				} else if (smsTemplate.indexOf(column.toLowerCase()) != -1) {
					smsTemplate = smsTemplate.replace("<%"+column.toLowerCase()+"%>", value);
				} else {
					//都不匹配则逐一替换
					smsTemplate = smsTemplate.replace("<%", "").replace("%>", "").replace(column, value).replace(column.toLowerCase(), value);
				}
			}
		}*/
}
