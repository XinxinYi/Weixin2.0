package com.weixin.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.weixin.data.CreatTable;
import com.weixin.menu.Button;
import com.weixin.menu.ClickButton;
import com.weixin.menu.Menu;
import com.weixin.menu.ViewButton;
import com.weixin.po.AccessToken;
import com.weixin.po.Article;
import com.weixin.user.User;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class WeixinUtil {
	//appid,appsecret根据微信号更改
	private static final String APPID = "wx3032409d5b572407";
	private static final String APPSECRET = "c1a748f2e51e0c4be7e7eb2f429f8de1";
	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	private static final String GET_USER_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	public static final String GET_ALL_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ACCESS_TOKEN";
	
	//private static final String XML_TOKEN_URL = "../../workspace/Weixin/xmlToken.xml";
	
	
	public static JSONObject doGetStr(String url) throws ClientProtocolException, IOException{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		try{
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				String result = EntityUtils.toString(entity,"UTF-8");
				jsonObject = JSONObject.fromObject(result);
			}
		}catch(ClientProtocolException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return jsonObject;		
	}
	
	public static JSONObject doPostStr(String url,String outStr){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		JSONObject jsonObject = null;
		try{
			httpPost.setEntity(new StringEntity(outStr,"UTF-8"));			
			HttpResponse response = httpClient.execute(httpPost);
			String result = EntityUtils.toString(response.getEntity(),"UTF-8");
			jsonObject = JSONObject.fromObject(result);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return jsonObject;		
	}
	
	/*
	 * 获取access_token
	 */
	public static AccessToken getAccessToken() throws ClientProtocolException, IOException{
		AccessToken token = new AccessToken();		
		String url = WeixinUtil.ACCESS_TOKEN_URL.replace("APPID", WeixinUtil.APPID).replace("APPSECRET",WeixinUtil.APPSECRET);
		JSONObject jsonObject = WeixinUtil.doGetStr(url);
		System.out.println(jsonObject);
		if(jsonObject != null){
			token.setToken(jsonObject.getString("access_token"));
			token.setExpiresIn(jsonObject.getInt("expires_in"));
		}
		return token;
	}
	/*
	 * 避免多次重复从微信服务器获取accessToken，将最新的存储于xmltoken文件中
	 */
	public static AccessToken getExitAccessToken(){
		AccessToken token = new AccessToken();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		//获取当前路径
		//String userDir = System.getProperty("user.dir");
		//System.out.println(userDir);
		ConfigUtil cu = new ConfigUtil(CreatTable.configUrl);
		String XML_TOKEN_URL = cu.getValue("tokenXmlUrl");
		
		try {
			//创建一个DocumentBuilder的对象
			DocumentBuilder db = dbf.newDocumentBuilder();
			//通过DocumentBuilder对象的parse方法加载xml文件到当前项目下
			Document document = db.parse(XML_TOKEN_URL);
			//获取AccessToken和expiresIn
			String accessToken = document.getElementsByTagName("AccessToken").item(0).getTextContent();
			String time = document.getElementsByTagName("Time").item(0).getTextContent();
			int expiresIn = Integer.parseInt(document.getElementsByTagName("ExpiresIn").item(0).getTextContent());
			//时间格式化
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			//将取出的时间戳转换为DATE对象
			Date historyTime = date.parse(time);			
			//日期计算方法
			GregorianCalendar gc = new GregorianCalendar(); 						
			gc.setTime(historyTime);
			//有效时间为7200秒
			gc.add(13, 7200);
			Date nowTime = new Date();
			if(gc.getTime().before(nowTime)){
				//System.out.println("****new****");
				token = WeixinUtil.getAccessToken();
				String nowTimeStr = date.format(nowTime);
				WeixinUtil.updateToken(token, nowTimeStr);				
			}else{
				//System.out.println("****old****");
				token.setToken(accessToken);
				token.setExpiresIn(expiresIn);
			}			
			
		}catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return token;
	}
	/*
	 * 将最新accessToken写入xml文件中
	 */
	public static void updateToken(AccessToken accessToken, String nowTimeStr){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		ConfigUtil cu = new ConfigUtil(CreatTable.configUrl);
		String XML_TOKEN_URL = cu.getValue("tokenXmlUrl");
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(XML_TOKEN_URL);
			document.getElementsByTagName("AccessToken").item(0).setTextContent(accessToken.getToken());
			document.getElementsByTagName("ExpiresIn").item(0).setTextContent(Integer.toString(accessToken.getExpiresIn()));						
			document.getElementsByTagName("Time").item(0).setTextContent(nowTimeStr);
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer former = factory.newTransformer();
			former.transform(new DOMSource(document), new StreamResult(new File(XML_TOKEN_URL)));			
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}
	/*
	 * 文件上传
	 */
	public static String upload(String filePath,String accessToken,String type) throws IOException{
		File file = new File(filePath);
		if(!file.exists() || !file.isFile()){
			throw new IOException("文件不存在！");
		}
		String url = WeixinUtil.UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE",type);
		URL urlObj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		
		//设置请求头信息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		//设置边界
		String BOUNDARY = "-----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data;boundary =" + BOUNDARY);
		
		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");

		byte[] head = sb.toString().getBytes("utf-8");

		//获得输出流
		OutputStream out = new DataOutputStream(con.getOutputStream());
		//输出表头
		out.write(head);

		//文件正文部分
		//把文件已流文件的方式 推入到url中
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		in.close();

		//结尾部分
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");//定义最后数据分隔线

		out.write(foot);
		out.flush();
		out.close();

		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		try {
			//定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		JSONObject jsonObj = JSONObject.fromObject(result);
		System.out.println(jsonObj);
		String typeName = "media_id";
		if(!"image".equals(type)){
			typeName = type + "_media_id";
		}
		String mediaId = jsonObj.getString(typeName);
		return mediaId;
	
	}
	/*
	 * 组装菜单
	 */
	public static Menu initMenu(){
		Menu menu = new Menu();
		
		ViewButton button11 = new ViewButton();
		button11.setName("进入影院");
		button11.setType("view");
		button11.setUrl("http://t.cn/RUp5drF");
		
		ClickButton button21 = new ClickButton();
		button21.setName("签到");
		button21.setType("click");
		button21.setKey("21_qiandao");
		
		//ClickButton button22 = new ClickButton();
		//button22.setName("扫码");
		//button22.setType("click");
		//button22.setKey("31_saoma");
		
		//Button button = new Button();
		//button.setName("菜单");
		//button.setSub_button(new Button[]{button21,button22});
		
		menu.setButton(new Button[]{button11,button21});
		return menu;
	}
	/*
	 * 创建菜单
	 */
	public static int createMenu(String token,String menu) throws ParseException,IOException{
		int result = 0;
		String url = WeixinUtil.CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doPostStr(url,menu);
		if(jsonObject != null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
	/*
	 * 获取用户信息
	 */
	public static User getUser(String fromUserName)throws ClientProtocolException, IOException{
		User ui = new User();
		try{	
			String token = getExitAccessToken().getToken();				
			String url = WeixinUtil.GET_USER_URL.replace("ACCESS_TOKEN", token).replace("OPENID",fromUserName);
			JSONObject jsonObject = WeixinUtil.doGetStr(url);
			if(jsonObject.getInt("subscribe") == 1){
				ui.setOpenid(fromUserName);
				ui.setNickname(jsonObject.getString("nickname"));
				ui.setSex(jsonObject.getInt("sex"));
				ui.setCity(jsonObject.getString("city"));
				ui.setProvince(jsonObject.getString("province"));
				ui.setHeadimgurl(jsonObject.getString("headimgurl"));
				ui.setSubscribe_time(jsonObject.getString("subscribe_time"));
				System.out.println(ui.getSubscribe_time());
			}
			 	
		}catch(Exception e){
			e.printStackTrace();
		}
		return ui;	
	}
	/*
	 * 判断昨天是否签到
	 */
	public static boolean isYtdaySign(String lastSignTime, String newSignTime){
		//时间格式化
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd"); 
		try {
			Date newTime = date.parse(newSignTime);
			Date oldTime = date.parse(lastSignTime);
			long tmp = (newTime.getTime() - oldTime.getTime()) / (1000 * 60 * 60 * 24);;
			if(tmp > 1){
				//当前时间与上次签到时间的日期，相差大于1天，则没有连续签到
				return false;
			}else{
				//昨天已签到
				return true;
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;		
	}
	/*
	 * 判断今天是否签到
	 */
	public static boolean isTodaySign(String lastSignTime, String newSignTime){
		//时间格式化
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd"); 
		try {
			Date newTime = date.parse(newSignTime);
			Date oldTime = date.parse(lastSignTime);
			long tmp = (newTime.getTime() - oldTime.getTime()) / (1000 * 60 * 60 * 24);

			if(tmp == 0){
				//当前时间与上次签到时间的日期，相差大于1天，则没有连续签到
				return true;
			}else{
				//昨天已签到
				return false;
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;		
	}
	/*
	 * 签到积分规则，根据连续签到几天，则相应增加几个积分
	 */
	public static int getPoints(int signCount){
		int points = 0;
		switch(signCount){
		case 1:
			points = 1;
			break;
		case 2:
			points = 2;
			break;
		case 3:
			points = 3;
			break;
		case 4:
			points = 5;
			break;
		default:
			points = 6;
			break;
		}
		return points;
	}
	
	/*
	 * 获取图文素材列表
	 */
	public static JSONObject getAllMaterial() throws ClientProtocolException, IOException {		
		String url = WeixinUtil.GET_ALL_MATERIAL.replace("ACCESS_TOKEN", WeixinUtil.getExitAccessToken().getToken());
		
		String jsonStr = "{\"type\":\"news\",\"offset\":0,\"count\":10}";
		JSONObject jsonObject = doPostStr(url,jsonStr);
		System.out.println("这里是getAllMaterial(),输出："+jsonObject.toString());
		return jsonObject;
	}
	
	/*
	 * 获取图片素材列表
	 */
	public static JSONObject getImgMaterial() throws ClientProtocolException, IOException {		
		String url = WeixinUtil.GET_ALL_MATERIAL.replace("ACCESS_TOKEN", WeixinUtil.getExitAccessToken().getToken());
		
		
		int offset = 0;
		int count = 10;
		String jsonStr = "{\"type\":\"image\",\"offset\":0,\"count\":10}";
		JSONObject jsonObject = doPostStr(url,jsonStr);
		
		int total_count = jsonObject.getInt("total_count");
		int item_count = jsonObject.getInt("item_count");
		
		JSONArray jsonArray = jsonObject.getJSONArray("item");
		
		for(int i=0;i<item_count;i++){
			String update_time = jsonArray.getJSONObject(i).getString("update_time");
			//时间格式化
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
			
		}
		
		
		
		while((offset+10) < total_count){
			offset = offset + count;
			
		}
		System.out.println("这里是getImgMaterial(),输出："+jsonObject.toString());
		return jsonObject;
	}
	/*
	 * 
	 */
	public static String getImgUrl(String thumb_media_id) throws ClientProtocolException, IOException{		
		//获取公众号中所有图文素材
		JSONObject jsonObject = WeixinUtil.getImgMaterial();
		System.out.println("这里是getImgUrl(),输出："+jsonObject.toString());
		//获取图文素材的个数
		int count = jsonObject.getInt("item_count");
		//int count = 1;
		//获取图文素材的item
		JSONArray jsonArray = jsonObject.getJSONArray("item");
		
		for(int i=0;i<count;i++){
			String media_id = jsonArray.getJSONObject(i).getString("media_id");
			System.out.println("media_id：　" + media_id);
			System.out.println("thumb_media_id：　" + thumb_media_id);
			if(media_id.equals(thumb_media_id)){
				String url = jsonArray.getJSONObject(i).getString("url");
				return url;				
			}
		}				
		return null;
	}
	
	/*
	 * 通过关键字匹配查找图文
	 */
	public static ArrayList getNews(String keyWord) throws ClientProtocolException, IOException{		
		//获取公众号中所有图文素材
		JSONObject jsonObject = WeixinUtil.getAllMaterial();
		//获取图文素材的个数
		int count = jsonObject.getInt("item_count");
		//获取图文素材的item
		JSONArray jsonArray = jsonObject.getJSONArray("item");
		//新建一个list，用于存放匹配的图文
		ArrayList newsList = new ArrayList<Article>();
		//先遍历所有的图文
		for(int i=0;i<count;i++){
			
			//按素材的顺序获取每个素材中的具体内容
			JSONArray articles = jsonArray.getJSONObject(i).getJSONObject("content").getJSONArray("news_item");
			//获取单个图文中，第一篇文章的标题
			String firstTitle = articles.getJSONObject(0).getString("digest");
			//如果第一篇文章的标题含有关键字，则匹配成功
			if(firstTitle.indexOf(keyWord) >= 0 ){
				//将匹配成功的图文，存放到List中
				for(int j=0;j<articles.size();j++){	
					Article article = new Article();
					article.setTitle(articles.getJSONObject(j).getString("title"));
					article.setDescription(articles.getJSONObject(j).getString("digest"));
					//article.setPicUrl(articles.getJSONObject(j).getString("thumb_media_id"));
					//找出图片素材中匹配的pic_url
					//String pic_url = WeixinUtil.getImgUrl(articles.getJSONObject(j).getString("thumb_media_id"));
					String pic_url = articles.getJSONObject(j).getString("thumb_url");
					System.out.println(pic_url);
					article.setPicUrl(pic_url);
					article.setUrl(articles.getJSONObject(j).getString("url"));																	
					newsList.add(article);					
				}
				//找到一个匹配的图文消息，即退出。
				break;
			}			
		}
		return newsList;
	}
	

}