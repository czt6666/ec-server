package com.bistu.common.JNI.dto;

import java.util.Arrays;

public class TagsResult {
    public long Id;
    public long fatherId;
    public String TagsName;
    public int childCount;

    public stcODKNodeInfo[] kNodeList;

    public int pos;//12.5 add 所在树的索引

    public int selfpos;//12.5 add 节点所在索引

    public TagsResult(stcODKNodeInfo[] kNodeList) {
        this.kNodeList = kNodeList;
    }
    public TagsResult() {}
    //    public TagsResult(long id, String tagsName, int childCount, stcODKNodeInfo[] kNodeList) {
//        Id = id;
//        TagsName = tagsName;
//        this.childCount = childCount;
//        this.kNodeList = kNodeList;
//    }

//    public TagsResult(long id, String tagsName, int childCount) {
//        Id = id;
//        TagsName = tagsName;
//        this.childCount = childCount;
//    }

//    @Override
//    public String toString() {
//        return "TagsResult{" +
//                "Id=" + Id +
//                ", TagsName='" + TagsName + '\'' +
//                ", childCount=" + childCount +
//                ", kNodeList=" + Arrays.toString(kNodeList) +
//                '}';
//    }


    @Override
    public String toString() {
        return "TagsResult{" +
                "Id=" + Id +
                ", fatherId=" + fatherId +
                ", TagsName='" + TagsName + '\'' +
                ", childCount=" + childCount +
                ", kNodeList=" + Arrays.toString(kNodeList) +
                ", pos=" + pos +
                ", selfpos=" + selfpos +
                '}';
    }
}
