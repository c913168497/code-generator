package com.cnc.util.mybatis.tool.viewUtils;

import javax.swing.*;
import java.awt.*;

public class IconList extends JList {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public IconList(DefaultListModel model)// 使用列表模板创建列表
	{
		super(model);// 调用父类构造方方
		setCellRenderer(new IconCellRenderer());// 设置单元格设置
		setBackground(Color.white);// 设置列表背景色
		setForeground(Color.blue);// 设置列表前景色
	}
}