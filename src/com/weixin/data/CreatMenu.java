package com.weixin.data;

import java.io.IOException;
import java.text.ParseException;

import com.weixin.menu.Menu;
import com.weixin.util.WeixinUtil;

import net.sf.json.JSONObject;

public class CreatMenu {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Menu menu = WeixinUtil.initMenu();
		String menuStr = JSONObject.fromObject(menu).toString();
		System.out.println(menuStr);
		
		try {
			int resutl = WeixinUtil.createMenu(WeixinUtil.getExitAccessToken().getToken(), menuStr);
			System.out.println(resutl);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
}
	}

}
