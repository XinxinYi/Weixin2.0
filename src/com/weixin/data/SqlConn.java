package com.weixin.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;

import com.weixin.po.Material;
import com.weixin.user.User;
import com.weixin.util.ConfigUtil;
import com.weixin.util.WeixinUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/*
 * 从数据库查找用户数据
 */
public class SqlConn {				
	
	private Connection conn = null;  
    private Statement stmt = null; 
    
	public void connSQL(String tableName){
		
		
		try {						
			//String userDir = System.getProperty("user.dir");
			//System.out.println(userDir);
			
			ConfigUtil cu = new ConfigUtil(CreatTable.configUrl);
			String CONN_URL = cu.getValue("dbUrl");
			String USERNAME = cu.getValue("dbUserName");
			String PASSWORD = cu.getValue("dbPassword");			
			
		    Class.forName("com.mysql.jdbc.Driver");     //加载MYSQL JDBC驱动程序   
		    conn = DriverManager.getConnection(CONN_URL,USERNAME,PASSWORD);
		    //连接URL为   jdbc:mysql//服务器地址/数据库名  ，后面的2个参数分别是登陆用户名和密码
		      
		    //System.out.println("Success connect Mysql server!");
		    stmt = conn.createStatement();
		    ResultSet rs = stmt.executeQuery("select * from " + tableName);
		     
		    //user 为你表的名称
		    }catch (Exception e) {
		      System.out.print("get data error!");
		      e.printStackTrace();
		    } 
		  }
	
	// disconnect to MySQL  
     void deconnSQL() {  
        try {  
            if (conn != null)  
                conn.close();  
        } catch (Exception e) {  
            System.out.println("关闭数据库问题 ：");  
            e.printStackTrace();  
        }  
     }
     	  
