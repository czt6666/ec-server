package com.bistu.common.JNI.dto;
/**
 * @author: LL
 * @date: 2023/7/12
 * @description: 权限功能
 */
public class myVPerferences {
    public int nPageMode;//页面窗口模式. 0-None常规模式(默认值); 1-FullScreen打开后全屏显示; 2-UseOutlines-同时呈现大纲视图; 3-UseThumbs-同时呈现缩略图; 4-UseCustomTags-同时呈现语义树管理视图; 5-UseLayers--同时呈现图层管理视图; 6-UseAttachs--同时呈现附件管理视图; 7-UseBookmarks--同时呈现书签管理视图
    public int nPageLayout;//页面布局. 0-OneColumn单页连续(默认值); 1-OnePage单页; 2-TwoPageL左右对开; 3-TwoColumnL对开连续; 4-TwoPageR对开靠右 5-TwoColumnR对开靠右连续
    public int nTabDisplay;//标题栏显示. 0-FileName文件名称(默认值); 1-DocTitle按照元数据中的Title属性值显示【若不存在则等价为0处理】
    public int nHideToolbar;//是否隐藏工具栏. 0-不隐藏(默认值); 1-隐藏
    public int nHideMenubar;//是否隐藏菜单栏. 0-不隐藏(默认值); 1-隐藏
    public int nHideWindowUI;//是否隐藏主窗口之外的其它窗体. 0-不隐藏(默认值); 1-隐藏
    //2020.03.05 dScale | nZoomMode 二选一，可能存在错误的ofd文件中二者都同时存在，此时优先处理权交给APP[规范没有说明]. 另ofd规范没有约束dScale值0.1代表10%还是10代表10%[有些样例交错表示--乱套]，建议也加入约束范围[0.1~64]
    public double dScale;//缩放值[0.1~64], 下述nZoomMode为-1时有效，否则下述nZoomMode缩放模式有效;2020.03.05补充dScale为0时nZoomMode有效[此时不为-1]
    public int nZoomMode;//缩放模式. -1代表上述dScale有效,即该缩放模式无效; 0-Default按照页面实际大小100%显示(默认); 1-FitHeight合适高度; 2-FitWidth适合宽度; 3-FitRect适合视区. also see bookmarks
    myVPerferences(){nPageMode=0; nPageLayout=0; nTabDisplay=0; nHideToolbar=0; nHideMenubar=0; nHideWindowUI=0; dScale=1.0; nZoomMode=-1;};
    void ctor(){nPageMode=0; nPageLayout=0; nTabDisplay=0; nHideToolbar=0; nHideMenubar=0; nHideWindowUI=0; dScale=1.0; nZoomMode=-1;}
}
