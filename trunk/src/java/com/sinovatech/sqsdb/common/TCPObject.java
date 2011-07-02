package com.sinovatech.sqsdb.common;

import java.io.Serializable;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;




@Entity
public class TCPObject implements Serializable{
	
	@PrimaryKey
	private String pk;
	@SecondaryKey(relate=Relationship.MANY_TO_ONE)
	private String sk1;
	//@SecondaryKey(relate=Relationship.MANY_TO_ONE)
	private String sk2;
	//@SecondaryKey(relate=Relationship.MANY_TO_ONE)
	private String sk3;
	@SecondaryKey(relate=Relationship.MANY_TO_ONE)
	private String sk4;
	private String time;
	private String content;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	public String getSk1() {
		return sk1;
	}
	public void setSk1(String sk1) {
		this.sk1 = sk1;
	}
	public String getSk2() {
		return sk2;
	}
	public void setSk2(String sk2) {
		this.sk2 = sk2;
	}
	public String getSk3() {
		return sk3;
	}
	public void setSk3(String sk3) {
		this.sk3 = sk3;
	}
	public String getSk4() {
		return sk4;
	}
	public void setSk4(String sk4) {
		this.sk4 = sk4;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

}
