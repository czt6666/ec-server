package com.bistu.common.JNI.dto;

import lombok.Data;

import java.util.Arrays;

@Data
public class MetadataResult {
	private String dataGW;
	private String dataDisp;
	private String dataCreate;
	private String docID;
	private String[] dataMetadata;
	private MyPermissionInfo permission;// 20240430LLadd

	public MetadataResult() {
	}

	public MetadataResult(String[] dataMetadata) {
		this.dataMetadata = dataMetadata;
	}

	public MetadataResult(String dataGW, String dataDisp, String dataCreate, String docID, String[] dataMetadata, MyPermissionInfo permission) {
		this.dataGW = dataGW;
		this.dataDisp = dataDisp;
		this.dataCreate = dataCreate;
		this.docID = docID;
		this.dataMetadata = dataMetadata;
		this.permission = permission;
	}

	@Override
	public String toString() {
		return "MetadataResult{" +
				"dataGW='" + dataGW + '\'' +
				", dataDisp='" + dataDisp + '\'' +
				", dataCreate='" + dataCreate + '\'' +
				", DocID='" + docID + '\'' +
				", dataMetadata=" + Arrays.toString(dataMetadata) +
				", permission=" + permission +
				'}';
	}
}
