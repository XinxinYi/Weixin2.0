<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.weixin.data.SqlConn"
    	import="com.weixin.user.User"
    	import="com.weixin.util.WeixinUtil"
    	import="com.weixin.util.GetHtml"
    	import="com.weixin.servlet.TimeUpdate"
    	import="com.weixin.util.ConfigUtil"
    	import="com.weixin.data.CreatTable"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta id="viewport" name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=2.0;" />
<title>每日签到</title>
<style type="text/css">
body {background:#fffff0; max-width:400px;margin:0 auto;font-family:"微软雅黑";font-size:20px; color:#007799}
p {font-family:"黑体";font-size:26px;margin:0; font-weight:bold; font-style:italic; }
.othP{font-family:"微软雅黑";font-size:20px; color:#007799;float:left;}
.tab {background:#ffffff; border-radius:0;  
		 margin:0 10px; padding:15px;
		border:1px; border-style: solid;border-color:rgb(242,242,242);
		}
.tab1{text-align:center;margin: 0px auto;height:230px;}
.signImg{width:180px; height:180px; margin-bottom:8px; }
.tab2{margin-top:10px;height:110px;}
.headImg {width:55px; height:55px;}
.headImgDiv{border-radius:50%;overflow:hidden; }
.headAll{float:left; margin:5px 5px;}
.wenzi{float:left;margin:0;margin-right:5px;}
.lastwenzi{white-space:nowrap;}

.tab3{margin-top:10px;}
.title{font-family:"微软雅黑";font-size:20px;margin:0; font-weight:normal; font-style:normal; word-break: normal; color:#000}
.tjImg{margin:0 auto; width:320px; height:180px; }
.tab4{margin-top:10px;height:30px;}

</style>


</head>
 
<script type="text/javascript">
    // 对浏览器的UserAgent进行正则匹配，不含有微信独有标识的则为其他浏览器
    var useragent = navigator.userAgent;
    if (useragent.match(/MicroMessenger/i) != 'MicroMessenger') {
        // 这里警告框会阻塞当前页面继续加载
        alert('已禁止本次访问：您必须使用微信内置浏览器访问本页面！');
        // 以下代码是用javascript强行关闭当前页面
        var opened = window.open('about:blank', '_self');
        opened.opener = null;
        opened.close();
    }
</script>
<script type="text/javascript">
function orderclick(openId){
    //alert("hi");
    window.location.href="PointsOrder.jsp?openid="+openId;  
}
function tuijian(url){	
    window.location.href = url;  	
}
</script>
<body>
	<%
		//运行定时任务，在零点将所有用户的todaySign值置为false
		TimeUpdate.autoUpdate();			
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		String openId = request.getParameter("openid");//用request得到
		SqlConn sc = new SqlConn();
		User user = new User();
		user = sc.selectUser(openId);			
		String headImgUrl = user.getHeadimgurl();
		String[] allSignHead = sc.selectAllSign();
		int allSignCount = allSignHead.length;
		
	%> 
	<div class="tab tab1">
		<div class="tab1 signImg">
			<!--  <img class="signImg" alt="" src="http://tongyuan.tunnel.qydev.com/Weixin/files/f45083185e224c3b8e5e9df297e5fb5b.jpeg" >-->
			<img class="signImg" alt="" src="/Weixin/files/f45083185e224c3b8e5e9df297e5fb5b.jpeg" >			
		</div>
		<div >
			<img class="wenzi" alt="" src="/Weixin/files/yijianchi.png" >
			<p class="wenzi"><% out.println(user.getSignCount());%></p>
			<img class="wenzi lastwenzi" alt="" src="/Weixin/files/tian.png" >
			
			<!--  
			<img class="wenzi" alt="" src="http://tongyuan.tunnel.qydev.com/Weixin/files/bencijifen.png" >
			<p class="wenzi"> <% out.println(WeixinUtil.getPoints(user.getSignCount())); %></p>
			</br>
			<img class="wenzi" alt="" src="http://tongyuan.tunnel.qydev.com/Weixin/files/zongjifen.png" >
			<p class="wenzi"> <% out.println(user.getPoints()); %></p>
			-->
			<p>+<% out.println(WeixinUtil.getPoints(user.getSignCount())); %></p>
			
		</div>
	</div>
	<div class="tab tab2" onclick = "javascript:orderclick('<%=openId%>');">
		<table>
			<tr><td>
			<div >
				<img class="wenzi" alt="" src="/Weixin/files/jinridaka.png" >
				<p class="wenzi"><% out.println(allSignCount);%></p>
				<img class="wenzi lastwenzi" alt="" src="/Weixin/files/ren.png" >
			</div>
			</td></tr>
			<tr><td>					
			<%
				int i =0;
				for(i=0;i<allSignCount;i++){ %>
					<div class="headImg headImgDiv headAll">		
						<img class="headImg" alt="" src="<%=allSignHead[i]%>" >											
					</div>
			<%  
				if(i>3) break;
				}%>
				<h1 class="wenzi"><%if(i>7) out.println("...");%></h1>
				
			</td></tr>
		</table>					
	</div>

	<%		
		ConfigUtil cu = new ConfigUtil(CreatTable.configUrl);
		String html_url = cu.getValue("tuijian"); 
		String img_url = cu.getValue("imgUrl");
		String[] html = GetHtml.getHtml(html_url);
		//System.out.println(html[0]);
	%>
	<div class="tab tab3" onclick = "javascript:tuijian('<%=html_url%>');">
		<img class="wenzi" alt="" src="/Weixin/files/meirituijian.png" >
		</br></br>
		<p class="title"><%out.println(html[0]); %></p>
		</br>
		<div class = tjImg>
			<!--  <img class ="tjImg" alt="" src="<%=html[1]%>">-->
			<img class ="tjImg" alt="" src="<%=img_url%>">
		</div>
		</br>
		<p class="title">查看全文>></p>
			
	</div>
	
	<div class="tab tab4">
		<img class="wenzi" alt="" src="/Weixin/files/dakatongji.png" >
		<p class="wenzi"><%out.println(user.getSignAllCount()); %></p>
		<img class="wenzi" alt="" src="/Weixin/files/tian.png" >	
	</div>
	
</body>

</html>