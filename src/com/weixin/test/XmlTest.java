package com.weixin.test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//����һ��DocumentBuilderFactory�Ķ���
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			//����һ��DocumentBuilder�Ķ���
			DocumentBuilder db = dbf.newDocumentBuilder();
			//ͨ��DocumentBuilder�����parse��������xml�ļ�����ǰ��Ŀ��
			Document document = db.parse("xmlToken.xml");
			//��ȡAccessToken��expiresIn
			//String accessToken = document.getElementsByTagName("AccessToken").item(0).getTextContent();
			//String expiresIn = document.getElementsByTagName("AccessExpires").item(0).getTextContent();
			
			//ʱ���ʽ��
			SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd"); 
			//��ȡ����ʱ���ת��ΪDATE����
			Date historyTime = time.parse("2016-03-23 12:09:00");
			
			Date nowTime = time.parse("2016-03-25 19:00:00");;
			//time.format(nowTime);
			long taget = (historyTime.getTime() - nowTime.getTime()) / (1000 * 60 * 60 * 24) ;
			System.out.println(taget);
			//���ڼ��㷽��
			GregorianCalendar gc=new GregorianCalendar(); 						
			gc.setTime(historyTime);
			//��Чʱ��Ϊ7200��
			gc.add(7, 1);			
			
			//int taget = time.format(gc.getTime()) - time.parse("2016-10-11").getTime();
						
			
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
		
	}

}
