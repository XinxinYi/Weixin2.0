package com.weixin.test;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetUrlTest {
    static String url="http://mp.weixin.qq.com/s?__biz=MzA5OTExMDE0NA==&mid=404455968&idx=1&sn=d76353d240b6528b6eb803040b8dfcc1&scene=4#wechat_redirect";
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        BolgBody();
        //test();
        //Blog();
        /*
         * Document doc = Jsoup.connect("http://www.oschina.net/")
         * .data("query", "Java") // ������� .userAgent("I �� m jsoup") // ����
         * User-Agent .cookie("auth", "token") // ���� cookie .timeout(3000) //
         * �������ӳ�ʱʱ�� .post();
         */// ʹ�� POST �������� URL

        /*
         * // ���ļ��м��� HTML �ĵ� File input = new File("D:/test.html"); Document doc
         * = Jsoup.parse(input,"UTF-8","http://www.oschina.net/");
         */
    }

    /**
     * ��ȡָ��HTML �ĵ�ָ����body
     * @throws IOException
     */
    private static void BolgBody() throws IOException {
        /*
    	// ֱ�Ӵ��ַ��������� HTML �ĵ�
        String html = "<html><head><title> ��Դ�й����� </title></head>"
                + "<body><p> ������ jsoup ��Ŀ��������� </p></body></html>";
        Document doc = Jsoup.parse(html);
        System.out.println(doc.body());
        */
        
        // �� URL ֱ�Ӽ��� HTML �ĵ�
        Document doc2 = Jsoup.connect(url).get();
        String title = doc2.body().toString();
        
        Elements ListDiv = doc2.getElementsByAttributeValue("id","activity-name");
        
        
        System.out.println(ListDiv.html());
        
       
        
        System.out.println("************");
        
        Elements ListDiv2 = doc2.getElementsByAttributeValue("id","js_content");
       // System.out.println(ListDiv2.html());
     
       
        for (Element element :ListDiv2) {
            Elements links = element.getElementsByTag("img");
            //System.out.println(links);
            
            for (Element link : links) {        	       
	        	
            	String linkHref = link.attr("data-src");	          
	            if(linkHref.endsWith("jpeg")){
	            	System.out.println(linkHref);	
	            }           	            
            }
        }        
        
    
       
    }

    /**
     * ��ȡ�����ϵ����±��������
     */
    public static void article() {
        Document doc;
        try {
            doc = Jsoup.connect("http://www.cnblogs.com/zyw-205520/").get();
            Elements ListDiv = doc.getElementsByAttributeValue("class","postTitle");
            for (Element element :ListDiv) {
                Elements links = element.getElementsByTag("a");
                for (Element link : links) {
                    String linkHref = link.attr("href");
                    String linkText = link.text().trim();
                    System.out.println(linkHref);
                    System.out.println(linkText);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    /**
     * ��ȡָ���������µ�����
     */
    public static void Blog() {
        Document doc;
        try {
            doc = Jsoup.connect("http://www.cnblogs.com/zyw-205520/archive/2012/12/20/2826402.html").get();
            Elements ListDiv = doc.getElementsByAttributeValue("class","postBody");
            for (Element element :ListDiv) {
                System.out.println(element.html());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}