package com.sinovatech.common.config.bean;

public class Item {
	private String display;
	private String value;
	public Item(){
		
	}
	public Item(String display, String value) {
		super();
		this.display = display;
		this.value = value;
	}
	/**
	 * @return the display
	 */
	public String getDisplay() {
		return display;
	}
	/**
	 * @param display the display to set
	 */
	public void setDisplay(String display) {
		this.display = display;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
