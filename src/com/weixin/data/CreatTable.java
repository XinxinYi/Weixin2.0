package com.weixin.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.weixin.util.ConfigUtil;
/*
 * 创建存储信息的用户表格
 */
public class CreatTable {
	
	//获取配置文件，更改项目位置时需要修改
<<<<<<< HEAD
	public static String configUrl = "../../workspace/Weixin/WebContent/WEB-INF/config.properties";	
=======
	public static String configUrl = "../../workspace/Weixin2.0/WebContent/WEB-INF/config.properties";	
>>>>>>> input keyword,output news

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Class.forName("com.mysql.jdbc.Driver");     //加载MYSQL JDBC驱动程序   
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
				           //连接URL为   jdbc:mysql//服务器地址/数据库名  ，后面的2个参数分别是登陆用户名和密码
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



