package com.weixin.install;

import com.weixin.data.CreatTable;
import com.weixin.servlet.TimeUpdate;

public class Install {
	public static void main(String[] args) {
		//������Ҫ�ı��
		CreatTable.creatTable();
		//��ʱ���г��򡣰���ǩ����ÿ��״̬���£�������ͼ�ĵĸ��¡�
		TimeUpdate.autoUpdate();
	}
}
