package com.weixin.install;

import com.weixin.data.CreatTable;
import com.weixin.servlet.TimeUpdate;

public class Install {
	public static void main(String[] args) {
		//创建需要的表格
		CreatTable.creatTable();
		//定时运行程序。包括签到的每日状态更新，和新增图文的更新。
		TimeUpdate.autoUpdate();
	}
}
