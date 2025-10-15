package com.bistu.common.JNI.dto;

/**
 * @author: zxh
 * @date: 2023/6/13 15:37
 * @description: 注释移动
 */
public class stcobjecCTM {

	public long id;
	public CommonPos startpos;
	public CommonPos endpos;
	public int type;

	public stcobjecCTM() {
		id = 0;
		startpos = new CommonPos();
		endpos = new CommonPos();
		type = 0;
	}

	public stcobjecCTM(long id, CommonPos startpos, CommonPos endpos, int type){
		this.id = id;
		this.startpos = new CommonPos(startpos);
		this.endpos = new CommonPos(endpos);
		this.type = type;
	}

}
