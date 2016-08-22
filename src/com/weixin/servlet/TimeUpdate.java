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
 * ��ʱ�������ݿ��е�todaySignֵ��ִ��ʱ��Ϊÿ��23��59��59
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
				System.out.println("ʱ��=" + new Date() + " ִ����" + count + "��"); // 1��
			}
		};

		//����ִ��ʱ��
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);//ÿ��
		//����ÿ���21:09:00ִ�У�
		calendar.set(year, month, day, 16, 47, 59);
		Date date = calendar.getTime();
		Timer timer = new Timer();
		System.out.println(date);
		               
		//int period = 2 * 1000;
		//ÿ���dateʱ��ִ��task��ÿ��2���ظ�ִ��
		//timer.schedule(task, date, period);
		//ÿ���dateʱ��ִ��task, ��ִ��һ��
		timer.schedule(task, date);
	}

	
	public static void main(String[] args) {
	     showTimer();
		
	}
	       
}


