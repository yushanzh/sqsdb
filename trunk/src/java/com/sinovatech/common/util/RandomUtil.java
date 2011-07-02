/**
 * 
 */
package com.sinovatech.common.util;

import java.util.Random;

/**
 * <ul>
 * <li> <b>目的:</b> <br />
 * <p>
 * 随机数工具类
 * </p>
 * </li>
 * <li><b>采用的不变量：</b></li>
 * <li><b>并行策略：</b></li>
 * <li> <b>修改历史：</b><br />
 * <p>
 * 创建:Dec 11, 2007 4:15:42 PM<br />
 * 作者:liulibin@sinovatech.com
 * </p>
 * </li>
 * <li><b>已知问题：</b></li>
 * </ul>
 */

public class RandomUtil
{
	private RandomUtil()
	{

	}

	/**
	 * 得到width长度的随机数字组成的字符串
	 * 
	 * @param width
	 * @return
	 */
	public static String getRand(int width)
	{
		Random random = new Random();
		String strRand = "";
		for (int i = 0; i < width; i++)
		{
			String rand = String.valueOf(random.nextInt(10));
			strRand += rand;
		}
		return strRand;
	}

}
