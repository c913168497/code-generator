package com.cnc.util.mybatis.tool.viewUtils;

import javax.swing.*;
import java.awt.*;

public class IconCellRenderer extends JLabel implements ListCellRenderer {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public Component getListCellRendererComponent(JList list, Object obj,
												  int index, boolean isSelected, boolean cellHasFocus) {
		Object[] cell = (Object[]) obj;
		setIcon((Icon) cell[0]);// 设置图片
		setText((cell[1].toString()));// 设置文本
		setToolTipText(cell[2].toString());// 设置提示文本
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));// 加入宽度为5的空白边框
		if (isSelected)// 如果选中
		{
			setBackground(Color.cyan);// 设置背景色
			setForeground(Color.blue);
		} else// 没有选中
		{
			setBackground(Color.white); // 设置背景色
			setForeground(Color.blue);
		}
		setEnabled(list.isEnabled());
		setFont(new Font("sdf", Font.ROMAN_BASELINE, 13));
		setOpaque(true);
		return this;
	}
}
