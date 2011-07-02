package com.sinovatech.sqsdb.common;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class ReportObject {

	@PrimaryKey
	private String pk;
	
	private String ct1;
	private String ct2;
	private String ct3;
	private String time;
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	public String getCt1() {
		return ct1;
	}
	public void setCt1(String ct1) {
		this.ct1 = ct1;
	}
	public String getCt2() {
		return ct2;
	}
	public void setCt2(String ct2) {
		this.ct2 = ct2;
	}
	public String getCt3() {
		return ct3;
	}
	public void setCt3(String ct3) {
		this.ct3 = ct3;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
