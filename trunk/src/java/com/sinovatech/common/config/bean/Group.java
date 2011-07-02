package com.sinovatech.common.config.bean;

public class Group {
	/**
	 * 组名称
	 */
	private String name;
	/**
	 * 文件名称
	 */
	private String cfgName;
	/**
	 * 属性组 
	 */
	private Property[] properties;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the properties
	 */
	public Property[] getProperties() {
		return properties;
	}
	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Property[] properties) {
		this.properties = properties;
	}
	/**
	 * @return the cfgName
	 */
	public String getCfgName() {
		return cfgName;
	}
	/**
	 * @param cfgName the cfgName to set
	 */
	public void setCfgName(String cfgName) {
		this.cfgName = cfgName;
	}
	
}
