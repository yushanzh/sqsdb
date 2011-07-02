package com.sinovatech.common.config.bean;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;


public class CheckProperty extends Property{
	//scriptÐ£Ñé´úÂë
	private String smallStrFormScript;
	private List items;

	public CheckProperty(){}
	
	/**
	 * @return the items
	 */
	public List getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List items) {
		this.items = items;
	}

	public CheckProperty( String fdName,String fdTypeValue,String fdTypeAttribute,String fdTypeName,int fdTypeOrder ){
		this.setFdName( fdName );
		this.setFdTypeValue( fdTypeValue );
		this.setFdTypeAttribute( fdTypeAttribute );
		this.setFdTypeName( fdTypeName );
		this.setFdTypeOrder( fdTypeOrder ); 
		buildFormField();
	}

	public void buildFormField() {
		StringBuffer selBuf=new StringBuffer();
		if(items!=null){
			String values[]=getFdTypeValue().split(",");
			for (int i = 0; i < items.size(); i++) {
				Item item= (Item) items.get(i);
				if(ArrayUtils.contains(values, item.getValue())){
					selBuf.append(item.getDisplay()+":<input type='checkbox' name='"+getFdTypeName()+"' value='"+item.getValue()+"' checked='checked'>");
				}else{
					selBuf.append(item.getDisplay()+":<input type='checkbox' name='"+getFdTypeName()+"' value='"+item.getValue()+"'>");
				}
			}
		}
		this.setFdTypeLabel( selBuf.toString() );	    
//		smallStrFormScript = "if( !checkLeng(configForm."+getFdName()+",0,100,true,\""+this.getFdName()+"\") ){";
//		smallStrFormScript += " return false;";
//		smallStrFormScript += "}";
		this.setFormScript( smallStrFormScript );
	}


}
