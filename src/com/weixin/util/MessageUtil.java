package com.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.weixin.data.CreatTable;
import com.weixin.po.Article;
import com.weixin.po.Image;
import com.weixin.po.ImageMessage;
import com.weixin.po.Music;
import com.weixin.po.MusicMessage;
import com.weixin.po.NewsMessage;
import com.weixin.po.TextMessage;


public class MessageUtil {
	public static final String MESSAGE_TEXT = "text";
	public static final String MESSAGE_IMAGE = "image";
	public static final String MESSAGE_VOICE = "voice";
	public static final String MESSAGE_VIDEO = "video";
	public static final String MESSAGE_MUSIC = "music";
	public static final String MESSAGE_NEWS = "news";	
	public static final String MESSAGE_LINK = "link";
	public static final String MESSAGE_LOCATION = "location";
	public static final String MESSAGE_SHORTVIDEW = "shortvideo";
	public static final String MESSAGE_EVENT = "event";
	public static final String MESSAGE_SUBSCRIBE = "subscribe";
	public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
	public static final String MESSAGE_CLICK = "CLICK";
	public static final String MESSAGE_VIEW = "VIEW";
	public static final String MESSAGE_SCAN = "scancode_push";
	
	//private static final String SIGN_URL = "WebContent/SignCount.jsp";
	/*
	 * xmlתΪmap����
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	
	public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException{
		Map<String,String> map = new HashMap<String,String>();
		SAXReader reader = new SAXReader();
		
		InputStream ins = request.getInputStream();
		Document doc = reader.read(ins);
		
		Element root = doc.getRootElement();
		
		List<Element> list = root.elements();
		
		for(Element e:list){
			map.put(e.getName(), e.getText());
		}
		ins.close();
		return map;
	}
	
	/*
	 * ���ı�����ת��Ϊxml
	 */
	public static String textMessageToXml(TextMessage textMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", textMessage.getClass());
		
		return xstream.toXML(textMessage);
	}
	/*
	 * ͼ����Ϣת��ΪXML	
	 */
	public static String newsMessageToXml(NewsMessage newsMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new Article().getClass());
		return xstream.toXML(newsMessage);
	}
	/* 
	 * ͼƬ��Ϣת��ΪXML	
	 */
	public static String imageMessageToXml(ImageMessage imageMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", imageMessage.getClass());
		xstream.alias("image", new Image().getClass());
		return xstream.toXML(imageMessage);
	}
	/* 
	 * ������Ϣת��ΪXML	
	 */
	public static String musicMessageToXml(MusicMessage musicMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", musicMessage.getClass());
		xstream.alias("Music", new Music().getClass());
		return xstream.toXML(musicMessage);
	}
	/*
	 * ƴ���ı���Ϣ
	 */
	public static String initText(String toUserName,String fromUserName,String content){
		TextMessage text = new TextMessage();
		text.setFromUserName(toUserName);
		text.setToUserName(fromUserName);
		text.setMsgType(MessageUtil.MESSAGE_TEXT);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		 
		text.setCreateTime(sdf.format(new Date()));
		text.setContent(content);
		return textMessageToXml(text);
		
	}
	/*
	 * ��עʱ�ظ����ı���Ϣ
	 */
	public static String subscribeText(String nickName){
		StringBuffer sb = new StringBuffer();
		sb.append(nickName);
		sb.append("���ǳ���л���Ĺ�ע��\n");
		sb.append("�����ںű��š�Ϊ������������²��������ľ����ṩ���ྫƷ��Դ��");
		return sb.toString();
	}

	/*
	 * ���˵� 
	 */
	public static String sorryText(){
		StringBuffer sb = new StringBuffer();
		sb.append("��Ǹ����ɱ��ӰԺ���ڽ�����\n");
		sb.append("�ɵ������ӰԺ�ۿ���Ӱ��");
		return sb.toString();
	}

	/*
	 * ǩ��ʱ���ص�ͼ����Ϣ
	 */
	public static String signNewsMessage(String toUserName,String fromUserName){
		String message = null;
		List<Article> articleList = new ArrayList<Article>();
		NewsMessage newsMessage = new NewsMessage();

		ConfigUtil cu = new ConfigUtil(CreatTable.configUrl);
		String signUrl = cu.getValue("signUrl");
		String signImg = cu.getValue("signImg");
		
		Article article = new Article();
		article.setTitle("�򿨳ɹ���");
		article.setDescription("���������ң�������");
		article.setPicUrl(signImg);				
		article.setUrl(signUrl+"?openid="+fromUserName);
		
		articleList.add(article);		
		
		newsMessage.setFromUserName(toUserName);
		newsMessage.setToUserName(fromUserName);
		newsMessage.setArticles(articleList);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		newsMessage.setCreateTime(sdf.format(new Date()));
		newsMessage.setArticleCount(articleList.size());
		newsMessage.setMsgType(MESSAGE_NEWS);
		
		message = MessageUtil.newsMessageToXml(newsMessage);
		return message;
	}
	/*
	 * ͼƬ��Ϣ
	 */
	public static String initImageMessage(String toUserName,String fromUserName){
		String message = null;
		Image image = new Image();
		image.setMediaId("KW8gv0lZHIccoL1zL3B1Re4LVVC4nWxB443epAW6wGs");
		ImageMessage im = new ImageMessage();
		im.setFromUserName(toUserName);
		im.setToUserName(fromUserName);
		im.setCreateTime("2016-3-9");
		im.setMsgType(MessageUtil.MESSAGE_IMAGE);
		im.setImage(image);
		
		message = MessageUtil.imageMessageToXml(im);
		return message;
		
	}
	/*
	 * ��Ƶ��������Ϣ
	 */
	public static String initMusicMessage(String toUserName,String fromUserName){
		String message = null;
		Music music = new Music();
		music.setTitle("test Music");
		music.setDescription("test description");
		music.setThumbMediaId("ABLgKOOwgusANliiMCrOfX55a5VabN0oTdx89MG6E4gCJJAYjrMSvA1PIUwMBmJx");
		music.setMusicUrl("http://tongyuan.tunnel.qydev.com/Weixin/resource/Sleep Away.mp3");
		music.setHQMusicUrl("http://tongyuan.tunnel.qydev.com/Weixin/resource/Sleep Away.mp3");
		
		MusicMessage mm = new MusicMessage();
		mm.setFromUserName(toUserName);
		mm.setToUserName(fromUserName);
		mm.setCreateTime("2016-3-9");
		mm.setMsgType(MessageUtil.MESSAGE_MUSIC);
		mm.setMusic(music);

		message = MessageUtil.musicMessageToXml(mm);
		return message;
		
	}
	
	/*
	 * ���ݹؼ����Զ��ظ�ͼ����Ϣ
	 */
	public static String abstractNewsMessage(String toUserName,String fromUserName, ArrayList newsList){
		String message = null;
		List<Article> articleList = new ArrayList<Article>();
		NewsMessage newsMessage = new NewsMessage();
						
		articleList = newsList;
		newsMessage.setFromUserName(toUserName);
		newsMessage.setToUserName(fromUserName);
		newsMessage.setArticles(articleList);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		newsMessage.setCreateTime(sdf.format(new Date()));
		newsMessage.setArticleCount(articleList.size());
		newsMessage.setMsgType(MESSAGE_NEWS);
		
		message = MessageUtil.newsMessageToXml(newsMessage);
		return message;
	}
	
}
