package com.example.weatherapk;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class WeatherHttpClient {

	private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
	private static String IMG_URL = "http://openweathermap.org/img/w/";

	
	public String getWeatherData(String location) {
		InputStream is=null;
		String json="";
		String stringURL=location;
		
		try{
			DefaultHttpClient httpClient=new DefaultHttpClient();
			HttpPost httpPost=new HttpPost(stringURL);
			HttpResponse httpResponse=httpClient.execute(httpPost);
			HttpEntity httpEntity =httpResponse.getEntity();
			is=httpEntity.getContent();
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
			e.toString();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			e.toString();
		} catch (IOException e) {
			e.printStackTrace();
			e.toString();
		}
		
		try{
			BufferedReader reader=new BufferedReader(new InputStreamReader(is,"UTF-8"));
			StringBuilder sb=new StringBuilder();
			String line=null;
			while((line=reader.readLine())!=null){
				sb.append(line);
			}
			is.close();
			json=sb.toString();
			return json;
		} catch (Exception e){
			Log.e("Buffer Error", "Eror converting result" + e.toString());
			return e.toString();
		}
				
	}
	
	public byte[] getImage(String code) {
		HttpURLConnection con = null ;
		InputStream is = null;
		try {
			con = (HttpURLConnection) ( new URL(IMG_URL + code)).openConnection();
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();
			
			// Let's read the response
			is = con.getInputStream();
			byte[] buffer = new byte[1024];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			while ( is.read(buffer) != -1)
				baos.write(buffer);
			
			return baos.toByteArray();
	    }
		catch(Throwable t) {
			t.printStackTrace();
		}
		finally {
			try { is.close(); } catch(Throwable t) {}
			try { con.disconnect(); } catch(Throwable t) {}
		}
		
		return null;
		
	}
}
