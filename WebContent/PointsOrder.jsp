<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.weixin.data.SqlConn"
    	import="com.weixin.user.User"
    	import="com.weixin.util.WeixinUtil"
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta id="viewport" name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=2.0;" />
<title>积分排行榜</title>
<style type="text/css">
body {background:#fffff0; max-width:400px;margin:0 auto;font-family:"微软雅黑";font-size:20px; color:#007799}
p {margin:0; white-space:nowrap;}
table{border:0;border-spacing:0px;margin:0 15px;}
.row {  margin:10px 10px; padding:5px;
		 width:100%; border-color: #F0FBEB;
		background-color: #F0FBEB；}

.headFirstDiv{margin-left:40%;margin-top:15px;border-radius:50%;overflow:hidden;}
.headFirst{width:70px;height:70px;}
.mingci{font-family:"微软雅黑";font-size:14px; color:#666;margin-top:30px;}
.firstTr{margin-bottom:100px;}
.headImg {width:55px; height:55px;}
.headImgDiv{border-radius:50%;overflow:hidden; }
.headAll{float:left; margin:5px 5px;}

.wenzi{float:left;margin:0;margin-right:5px;}

.td2{width:78%;}
.td3{width:15%;}

td {
	    border-bottom: 8px solid #fffff0;
	    padding: 6px 6px 6px 6px;
	    background-color:#F0FBEB;
	}

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
<body>
	<%
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		String openId = request.getParameter("openid");//用request得到
	
		SqlConn sc = new SqlConn();
		String[][] pointsOrder = sc.getPointsOrder();
		
		User user = new User();
		user = sc.selectUser(openId);
		String headImgUrl = user.getHeadimgurl();
		int points = user.getPoints();
		int userOrder = sc.getOrder(openId);
		
		//System.out.println(userOrder);
		//System.out.println(openId);						
	%>
	<div class="headFirst headFirstDiv">		
					<img class="headFirst" alt="" src="<%=pointsOrder[0][2]%>" >													
	</div>
	
	
	<table class="table">
		<tr class="firstTr">
		<td>
			
		</td>
		<td class="td2">
				<div class="headImg headImgDiv headAll">		
					<img class="headImg" alt="" src="<%=headImgUrl%>" >												
				</div>
				<p class="mingci">第<%out.println(userOrder);%>名</p>	
			</td>
			
			<td class="td3">
				<%out.println(points);%>
			</td>
		</tr>
		<tr></tr>
	
	<% //out.println(pointsOrder.length);
		for(int i=0;i<pointsOrder.length;i++){
			if(i>9) break;
			//out.println(pointsOrder[i][0]);
			%>			
			<tr >
			<td>
				<p>&nbsp&nbsp&nbsp<%out.println(i+1); %></p>
			</td>
			
			<td class="td2">
				<div class="headImg headImgDiv headAll">		
					<img class="headImg" alt="" src="<%=pointsOrder[i][2]%>" >													
				</div>
			</td>
			
			<td class="td3">
				<%out.println(pointsOrder[i][3]);%>
			</td>
			</tr>		
			<%						
			}
			%>
	
	</table>	
</body>
</html>