package com.utils;

import java.io.*;
import java.net.*;
import android.util.*;

public class NetHelper {
	
	//Class Method - 인자로 받은 주소값으로 접속해 결과값을 리턴
	public static String DownloadHtml(String addr) {
		Log.i("my", "addr : " + addr);
		StringBuilder sbHtml = new StringBuilder();
		try {
			URL url = new URL(addr);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			if(conn != null){
				conn.setConnectTimeout(5000);
				conn.setUseCaches(false);
				if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
					BufferedReader brReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					for(;;){
						String line = brReader.readLine();
						if (line == null) {
							break;
						}
						sbHtml.append(line + '\n');
					}
					brReader.close();
				}
				conn.disconnect();
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("my", "downloadhtml err"+e);
		}
		return sbHtml.toString();
	}
	public static String UploadHtml(String addr, String data){
		try {
			URL url = new URL(addr);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();

			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			conn.setRequestMethod("POST");
			conn.setDoInput( true );
			conn.setDoOutput( true );
			conn.setUseCaches( false );
			conn.setFixedLengthStreamingMode(data.getBytes().length);
//			conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.connect();

			OutputStream os = new BufferedOutputStream(conn.getOutputStream());
			os.write(data.getBytes());
			os.flush();
			os.close();
			
			int responseCode = conn.getResponseCode();
			String responseMessage = conn.getResponseMessage();
			
			Log.i("my", "ret code -> " + responseCode + " " + responseMessage);
			
			conn.disconnect();

		} catch (Exception e) {
			Log.i("my", "UploadHtml err" + e);
		} finally{
		}

		return null;
	}
}
