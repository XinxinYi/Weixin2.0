package com.weixin.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import com.weixin.data.SqlConn;
import com.weixin.po.Article;
import com.weixin.user.User;
import com.weixin.util.CheckUtil;
import com.weixin.util.MessageUtil;
import com.weixin.util.WeixinUtil; 
/*
 * ΢�ŵ������
 */
public class WeixinServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException{
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		
		PrintWriter out = resp.getWriter();
		if(CheckUtil.checkSignature(signature, timestamp, nonce)){
			out.print(echostr);				
		}		
		
	}
	protected void doPost(HttpServletRequest req,HttpServletResponse resp)
		throws ServletException,IOException{
		
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		try{
			Map<String,String> map = MessageUtil.xmlToMap(req);
			String toUserName = map.get("ToUserName");
			String fromUserName = map.get("FromUserName");
			String creatTime = map.get("CreateTime ");
			String msgType = map.get("MsgType");
			String content = map.get("Content");
			String msgId = map.get("MsgId ");
			
			String message = null;
			if(MessageUtil.MESSAGE_TEXT.equals(msgType)){				
				//�û������κ��ı���Ϣ����ظ���Ǹ����
					//String[] news = newsMap.get("��Ӱ");
					System.out.println("������weixinServlet()���û�������ǣ�"+content);
					SqlConn sql = new SqlConn();
					ArrayList newsList = new ArrayList<Article>();
					newsList = sql.selectMateId(content);
					if(newsList.size() == 0){
						message = MessageUtil.initText(toUserName, fromUserName, "��Ǹ��û���ҵ�������ݣ�");
					}else{
						message = MessageUtil.abstractNewsMessage(toUserName, fromUserName, newsList);
					}

			
				
			}else if(MessageUtil.MESSAGE_EVENT.equals(msgType)){
				String eventType = map.get("Event");			
				if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
					//�û���עʱ���ظ���ӭ���ݣ������û��������ݿ�
					String nickName = WeixinUtil.getUser(fromUserName).getNickname();
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.subscribeText(nickName));						 
					User user = new User();
					user = WeixinUtil.getUser(fromUserName);											
					SqlConn sc = new SqlConn();
					sc.insertUser(user);
					
				}else if(MessageUtil.MESSAGE_CLICK.equals(eventType)){
					String key = map.get("EventKey");
					if(key.equals("21_qiandao")){
						//message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
						User user = new User();
						SqlConn sc = new SqlConn();
						user = sc.selectUser(fromUserName);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						
						if(user.isTodaySign()){
							//������ǩ��
							message = MessageUtil.initText(toUserName, fromUserName, "��������ǩ����");	
						}else {
							if(WeixinUtil.isYtdaySign(user.getLastSignTime(),sdf.format(new Date()))){
								//����ǩ��
								user.setSignCount(user.getSignCount()+1);								
								//message = MessageUtil.initText(toUserName, fromUserName, "����ǩ��"+user.getSignCount()+"��");								
							}else{
								//û������ǩ��
								user.setSignCount(1);							
								//message = MessageUtil.initText(toUserName, fromUserName, "������û��ǩ������������ǩ��1�죡");
							}
							user.setTodaySign(true);
							user.setSignAllCount(user.getSignAllCount()+1);
							user.setLastSignTime(sdf.format(new Date()));
							user.setPoints(user.getPoints() + WeixinUtil.getPoints(user.getSignCount()));
							System.out.println("����points=" + WeixinUtil.getPoints(user.getSignCount()));
							sc.updateUser(user);
							message = MessageUtil.signNewsMessage(toUserName, fromUserName);
						}							
						
						
					}else if(key.equals("31_saoma")){
						//message = MessageUtil.initText(toUserName, fromUserName, "ɨ��ɹ�");						
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.sorryText());
					}	
				}else if(MessageUtil.MESSAGE_UNSUBSCRIBE.equals(eventType)){
					SqlConn sc = new SqlConn();
					sc.deleteUser(fromUserName);
				}else if(MessageUtil.MESSAGE_VIEW.equals(eventType)){
					String url = map.get("EventKey");
					message = MessageUtil.initText(toUserName, fromUserName, url);
				}else if(MessageUtil.MESSAGE_SCAN.equals(eventType)){
					String key= map.get("EventKey");
					message = MessageUtil.initText(toUserName, fromUserName,key);
				}				
			}else if(MessageUtil.MESSAGE_LOCATION.equals(msgType)){
				String Label = map.get("Label");
				message = MessageUtil.initText(toUserName, fromUserName,Label);
			}
			
			System.out.println(message);
			out.print(message);
			
		}catch(DocumentException e){
			e.printStackTrace();
		}finally{
			out.close();
		}
}
}
