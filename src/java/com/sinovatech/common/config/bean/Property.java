package com.sinovatech.common.config.bean;

public abstract class Property {
	//配置文件
	private String cfgName;
	//配置键
	private String cfgKey;
	//	表单域显示名
	private String fdName;
	//表单域Label
	private String fdTypeLabel;
	//表单域value
	private String fdTypeValue = "";
	//表单域属性
	private String fdTypeAttribute;
	
	//表单域名称
	private String fdTypeName;
	
	//表单域显示位置
	private int fdTypeOrder;
	//注释
	private String common;
	//script表单校验代码
	private String formScript;

	public Property(){
	}
	
	
	
	public int getFdTypeOrder() {
		return fdTypeOrder;
	}

	public void setFdTypeOrder(int fdTypeOrder) {
		this.fdTypeOrder = fdTypeOrder;
	}

	public String getFdTypeAttribute() {
		return fdTypeAttribute;
	}

	public void setFdTypeAttribute(String fdTypeAttribute) {
		this.fdTypeAttribute = fdTypeAttribute;
	}

	public String getFdTypeValue() {
		return fdTypeValue;
	}

	public void setFdTypeValue(String fdTypeValue) {
		this.fdTypeValue = fdTypeValue;
	}

	public String getFdTypeLabel() {
		return fdTypeLabel;
	}

	public void setFdTypeLabel(String fdTypeLabel) {
		this.fdTypeLabel = fdTypeLabel;
	}
	
	abstract public void buildFormField();

	public String getFdTypeName() {
		return fdTypeName;
	}

	public void setFdTypeName(String fdTypeName) {
		this.fdTypeName = fdTypeName;
	}

	public String getFdName() {
		return fdName;
	}

	public void setFdName(String fdName) {
		this.fdName = fdName;
	}
	
	/**
	 * @return the common
	 */
	public String getCommon() {
		return common;
	}



	/**
	 * @param common the common to set
	 */
	public void setCommon(String common) {
		this.common = common;
	}



	public String getFormScript() {
		return formScript;
	}

	public void setFormScript(String formScript) {
		this.formScript = formScript;
	}
	
	public String getFdTypeNameByStr( String fdName ){
		return "fd"+fdName;
	}
	
	public String getDealString( String s ){
		if( s==null )
			return "";
		else
			return s;
	}



	/**
	 * @return the cfgKey
	 */
	public String getCfgKey() {
		return cfgKey;
	}



	/**
	 * @param cfgKey the cfgKey to set
	 */
	public void setCfgKey(String cfgKey) {
		this.cfgKey = cfgKey;
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
