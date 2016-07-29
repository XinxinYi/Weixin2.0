package com.weixin.po;
/*
 * Material是专门为数据库存储图文素材
 */
public class Material extends Article{
	private String media_id;
	private String update_time;
	private int art_count;
	public String getMedia_id() {
		return media_id;
	}
	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public int getArt_count() {
		return art_count;
	}
	public void setArt_count(int art_count) {
		this.art_count = art_count;
	}
	
}
