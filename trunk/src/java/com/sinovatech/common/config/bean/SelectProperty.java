package com.sinovatech.common.config.bean;

import java.util.List;


public class SelectProperty extends Property{
	//scriptÐ£Ñé´úÂë
	private String smallStrFormScript;
	private List items;

	public SelectProperty(){}
	
	public SelectProperty( String fdName,String fdTypeValue,String fdTypeAttribute,String fdTypeName,int fdTypeOrder ){
		this.setFdName( fdName );
		this.setFdTypeValue( fdTypeValue );
		this.setFdTypeAttribute( fdTypeAttribute );
		this.setFdTypeName( fdTypeName );
		this.setFdTypeOrder( fdTypeOrder ); 
		buildFormField();
	}

	public void buildFormField() {
		StringBuffer selBuf=new StringBuffer();
		selBuf.append("<select name='"+getFdTypeName()+"' id='"+getFdTypeName()+"'>");
		if(items!=null){
			for (int i = 0; i < items.size(); i++) {
				Item item= (Item) items.get(i);
				if(item.getValue().equals(getFdTypeValue())){
					selBuf.append("<option selected value='"+item.getValue()+"'>"+item.getDisplay()+"</option>");
				}else{
					selBuf.append("<option value='"+item.getValue()+"'>"+item.getDisplay()+"</option>");
				}
			}
		}
		selBuf.append("</select>");
		this.setFdTypeLabel( selBuf.toString() );	    
//		smallStrFormScript = "if( !checkLeng(configForm."+getFdName()+",0,100,true,\""+this.getFdName()+"\") ){";
//		smallStrFormScript += " return false;";
//		smallStrFormScript += "}";
		this.setFormScript( smallStrFormScript );
	}

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


}
