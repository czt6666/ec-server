package com.bistu.common.JNI.dto;

public class OutlineElemResult {
    public String label;
    public int childCount;
    public int pageNum;
    public double left;
    public double right;
    public double top;
    public double bottom;
    public double zoom;

    public OutlineElemResult() {
    }

    public OutlineElemResult(String label, int childCount, int pageNum, double left, double right, double top, double bottom, double zoom) {
        this.label = label;
        this.childCount = childCount;
        this.pageNum = pageNum;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.zoom = zoom;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public double getLeft() {
        return left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public double getRight() {
        return right;
    }

    public void setRight(double right) {
        this.right = right;
    }

    public double getTop() {
        return top;
    }

    public void setTop(double top) {
        this.top = top;
    }

    public double getBottom() {
        return bottom;
    }

    public void setBottom(double bottom) {
        this.bottom = bottom;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    @Override
    public String toString() {
        return "OutlineElemResult{" +
                "label='" + label + '\'' +
                ", childCount=" + childCount +
                ", pageNum=" + pageNum +
                ", left=" + left +
                ", right=" + right +
                ", top=" + top +
                ", bottom=" + bottom +
                ", zoom=" + zoom +
                '}';
    }
}
