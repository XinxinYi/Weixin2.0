package com.weixin.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.weixin.user.User;
import com.weixin.util.ConfigUtil;
/*
 * 从数据库查找用户数据
 */
public class SqlConn {				
	
	private Connection conn = null;  
    private Statement stmt = null; 
    
	public void connSQL(){
		
		
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
		    ResultSet rs = stmt.executeQuery("select * from weixin_users");
		     
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
			this.connSQL();
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
			this.connSQL();
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
			this.connSQL();
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
    		this.connSQL();
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
    		this.connSQL();
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
    	this.connSQL();		
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
    	this.connSQL();		
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(update);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
    	this.deconnSQL();
    }
     
}
