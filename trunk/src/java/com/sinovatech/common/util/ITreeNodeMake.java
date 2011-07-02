/**
 * 
 */
package com.sinovatech.common.util;

/**
 * <ul>
 * <li> <b>目的:</b> <br />
 * <p>
 * 节点更新编写实现
 * </p>
 * </li>
 * <li><b>采用的不变量：</b></li>
 * <li><b>并行策略：</b></li>
 * <li> <b>修改历史：</b><br />
 * <p>
 * 创建: Feb 19, 2008 3:11:23 PM<br />
 * 作者:liulibin@sinovatech.com
 * </p>
 * </li>
 * <li><b>已知问题：</b></li>
 * </ul>
 */
public interface ITreeNodeMake
{
	/**
	 * <p>
	 * <ul>
	 * <li>返回树节点的JS代码</li>
	 * </ul>
	 * </p>
	 * 
	 * @param o
	 *            节点对象
	 * @param hasChilds 
	 * 			是否用于子节点
	 * @return
	 */
	public String makeNode(Object o, boolean hasChilds);
}
