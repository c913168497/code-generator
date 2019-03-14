package com.cnc.util.mybatis.tool.viewUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

//定义节点类 
public class IconNode extends DefaultMutableTreeNode {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Icon icon;
	private String txt;
	private String name;
	private boolean flag = false;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// 只包含文本的节点构造
	public IconNode(String txt) {
		super();
		this.txt = txt;
	}

	// 包含文本和图片的节点构造
	public IconNode(Icon icon, String txt) {
		super();
		this.icon = icon;
		this.txt = txt;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public Icon getIcon() {
		return icon;
	}

	public void setText(String txt) {
		this.txt = txt;
	}

	public String getText() {
		return txt;
	}
}
