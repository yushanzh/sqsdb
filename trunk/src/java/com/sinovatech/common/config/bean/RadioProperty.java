package com.sinovatech.common.config.bean;

import java.util.List;


public class RadioProperty extends Property{
	//scriptÐ£Ñé´úÂë
	private String smallStrFormScript;
	private List items;

	public RadioProperty(){}
	
	public RadioProperty( String fdName,String fdTypeValue,String fdTypeAttribute,String fdTypeName,int fdTypeOrder ){
		this.setFdName( fdName );
		this.setFdTypeValue( fdTypeValue );
		this.setFdTypeAttribute( fdTypeAttribute );
		this.setFdTypeName( fdTypeName );
		this.setFdTypeOrder( fdTypeOrder ); 
		buildFormField();
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

	public void buildFormField() {
		StringBuffer selBuf=new StringBuffer();
		if(items!=null){
			for (int i = 0; i < items.size(); i++) {
				Item item= (Item) items.get(i);
				if(item.getValue().equals(getFdTypeValue())){
					selBuf.append(item.getDisplay()+":<input type='radio' name='"+getFdTypeName()+"' value='"+item.getValue()+"' checked='checked'>&nbsp;");
				}else{
					selBuf.append(item.getDisplay()+":<input type='radio' name='"+getFdTypeName()+"' value='"+item.getValue()+"'>&nbsp;");
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
