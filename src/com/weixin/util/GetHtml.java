package com.weixin.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/*
 * 获取html里的标题和图片
 */
public class GetHtml {
	
	
	
	public static String[] getHtml(String url) throws IOException{
		String[] title_img = new String[2];						
		// 从 URL 直接加载 HTML 文档
        Document doc = Jsoup.connect(url).get();       
        
        Elements ListDiv = doc.getElementsByAttributeValue("id","activity-name");
        
        title_img[0] = ListDiv.html();
        
        Elements ListDiv2 = doc.getElementsByAttributeValue("id","js_content");
       // System.out.println(ListDiv2.html());    
       
        for (Element element :ListDiv2) {
            Elements links = element.getElementsByTag("img");
            //System.out.println(links);
            
            for (Element link : links) {        	               	
<<<<<<< HEAD
            	String linkHref = link.attr("data-src");	          

=======
<<<<<<< HEAD
            	String linkHref = link.attr("data-src");	          
=======
            	String linkHref = link.attr("src");	          
>>>>>>> input keyword,output news
>>>>>>> 1ebf37a98216d387fbec25e64a94ad5d1a7e5d51
	            if(linkHref.endsWith("jpeg")){
	            	title_img[1] = linkHref;
	            		break;
	            	}           	            
            }
        }                  
        return title_img;
	}
	
	
}
