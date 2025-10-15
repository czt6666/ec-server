package com.bistu.common.JNI.dto;

/**
 * @author: zxh
 * @date: 2023/6/13 14:40
 * @description:
 */
public class CommonPos implements Comparable<CommonPos> {

	public double left;
	public double top;

	public CommonPos() {
		left = 0;
		top = 0;
	}

	public CommonPos(double left, double top) {
		this.left = left;
		this.top = top;
	}

	public CommonPos(CommonPos pos) {
		left = pos.left;
		top = pos.top;
	}

	public void setPosValue(double x, double y) {
		left = x;
		top = y;
	}

	@Override
	public int compareTo(CommonPos another) {
		return (int) (this.top - another.top);
	}

	@Override
	public String toString() {
		return "CommonPos{" +
				"left=" + left +
				", top=" + top +
				'}';
	}
}
