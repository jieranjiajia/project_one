package org.study.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * 实现多线程下载.
 * 原理是:将文件分段切割.分段下载.分段保存
 *--分段切割用到HttpUrlConnection对象的setRequestProperty("Range", "bytes=" + start + "-" + end)方法。--
 *--上面的SetRange方法好像并不可行.-- 
 *--分段保存用到RandomAccessFile的seek(int start)方法。但是这个方法下在的文件打不开.尤其是zip的文件
 * @author hp
 *
 */
public class DownFile extends Thread {

	private HttpURLConnection con;       //下载文件的URL连接
	
	
	public DownFile(String url, long startPosition, long endPosition) {
		try {
			this.con = HttpGet.getHttpURLConnection(url);
			setDownInfoRange(startPosition, endPosition);
			System.out.println(con.getResponseCode());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	/**
	 * 利用线程的run方法写出文件
	 */
	@Override
	public void run() {
		long leng = this.con.getContentLengthLong();
		System.out.println("分段下载的长度"+leng);
		try {
			InputStream in = con.getInputStream();
			int available = in.available();
			System.out.println("获得输入流的长度"+available);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeDataIntoFile(byte[] buf, int startPosition, int length) {
		
	}

	public void setDownInfoRange(long startPosition, long endPosition) {
		System.out.println("RANGE bytes=" + startPosition + "-" + endPosition );
		this.con.setRequestProperty("RANGE", "bytes="+startPosition + "-" + endPosition);
	}

	
	public void prinConnection(HttpURLConnection con) {
		Map<String, List<String>> headerFields = con.getRequestProperties();
		for(Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
			System.out.print("key : " + entry.getKey());
			System.out.println("---->  value : " + entry.getValue());
		}
	}
	
}
