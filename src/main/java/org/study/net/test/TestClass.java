package org.study.net.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TestClass {
	
	private String url = "http://localhost:8090/collectsmcf/pages/CsCaseMain/possessionList.do?flowflag=Q";
	
	
	public void doPost() {
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(this.getUrl()).openConnection();
			con.setRequestMethod("GET");  //设置post请求
			con.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0");
			con.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			con.setRequestProperty("Accept-Encoding", "gzip, deflate");
			con.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
			con.setRequestProperty("Keep-Alive", "300");
			con.setRequestProperty("Cache-conntrol", "max-age=0");
			con.setRequestProperty("Referer", "http://www.baidu.com");
			con.setConnectTimeout(10000);
			con.setRequestProperty("Connection", "keep-alive");
			con.setRequestProperty("Accept", "*/*");
			//post请求必须设置如下这两个数据
			con.setDoOutput(true);
			con.setDoInput(true);
			
			//设置请求参数
			PrintWriter writer = new PrintWriter(con.getOutputStream());
			writer.write("idNumber=;truncate table test_tang2;&pageNum=1&numPerPage=30");
			writer.flush();
			writer.close();
			
			FileOutputStream out = new FileOutputStream(new File("f:\\demo.ini"));
			
			System.out.println(con.getResponseCode());
			InputStream in = con.getInputStream();
			byte[] bye = new byte[1024];
			for(int len = -1;(len = in.read(bye))!=-1;) {
				out.write(bye, 0, len);
			}
			out.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}
	
	public static void main(String[] args) {
		TestClass test = new TestClass();
		test.doPost();
	}

}
