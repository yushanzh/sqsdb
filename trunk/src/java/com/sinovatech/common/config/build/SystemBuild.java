package com.sinovatech.common.config.build;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.sinovatech.common.config.bean.Group;
import com.sinovatech.common.config.bean.Property;
import com.sinovatech.common.config.file.FileCfg;
import com.sinovatech.common.util.JoinHelper;

public class SystemBuild {
	
	/**
	 * properties 文件路径 
	 */
	private String propertiesFilePath=null;
	private FileCfg fileCfg=null;
	public static final SystemBuild systemBuild=new SystemBuild();
	private SystemBuild(){
		String path=getClass().getResource("/").getPath();
		this.fileCfg=new FileCfg(new File(path));
	}
	public static SystemBuild getInstance(){
		return systemBuild;
	}
	private List groups;
	private final static String separator="-";
	
	/**
	 * @param groups the groups to set
	 */
	public void setGroups(List groups) {
		this.groups = groups;
	}
	private String getFdTypeName(String cls,String key){
		return cls+separator+key;
	}
	private String[] getCfg(String fdName){
		return fdName.split(separator);
	}
	/**
	 * 获取系统所有组
	 * @return
	 */
	public List systemGroups(){
		for (int i = 0; i < groups.size(); i++) {
			Group group=(Group) groups.get(i);
			Property property[]=group.getProperties();
			for (int j = 0; j < property.length; j++) {
				if(StringUtils.isBlank(property[j].getCfgName())){
					property[j].setCfgName(group.getCfgName());
				}
				property[j].setFdTypeValue(fileCfg.getProperty(property[j].getCfgName(), property[j].getCfgKey()));
				property[j].setFdTypeName(getFdTypeName(property[j].getCfgName(), property[j].getCfgKey()));
				property[j].buildFormField();
			}
		}
		return groups;
	}
	
	public void saveProperties(Map map){
		Iterator iterator=map.keySet().iterator();
		while(iterator.hasNext()){
			String key=(String) iterator.next();
			String value= JoinHelper.join((String[]) map.get(key), ",");
			if(!key.contains(separator))
				continue;
			String c_k[] =getCfg(key);
			fileCfg.putProperty(c_k[0], c_k[1], value);
		
		}
		fileCfg.store();
	}
	
	/**
	 * @return the propertiesFilePath
	 */
	public String getPropertiesFilePath() {
		return propertiesFilePath;
	}
	/**
	 * @param propertiesFilePath the propertiesFilePath to set
	 */
	public void setPropertiesFilePath(String propertiesFilePath) {
		this.fileCfg.setPath(new File(propertiesFilePath));
		this.propertiesFilePath = propertiesFilePath;
	}
}
