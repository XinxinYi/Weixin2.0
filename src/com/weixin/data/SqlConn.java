package com.weixin.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.weixin.user.User;
import com.weixin.util.ConfigUtil;
/*
 * �����ݿ�����û�����
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
			
		    Class.forName("com.mysql.jdbc.Driver");     //����MYSQL JDBC��������   
		    conn = DriverManager.getConnection(CONN_URL,USERNAME,PASSWORD);
		    //����URLΪ   jdbc:mysql//��������ַ/���ݿ���  �������2�������ֱ��ǵ�½�û���������
		      
		    //System.out.println("Success connect Mysql server!");
		    stmt = conn.createStatement();
		    ResultSet rs = stmt.executeQuery("select * from weixin_users");
		     
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
			this.connSQL();
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
			this.connSQL();
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
    //�������н�����ǩ�����û�ͷ����Ϣ������ǩ��������ͷ��չʾ
    public String[] selectAllSign(){    	
    	String selectAll = "select * from weixin_users where todaySign = 'true' order by lastSignTime desc";
    	String selectCount = "select count(*)  as countSign from (select * from weixin_users where todaySign = 'true' order by lastSignTime desc) as signCount";
    	try {	
    		this.connSQL();
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
    		this.connSQL();
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
    //�����ݿ�������todaySignֵ��Ϊfalse��ÿ���賿����
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
