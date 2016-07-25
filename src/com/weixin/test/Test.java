package com.weixin.test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;

import com.weixin.po.Article;
import com.weixin.util.WeixinUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		try {
			insert();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		getNews();

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
	
	public static void getImgM() throws ClientProtocolException, IOException{
		JSONObject jsonObject = WeixinUtil.getImgMaterial();
		System.out.println(jsonObject.toString());
		int count = jsonObject.getInt("item_count");
		System.out.println("count"+ count);
		
		JSONArray jsonArray = jsonObject.getJSONArray("item");
		System.out.println(jsonArray.getJSONObject(0).getString("url"));
		
		String picurl = jsonArray.getJSONObject(0).getString("media_id");
		
		String media_id = "KW8gv0lZHIccoL1zL3B1Re4LVVC4nWxB443epAW6wGs";
		
		if(media_id.equals(picurl)){
			System.out.println("匹配成功！！！！");
		}
		
		
		String url = WeixinUtil.getImgUrl(media_id);
		System.out.println(url);
	}
	
	
	
	public static void getImg() throws ClientProtocolException, IOException{
		ArrayList newsList = new ArrayList<Article>();
		newsList = WeixinUtil.getNews("尼玛");
		Article article = new Article();
		
		article = (Article) newsList.get(0);

		String thumb_media_id = article.getPicUrl();
		
		String url1 = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=ACCESS_TOKEN";
		String po = "{\"media_id\":MEDIA_ID}";
		String url2 = url1.replace("ACCESS_TOKEN", WeixinUtil.getExitAccessToken().getToken());
		String po2 = po.replace("MEDIA_ID",thumb_media_id);
		JSONObject jsonObject = WeixinUtil.doPostStr(url2,po2);
		System.out.println(url2);
		System.out.println(po2);
		System.out.println(jsonObject.toString());

		
		
		System.out.println(article.getTitle());
	}
	
	
	public static void getNews() throws ClientProtocolException, IOException{
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
