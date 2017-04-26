package org.study.util;

import java.util.UUID;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 字符工具类
 *
 */
/**
 * @author hp
 *
 */
public class StringUtil {

	
	
	/** 判断一个字符串是不是为空: 不为空返回true   为空返回false
	 * @param src
	 * @return
	 */
	public static boolean isNotBlank(String src) {
		if(null == src || "".equals(src.trim())) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断一个字符是不为空: 为空返回true  不为空返回false
	 * @param src
	 * @return
	 */
	public static boolean isBlank(String src) {
		return !isNotBlank(src);
	}
	
	/**
	 * 获取带中划线连接点UUID字符串
	 * @return
	 */
	public static String getUUID() {
		String string = UUID.randomUUID().toString();
		return string;
	}
	/**
	 * 获取不带中划线的UUID字符串
	 * @return
	 */
	public static String getUUIDByNoStrike() {
		String string = getUUID();
		return string.replaceAll("-", "");
	}
	
	public static void main(String[] args) {
		String uuid = getUUIDByNoStrike();
		System.out.println(uuid);
	}
	
	/**
	 * 获取默认的拼音格式化模版
	 * @return
	 */
	public static HanyuPinyinOutputFormat getDefaultPinYinFormat() {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);              //小写
		format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);         //显示声调符号
		format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);       //当声调显示为符号时。VCharType只能设置为UNICODE编码
		return format;
	}
	
	
	/**
     * @param src
     *            目标字符串
     * @return 解析后的汉语拼音
     */
	public static String getPinYin(String src) {
		String strCopy = null != src ? src : "";
		char[] charArr = strCopy.toCharArray();
		StringBuffer sbf = new StringBuffer();      //连接拼音字符
		try {
			for (int i = 0, k = charArr.length; i < k; i++) {
				// 判断是不是中文
				if (Character.toString(charArr[i]).matches("[\\u4E00-\\u9FA5]+")) {
					String[] str = PinyinHelper.toHanyuPinyinStringArray(charArr[i], getDefaultPinYinFormat());
					StringBuffer ssbf = new StringBuffer();
					int len = null != str ? str.length : 0;
					if (len > 1) {    
						//len大于1说明是多音字
						for (int j = 0; j<len-1; j++) {
							ssbf.append(str[j] + "/");
						}
						sbf.append(" " + ssbf.toString() + str[len - 1]);
					} else if(len == 1) {
						sbf.append(" " + str[0]);
					} else {
						//没有解析出来用 -- 代替
						sbf.append(" --");
					}
				} else {
					sbf.append(Character.toString(charArr[i]));
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return sbf.toString();
	}
}
