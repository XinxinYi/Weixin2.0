package com.weixin.test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;

import com.weixin.po.Article;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import com.weixin.util.WeixinUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test {

	public static void main(String[] args) throws ClientProtocolException, IOException, ParseException {
		WeixinUtil.getAllMaterial();
		WeixinUtil.getImgMaterial();

	}
	
	public static String  getMany() throws ClientProtocolException, IOException, ParseException{
		//获取公众号中所有图文素材
    	int offset = 0;
    	int count = 2;
    	JSONObject jsonObject = WeixinUtil.getAllMaterial();
    	int total_count = jsonObject.getInt("total_count");
    	
    	//获取图文素材的item
    	JSONArray jsonArray = jsonObject.getJSONArray("item");   
    	
    	System.out.println("此处应该添加一次！");
    	while((offset+count) < total_count){
    		
    		System.out.println("增加前：offset:"+offset+",  count:"+count+",  total_count:"+total_count);
    		offset = offset+count;
    		JSONArray jsonArray2= WeixinUtil.getAllMaterial().getJSONArray("item");
    		//System.out.println("jsonArray2:"+jsonArray2.toString());
    		System.out.println("此处应该添加一次！");
    		
    		System.out.println("增加后：offset:"+offset+",  count:"+count+",  total_count:"+total_count);
    		//System.out.println("spe:"+spe);
    	}
    	    	//jsonArray = jsonObject.fromObject(spe).toJSONArray(null);
    	return null;
	}
	
	public static void insert() throws ParseException{
		
		
		int offset = 0;
		int count = 10;
		String jsonStr = "{\"type\":\"image\",\"offset\":" + offset + ",\"count\":" + count + "}";
		String url = WeixinUtil.GET_ALL_MATERIAL.replace("ACCESS_TOKEN", WeixinUtil.getExitAccessToken().getToken()); 
		JSONObject jsonObject = WeixinUtil.doPostStr(url,jsonStr);
		
		int total_count = jsonObject.getInt("total_count");
		int item_count = jsonObject.getInt("item_count");
		
		JSONArray jsonArray = jsonObject.getJSONArray("item");
		
		for(int i=0;i<item_count;i++){
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");			
			Date data_update_time = sdf.parse(sdf.format(new Date()));			
			Long timeStamp = jsonArray.getJSONObject(i).getLong("update_time")*1000;			
			Date update_time = sdf.parse(sdf.format(timeStamp));
			
			if(!update_time.before(data_update_time)){
				System.out.println("图文消息比现在时间更往后");
				
				System.out.println("第"+i+"篇图文：");
				System.out.println("data_update_time:" + data_update_time);					
				System.out.println("update_time:" + update_time);
				System.out.println("************************");
			}				
		}				
	}
	
	public static void getNews() throws ClientProtocolException, IOException, ParseException{
		// TODO Auto-generated method stub
				JSONObject jsonObject = WeixinUtil.getAllMaterial();
				System.out.println(jsonObject);
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
