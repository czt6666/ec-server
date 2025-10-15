package com.bistu.common.JNI.dto;

public class stcODKNodeInfo {
    //代表电子公文等各种语义树中某个元素节点信息，如<od:抄送机关>节点， 尽管其可能有0~n个同名节点，但是每个节点用一个自动累计计数的ID标识，计数原则见如上TAGID说明
    //int nCount;//该值可以通过如下vector大小获取。 实际文档中出现该文本节点的个数1~n【通常1个，下述说明可能存在多个该节点情况】，不可能存在小于等于0的情况，否则就无该节点了。
    public int nPageNum;//该节点文本所在的页码--从1开始，允许跨页存在，即各页码值不同。多数情况页码都是相同的
    //ofd每个引用图元映射到内核图元's box：通常一个Tag元素节点由1-m个(文本)图元表达，每个图元有其对应的box.备注所有这些图元通常都是前后连接的，也可能断开，故没有直接给出这些区域的并集。应用层面逐个图元box高亮即可
    public CommonRect box;//Ofd文档页面坐标系下的box--通常用于高亮绘制该区域的文本(绘制时需要转换该区域(x,y)【这个需要注意页面绘制时的偏移量offset】+(w/h)到显示页面坐标系下，包括此时的页面pageScale)。

    public stcODKNodeInfo() {}

}
