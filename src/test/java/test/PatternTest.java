package test;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
		PatternTest.getLocalIp();
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
	
	public static void getLocalIp() {
		try {
			InetAddress address = InetAddress.getLocalHost();
			String hostAddress = address.getHostAddress();
			String hostName = address.getHostName();
			System.out.println(hostAddress);
			System.out.println(hostName);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
