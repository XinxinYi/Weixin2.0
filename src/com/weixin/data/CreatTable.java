package com.weixin.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.weixin.util.ConfigUtil;
/*
 * �����洢��Ϣ���û����
 */
public class CreatTable {
	
	//��ȡ�����ļ���������Ŀλ��ʱ��Ҫ�޸�
<<<<<<< HEAD
	public static String configUrl = "../../workspace/Weixin/WebContent/WEB-INF/config.properties";	
=======
	public static String configUrl = "../../workspace/Weixin2.0/WebContent/WEB-INF/config.properties";	
>>>>>>> input keyword,output news

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Class.forName("com.mysql.jdbc.Driver");     //����MYSQL JDBC��������   
			//Class.forName("org.gjt.mm.mysql.Driver");
			System.out.println("Success loading Mysql Driver!");
			}catch (Exception e) {
				System.out.print("Error loading Mysql Driver!");
				e.printStackTrace();
			}
			try {								
				ConfigUtil cu = new ConfigUtil(configUrl);
				String CONN_URL = cu.getValue("dbUrl");
				String USERNAME = cu.getValue("dbUserName");
				String PASSWORD = cu.getValue("dbPassword");	
				
				Connection connect = DriverManager.getConnection(
						CONN_URL,USERNAME,PASSWORD);
				           //����URLΪ   jdbc:mysql//��������ַ/���ݿ���  �������2�������ֱ��ǵ�½�û���������
				      String sql = "CREATE TABLE weixin_users (openid varchar(100) not null unique, nickname varchar(20), sex int null, language varchar(20),city varchar(20),province varchar(20),country varchar(20),headimgurl varchar(200),subscribe_time varchar(20),unionid varchar(20),remark varchar(20),groupid varchar(20),lastSignTime varchar(20),signCount int, signAllCount int ,todaySign varchar(20),points int) ENGINE = MyISAM  DEFAULT CHARSET = utf8;";
				      Statement stmt = connect.prepareStatement(sql);
				      stmt.execute(sql);
				      //ResultSet rs = stmt.executeQuery("select * from weixin_users");					     					      
					   /*   
					while (rs.next()) {
					        System.out.println("openid:" + rs.getString("openid") + "  lastSignTime:" + rs.getString("lastSignTime") + "  SignCount: "+rs.getString("signCount") + "  SignAllCount:" + rs.getString("signAllCount") +  "   todaySign: "+rs.getString("todaySign"));
					        System.out.println(rs.getString("headimgurl"));
					        System.out.println("points:" + rs.getString("points"));
					}*/
					    }
					    catch (Exception e) {
					      System.out.print("get data error!");
					      e.printStackTrace();
					    }
					  

}


}



