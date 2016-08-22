package com.weixin.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.weixin.data.SqlConn;
import com.weixin.util.WeixinUtil;
/*
 * 定时更新数据库中的todaySign值，执行时间为每日23：59：59
 */
public class TimeUpdate {

	static int count = 0;
	           
	public static void showTimer() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// task to run goes here  
				SqlConn sc = new SqlConn();
				sc.updateTodaySign();
				
				try {
					WeixinUtil.getAllMaterial();
					WeixinUtil.getImgMaterial();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				++count;
				System.out.println("时间=" + new Date() + " 执行了" + count + "次"); // 1次
			}
		};

		//设置执行时间
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);//每天
		//定制每天的21:09:00执行，
		calendar.set(year, month, day, 16, 47, 59);
		Date date = calendar.getTime();
		Timer timer = new Timer();
		System.out.println(date);
		               
		//int period = 2 * 1000;
		//每天的date时刻执行task，每隔2秒重复执行
		//timer.schedule(task, date, period);
		//每天的date时刻执行task, 仅执行一次
		timer.schedule(task, date);
	}

	
	public static void main(String[] args) {
	     showTimer();
		
	}
	       
}


