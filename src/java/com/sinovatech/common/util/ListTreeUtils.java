/**
 * 
 */
package com.sinovatech.common.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * <ul>
 * <li> <b>目的:</b> <br />
 * <p>
 * 树排序工具:要求传入的列表的节点为树节点，父子节点关联关系为通过parentid进行关联
 * </p>
 * </li>
 * <li><b>采用的不变量：</b></li>
 * <li><b>并行策略：</b></li>
 * <li> <b>修改历史：</b><br />
 * <p>
 * 创建: Feb 19, 2008 1:37:50 PM<br />
 * 作者:liulibin@sinovatech.com
 * </p>
 * </li>
 * <li><b>已知问题：</b></li>
 * </ul>
 */
public class ListTreeUtils
{
	/**
	 * <p>
	 * <ul>
	 * <li>排序方法</li>
	 * </ul>
	 * </p>
	 * 
	 * @param list
	 *            需要排序的列表
	 * @param idName
	 *            主键编号
	 * @param parentIdName、
	 *            父节点的编号
	 * @param rootIdValue
	 *            根节点值
	 * @return
	 */
	public static List sort(List list, String idName, String parentIdName,
			Object rootIdValue)
	{
		List re = new ArrayList();

		List childs = filterListWithPropertiesValue(list, parentIdName,
				rootIdValue);
		if (childs.size() > 0)
		{
			re.addAll(childs);
			Iterator it = childs.iterator();
			while (it.hasNext())
			{
				Object o = it.next();
				Object root = getBeanProperty(o, idName);
				re.addAll(sort(list, idName, parentIdName, root));
			}
		}
		return re;
	}

	/**
	 * <p>
	 * <ul>
	 * <li>产生树的显示JS代码</li>
	 * </ul>
	 * </p>
	 * 
	 * @param list
	 * @param idName
	 * @param parentIdName
	 * @param rootIdValue
	 * @param nodeMake
	 * @return
	 */
	public static String make(List list, String idName, String parentIdName,
			Object rootIdValue, ITreeNodeMake nodeMake)
	{
		List childs = filterListWithPropertiesValue(list, parentIdName,
				rootIdValue);
		if (childs.size() > 0)
		{
			StringBuffer re = new StringBuffer();

			Iterator it = childs.iterator();
			while (it.hasNext())
			{
				Object o = it.next();
				// 产生JS代码并添加到返回列表
				Object root = getBeanProperty(o, idName);
				String str = make(list, idName, parentIdName, root, nodeMake);

				// 添加本节点
				re.append(nodeMake.makeNode(o, str != null));

				if (str != null)
					re.append(str);
			}

			return re.toString();
		} else
		{
			return null;
		}
	}

	/**
	 * <p>
	 * <ul>
	 * <li>对列表进行树方式排序</li>
	 * </ul>
	 * </p>
	 * 
	 * @param list
	 * @param idName
	 * @param parentIdName
	 * @param rootIdValue
	 * @param nodeMake
	 * @return
	 */
	public static String make2(List list, String idName, String parentIdName,
			Object rootIdValue, ITreeNodeMake nodeMake)
	{
		List childs = filterListWithPropertiesValue(list, parentIdName,
				rootIdValue);
		if (childs.size() > 0)
		{
			StringBuffer re = new StringBuffer();

			Iterator it = childs.iterator();
			while (it.hasNext())
			{
				Object o = it.next();
				// 产生JS代码并添加到返回列表
				Object root = getBeanProperty(o, idName);
				String str = make(list, idName, parentIdName, root, nodeMake);

				// 添加本节点
				re.append(nodeMake.makeNode(o, str != null));

				if (str != null)
					re.append(str);
			}

			return re.toString();
		} else
		{
			return null;
		}
	}

	/**
	 * <p>
	 * <ul>
	 * <li>过滤列表</li>
	 * </ul>
	 * </p>
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static List filterListWithPropertiesValue(List list,
			String propertyName, Object value)
	{
		List re = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext())
		{
			Object o = it.next();
			Object v = getBeanProperty(o, propertyName);
			if ((value == null && v == null)
					|| (value != null && value.equals(v)))
			{
				re.add(o);
				it.remove();
			}
		}
		return re;
	}

	/**
	 * <p>
	 * <ul>
	 * <li>获取bean的属性 </li>
	 * </ul>
	 * </p>
	 * 
	 * @param propertyName
	 * @param o
	 * @return
	 */
	public static Object getBeanProperty(Object o, String propertyName)
	{
		try
		{
			return PropertyUtils.getProperty(o, propertyName);
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		} catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
