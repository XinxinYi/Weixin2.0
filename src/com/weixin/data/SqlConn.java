package com.weixin.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;

import com.weixin.po.Article;
import com.weixin.po.Image;
import com.weixin.po.Material;
import com.weixin.user.User;
import com.weixin.util.ConfigUtil;
import com.weixin.util.WeixinUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/*
 * �����ݿ�����û�����
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
			
		    Class.forName("com.mysql.jdbc.Driver");     //����MYSQL JDBC��������   
		    conn = DriverManager.getConnection(CONN_URL,USERNAME,PASSWORD);
		    //����URLΪ   jdbc:mysql//��������ַ/���ݿ���  �������2�������ֱ��ǵ�½�û���������
		      
		    //System.out.println("Success connect Mysql server!");
		    stmt = conn.createStatement();
		    ResultSet rs = stmt.executeQuery("select * from " + tableName);
		     
		    //user Ϊ��������
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
            System.out.println("�ر����ݿ����� ��");  
            e.printStackTrace();  
        }  
     }
     	  
     //����һ���û����û���עʱ����
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
    //�����û���Ϣ���û�ǩ��ʱ����
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
   //���ҵ����û�  
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
    //�������н�����ǩ�����û�ͷ����Ϣ������ǩ��������ͷ��չʾ
    public String[] selectAllSign(){    	
    	String selectAll = "select * from weixin_users where todaySign = 'true' order by lastSignTime desc";
    	String selectCount = "select count(*)  as countSign from (select * from weixin_users where todaySign = 'true' order by lastSignTime desc) as signCount";
    	try {	
    		this.connSQL("weixin_users");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(selectAll);
			//��ȡ����������Ŀ����������ǩ��������
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
    
    //��ȡ�����û��Ļ������У�����ֵΪ��ά���飬�����û���openid,�ǳƣ�ͷ�񣬻�����Ϣ��
    public String[][] getPointsOrder(){
    	String selectAll = "select * from weixin_users order by points desc,lastSignTime desc";
    	String selectCount = "select count(*)  as userCounts from (select * from weixin_users order by points desc) as userCounts";
    	try {	
    		this.connSQL("weixin_users");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(selectAll);
			//��ȡ����������Ŀ����������ǩ��������
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
    //��ȡ�����û��ڻ������а������
    public int getOrder(String openId){
    	String[][] pointsOrder = this.getPointsOrder();
    	int i=0;
    	for(i=0;i<pointsOrder.length;i++){
    		if(pointsOrder[i][0].equals(openId))
    			break;
    	}
    	
    	return ++i;
    }
    //ɾ���û���Ϣ���û�ȡ����עʱ����
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
    //�����ݿ�������todaySignֵ��Ϊfalse��ÿ���賿����
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
     * �����ǹ����زĵ����ݿ����
     * 
     */
    //��ͼ���زĲ������ݿ���
    public void insertMaterial(Material material){
    	String insert = "insert into weixin_materials" + " values('"+ material.getMedia_id()+"','"+ material.getUpdate_time()+"','" +material.getArt_count()+"','" +material.getTitle()+"','"+material.getDescription()+"','"+material.getPicUrl()+"','"+material.getUrl()+"','"+ material.getContent_source_url()+"')";
    	    	
    	System.out.println(insert);
    	try {	
			this.connSQL("weixin_materials");
			stmt = conn.createStatement();
			stmt.executeUpdate(insert);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}       	                 
    	this.deconnSQL(); 
    	
    }
    
  //��ͼ���زĲ������ݿ���
    public void insertImage(Image image){
    	String insert = "insert into weixin_images" + " values('"+ image.getMediaId()+"','"+ image.getName()+"','" +image.getUpdate_time()+"','" +image.getUrl()+"')";   	    	
    	System.out.println(insert);
    	try {	
			this.connSQL("weixin_images");
			stmt = conn.createStatement();
			stmt.executeUpdate(insert);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}       	                 
    	this.deconnSQL(); 
    	
    }
    
    //����ͼƬ�زı��е����ݣ�ͨ��media_id�ҵ���Ӧ��url
    public String selectImgUrl(String media_id){
    	String select = "select * from weixin_images where media_id = '"+media_id+"'";
    	System.out.println(select);
    	String url = null;
    	try {	
			this.connSQL("weixin_images");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(select);
			while (rs.next()) {	
				url = rs.getString("url");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}       	                 
    	this.deconnSQL();
    	return url;
    }
    
    
    //ͨ���ؼ��֣��������ݱ��е�ͼ���زĵ�id
    public ArrayList selectMateId(String keyWord){
    	String selectId = "select * from weixin_materials where digest like '%"+keyWord+"%'";
    	System.out.println("selectId: "+selectId);
    	
    	ArrayList newsList = new ArrayList<Article>();   	
    	try {	
			this.connSQL("weixin_materials");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(selectId);					
			String media_id = "";
			while (rs.next()) {	
				media_id = rs.getString("media_id");
				System.out.println("media_id: "+ media_id);
			}
			String selectNews = "select * from weixin_materials where media_id = '"+media_id+"'";
			System.out.println("selectNews: " + selectNews);
			ResultSet rs2 = stmt.executeQuery(selectNews);
			while (rs2.next()){
				Article article = new Article();
				article.setTitle(rs2.getString("title"));
				article.setDescription(rs2.getString("digest"));
				article.setPicUrl(rs2.getString("thumb_media_url"));
				article.setUrl(rs2.getString("url"));	
				newsList.add(article);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}       	                 
    	this.deconnSQL();
    	System.out.println("newsList.size:"+newsList.size());
    	return newsList;
    }
    
    //ͨ��ͼ���زĵ�id���ҵ���ͼ�ĵ���������
   
}
