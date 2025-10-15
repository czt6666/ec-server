package com.bistu.common.JNI.dto;


/***
 * 作用
 */
public class stcBookmark {
    //以下是该书签的目标区域描述
    public int	nPageNum;//从1开始，书签所在文档的页数
    public double	dScale;//点击书签GotoPage(nPageNum)后依据(该缩放比+nType)含义共同决定如何绘制显示该页,具体see nType含义
    /*ScrollToPage(nPageNum, dOffsetX=0, dOffsetY=0)该函数目的：精确设置滚动条位置(即确立页面绘制的精确坐标位置)<==内部首先通过nPageNum获取该待绘制页面左上角相对于所在窗口原点(通常都是0,0)的offset, 若dOffsetX/Y不为0再加上该偏移量.然后用设置滚动条水平和垂直位置
    XYZ-由左上角和dScale缩放比决定, 即SetPageScale(dScale); ScrollToPage(nPageNum, dLeft+0.5, dTop+0.5);
    Fit-即FitPage适合页面,即ScrollToPage(nPageNum)-滚动到该页文档坐标(0,0)位置. 备注此时dScale无效，真实缩放比值取值于min(有效显示窗口宽/页面原始宽, 有效显示窗口高/页面原始高)
    FitH-适合宽度，位置由Top坐标决定，即ScrollToPage(nPageNum, 0, dTop+0.5), 真实缩放比值取值于有效显示窗口宽/页面原始宽
    FitV-适合高度，位置由Left坐标决定，即ScrollToPage(nPageNum, dLeft+0.5, 0), 真实缩放比值取值于有效显示窗口高/页面原始高
    FitR-适合窗口，目标区域为(dLeft, dTop, dRight, dBottom）所确定的矩形区域Region。即ScrollToPage(nPageNum, dLeft+0.5, dTop+0.5);真实缩放比值取值于min(有效显示窗口宽/Region宽, 有效显示窗口高/Region高)
    */
    public int		nType;//0-XYZ, 1-Fit, 2-FitH, 3-FitV, 4-FitR 书签定位类型，由此计算该书签所在页面的准确位置
    public double	dLeft;
    public double	dTop;
    public double	dRight;
    public double	dBottom;
    public long	id; //书签的索引值
    public String strName;//2019.1.2 wchar->char否则vector at(i)返回乱码,书签名, 新增书签/换名时内部都会copy一份(strName为空时无效)：即外层传入的strName若是new出来的buffer,需要外层析构，包括文本选择接口获取的书签名。

    public stcBookmark(){nPageNum=1;dScale=1.0; nType=0;dLeft=0; dTop=0; dRight=0; dBottom=0; id=0; strName=" ";};//2019.1.2 add默认构造函数


    public int getnPageNum() {
        return nPageNum;
    }

    public void setnPageNum(int nPageNum) {
        this.nPageNum = nPageNum;
    }

    public double getdScale() {
        return dScale;
    }

    public void setdScale(double dScale) {
        this.dScale = dScale;
    }

    public int getnType() {
        return nType;
    }

    public void setnType(int nType) {
        this.nType = nType;
    }

    public double getdLeft() {
        return dLeft;
    }

    public void setdLeft(double dLeft) {
        this.dLeft = dLeft;
    }

    public double getdTop() {
        return dTop;
    }

    public void setdTop(double dTop) {
        this.dTop = dTop;
    }

    public double getdRight() {
        return dRight;
    }

    public void setdRight(double dRight) {
        this.dRight = dRight;
    }

    public double getdBottom() {
        return dBottom;
    }

    public void setdBottom(double dBottom) {
        this.dBottom = dBottom;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }
}
