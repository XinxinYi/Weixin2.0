package com.weixin.test;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import com.weixin.util.WeixinUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		JSONObject jsonObject = WeixinUtil.getAllMaterial();
		int count = jsonObject.getInt("item_count");
		JSONArray jsonArray = jsonObject.getJSONArray("item");
		//System.out.println(jsonArray.toJSONObject(jsonArray));
		
		ArrayList newsList = new ArrayList<String[]>();
		String[] article = new String[4];
		for(int i=0;i<count;i++){
			JSONObject news = jsonArray.getJSONObject(i);
			JSONArray articles = news.getJSONObject("content").getJSONArray("news_item");
			String firstTitle = articles.getJSONObject(0).getString("title");
			if(firstTitle.indexOf("测试") >= 0 ){
				for(int j=0;j<articles.size();j++){		
					article[0] = articles.getJSONObject(j).getString("title");
					article[1] = articles.getJSONObject(j).getString("digest");
					article[2] = articles.getJSONObject(j).getString("thumb_media_id");
					article[3] = articles.getJSONObject(j).getString("url");																	
					newsList.add(article);					
				}
				//找到一个匹配的图文消息，即退出。
				break;
			}			
		}
			
		System.out.println(newsList);
	}

}