     //新增一个用户，用户关注时调用
    public void insertUser(User user){  	
    	String insert = "insert into weixin_users" + " values('"+user.getOpenid()+"','"+user.getNickname()+"','"+user.getSex()+"','"+user.getLanguage()+"','"+user.getCity()+"','"+user.getProvince()+"','"+ user.getCountry()+"','"+user.getHeadimgurl()+"','"+user.getSubscribe_time()+"','"+user.getUnionid()+"','"+user.getRemark()+"','"+user.getGroupid()+"','"+user.getLastSignTime()+"','"+user.getSignCount()+"','" + user.getSignAllCount() + "','" +user.isTodaySign()+ "','" +user.getPoints() +"')";
    	
    	System.out.println(insert);
    	try {	
			this.connSQL("weixin_users");
			stmt = conn.createStatement();
			stmt.executeUpdate(insert);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}       	                 
    	this.deconnSQL(); 
    }  
    //更新用户信息，用户签到时调用
    public void updateUser(User user){
    	
    	String update = "UPDATE weixin_users set headimgurl='" +user.getHeadimgurl()+"',lastSignTime ='"+user.getLastSignTime()+"', signCount='"+user.getSignCount()+ "', signAllCount='" +user.getSignAllCount() + "', todaySign='"+user.isTodaySign()+ "', points='"+user.getPoints() +"' where openid='"+user.getOpenid()+"'";    	
    	System.out.println(update);
    	try {	
			this.connSQL("weixin_users");
			stmt = conn.createStatement();
			stmt.executeUpdate(update);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	this.deconnSQL();
    }
   //查找单个用户  
    public User selectUser(String openId){
    	String select = "select * from weixin_users where openid = '" +openId + "'";
    	User user = new User();
    	try {	
			this.connSQL("weixin_users");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(select);
			while (rs.next()) {	
				user.setOpenid(rs.getString("openid"));
				user.setNickname(rs.getString("nickname"));
				user.setSex(rs.getInt("sex"));
				user.setLanguage(rs.getString("language"));
				user.setCity(rs.getString("city"));
				user.setProvince(rs.getString("province"));
				user.setHeadimgurl(rs.getString("headimgurl"));
				user.setSubscribe_time(rs.getString("subscribe_time"));
				user.setUnionid(rs.getString("unionid"));
				user.setRemark(rs.getString("remark"));
				
				user.setLastSignTime(rs.getString("lastSignTime"));
				user.setSignCount(rs.getInt("signCount"));
				user.setSignAllCount(rs.getInt("signAllCount"));
				user.setTodaySign(rs.getBoolean("todaySign"));
				user.setPoints(rs.getInt("points"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	this.deconnSQL();
    	return user;
    }
    //查找所有今日已签到的用户头像信息，用于签到人数及头像展示
    public String[] selectAllSign(){    	
    	String selectAll = "select * from weixin_users where todaySign = 'true' order by lastSignTime desc";
    	String selectCount = "select count(*)  as countSign from (select * from weixin_users where todaySign = 'true' order by lastSignTime desc) as signCount";
    	try {	
    		this.connSQL("weixin_users");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(selectAll);
			//获取检索到的条目数，即今日签到总人数
			int length = 0;
			Statement stmt2 = conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery(selectCount);
			while(rs2.next()){
				length = rs2.getInt("countSign");
			}
			
			//System.out.println(length);
			String[] allHead = new String[length];
			int tmp = 0;
				while(rs.next()){
					allHead[tmp] = rs.getString("headimgurl");
					tmp++;					
				}	
							
			this.deconnSQL();
	    	return allHead;
    	}		
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
			}  
    	return null;
    	}
    
    //获取所有用户的积分排行，返回值为二维数组，包括用户的openid,昵称，头像，积分信息。
    public String[][] getPointsOrder(){
    	String selectAll = "select * from weixin_users order by points desc,lastSignTime desc";
    	String selectCount = "select count(*)  as userCounts from (select * from weixin_users order by points desc) as userCounts";
    	try {	
    		this.connSQL("weixin_users");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(selectAll);
			//获取检索到的条目数，即今日签到总人数
			int length = 0;
			Statement stmt2 = conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery(selectCount);
			while(rs2.next()){
				length = rs2.getInt("userCounts");
			}
			
			//System.out.println(length);
			String[][] allUsers = new String[length][4];
			int tmp = 0;
				while(rs.next()){
					allUsers[tmp][0] = rs.getString("openid");
					allUsers[tmp][1] = rs.getString("nickname");
					allUsers[tmp][2] = rs.getString("headimgurl");
					allUsers[tmp][3] = rs.getString("points");
					tmp++;					
				}	
							
			this.deconnSQL();
	    	return allUsers;
	    	}		
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();			
				}  
	    return null;
	    	
    }
    //获取单个用户在积分排行榜的名次
    public int getOrder(String openId){
    	String[][] pointsOrder = this.getPointsOrder();
    	int i=0;
    	for(i=0;i<pointsOrder.length;i++){
    		if(pointsOrder[i][0].equals(openId))
    			break;
    	}
    	
    	return ++i;
    }
    //删除用户信息，用户取消关注时调用
    public void deleteUser(String openId){
    	String delete = "delete from weixin_users where openid= '" + openId +"'";      	
    	this.connSQL("weixin_users");		
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(delete);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
    	this.deconnSQL();
    }
    //将数据库里所有todaySign值置为false，每日凌晨调用
    public void updateTodaySign(){
    	String update = "update weixin_users set todaySign = 'false'";
    	this.connSQL("weixin_users");		
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(update);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
    	this.deconnSQL();
    }
     
    /*
     * 下面是关于素材的数据库管理
     * 
     */
    public void addAllMaterial() throws ClientProtocolException, IOException, ParseException{
    	//获取公众号中所有图文素材
    	JSONObject jsonObject = WeixinUtil.getAllMaterial();
    	//获取图文素材的个数
    	int count = jsonObject.getInt("item_count");
    	//获取图文素材的item
    	JSONArray jsonArray = jsonObject.getJSONArray("item");
    	//先遍历所有的图文
    	for(int i=0;i<count;i++){
    		Material material = new Material();
    		material.setMedia_id(jsonArray.getJSONObject(i).getString("media_id"));
    		material.setUpdate_time(jsonArray.getJSONObject(i).getString("update_time"));    		
    		JSONArray art_arr = jsonArray.getJSONObject(i).getJSONArray("news_item");
			int art_length = art_arr.size();
			for(int j=0;j<art_length;j++){			
	    		material.setTitle(art_arr.getJSONObject(j).getString("title"));
				material.setDescription(art_arr.getJSONObject(j).getString("digest"));
				String thumb_url = art_arr.getJSONObject(j).getString("thumb_url");
				if("".equals(thumb_url)){
					String thumb_media_id = art_arr.getJSONObject(j).getString("thumb_media_id");
					
				}							
			}		
    	}	   	
    }
    
    //添加最新的图文素材到
    public void addMaterial() throws ClientProtocolException, IOException, ParseException{
    	//获取公众号中所有图文素材
    	JSONObject jsonObject = WeixinUtil.getAllMaterial();
    	//获取图文素材的个数
    	int count = jsonObject.getInt("item_count");
    	//获取图文素材的item
    	JSONArray jsonArray = jsonObject.getJSONArray("item");   	
    	//先遍历所有的图文
    	for(int i=0;i<count;i++){
    		String media_id = jsonArray.getJSONObject(i).getString("media_id");
    		String update_time = jsonArray.getJSONObject(i).getString("update_time");
    		
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");			
			Date now_time = sdf.parse(sdf.format(new Date()));			
			Long timeStamp = jsonArray.getJSONObject(i).getLong("update_time")*1000;			
			Date data_time = sdf.parse(sdf.format(timeStamp));
			
			if(!data_time.before(now_time)){
				JSONArray art_arr = jsonArray.getJSONObject(i).getJSONArray("news_item");
				int art_length = art_arr.size();
				for(int j=0;j<art_length;j++){
					String title =  art_arr.getJSONObject(j).getString("title");
					String digest = art_arr.getJSONObject(j).getString("digest");
					
					
					String thumb_url = art_arr.getJSONObject(j).getString("thumb_url");
					if("".equals(thumb_url)){
						String thumb_media_id = art_arr.getJSONObject(j).getString("thumb_media_id");
						thumb_url = SqlConn.getImgUrl(thumb_media_id);
						
						
					}
					String url = art_arr.getJSONObject(j).getString("url");
					
				}
				System.out.println("图文消息比现在时间更往后");				
			}
    	}
		
    }
    
    
    /*
	 * 获取图片素材列表
	 */
	public static JSONObject getImgMaterial() throws ClientProtocolException, IOException {		
		String url = WeixinUtil.GET_ALL_MATERIAL.replace("ACCESS_TOKEN", WeixinUtil.getExitAccessToken().getToken());
		
		
		int offset = 0;
		int count = 10;
		String jsonStr = "{\"type\":\"image\",\"offset\":0,\"count\":20}";
		JSONObject jsonObject = WeixinUtil.doPostStr(url,jsonStr);
		
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
	
    
    
    //将图文素材插入数据库中
    public void insertMaterial(){
    	String insert = "insert into weixin_users" + " values('"+user.getOpenid()+"','"+user.getNickname()+"','"+user.getSex()+"','"+user.getLanguage()+"','"+user.getCity()+"','"+user.getProvince()+"','"+ user.getCountry()+"','"+user.getHeadimgurl()+"','"+user.getSubscribe_time()+"','"+user.getUnionid()+"','"+user.getRemark()+"','"+user.getGroupid()+"','"+user.getLastSignTime()+"','"+user.getSignCount()+"','" + user.getSignAllCount() + "','" +user.isTodaySign()+ "','" +user.getPoints() +"')";
    	
    	System.out.println(insert);
    	try {	
			this.connSQL("weixin_users");
			stmt = conn.createStatement();
			stmt.executeUpdate(insert);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}       	                 
    	this.deconnSQL(); 
    	
    }
    public void updateMaterial(){
    	
    }
}
