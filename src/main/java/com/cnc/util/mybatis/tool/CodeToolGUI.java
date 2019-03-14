package com.cnc.util.mybatis.tool;


import com.cnc.util.mybatis.tool.model.ColumnModel;
import com.cnc.util.mybatis.tool.model.TableModel;
import com.cnc.util.mybatis.tool.utils.*;
import com.cnc.util.mybatis.tool.viewUtils.GBC;
import com.cnc.util.mybatis.tool.viewUtils.IconNode;
import com.cnc.util.mybatis.tool.viewUtils.IconNodeRenderer;
import org.jvnet.substance.skin.SubstanceOfficeSilver2007LookAndFeel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Date;
import java.util.List;

public class CodeToolGUI extends JFrame {

    public static final Integer WINDOW_WIDTH = 800;
    public static final Integer WINDOW_HEIGHT = 600;
    //下划线
    public static final String NEWLINES = System.getProperty("line.separator");

    private JPanel leftJPanel = new JPanel(); //左边容器
    private JTree  leftTree = new JTree();   //左边树形结构

    private JSplitPane hSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);//将界面分成左右两部分的分割线
    private JSplitPane vSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);//将界面分成上线两部分的分割线


    private JPanel rightJpanel = new JPanel(); //右边容器
    private JTabbedPane tabPane = new JTabbedPane(); //选项卡
    //表格
    private JTable   tablesTable = new JTable(new DefaultTableModel(null, GUIConvertor.TABLENAMES)){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;//不可编辑
        }
    };//表格
    //表格2
    private JTable   columnsTable = new JTable(new DefaultTableModel(null,GUIConvertor.COLUMNNAMES)){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;//不可编辑
        }
    };

    private JTextField searchField = new JTextField();
    private JButton searchBtn = new JButton("搜索");

    private JCheckBox javaCheckBox = new JCheckBox("JAVA文件");// 定义一个复选框
    private JCheckBox xmlCheckBox = new JCheckBox("XML文件");// 定义一个复选框
    private JTextField packageField = new JTextField();

    private JTextField dirField = new JTextField();
    private JButton dirBtn = new JButton("选择");

    private JButton submitButton  = new JButton("确定生成");

    private JTextArea textLog = new JTextArea();


    private AutoCoreUtil codeUtil = new CodeAutoUtil();//代码生成工具类
    private AutoCoreUtil xmlUtil = new XmlAutoUtil();//xml文件生成工具类

    public CodeToolGUI() {
        this("代码生成器");
    }

    public CodeToolGUI(String title){
        initTool(title);
        initEvent();
    }

    private void initEvent() {
        //树节点的点击事件
        leftTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 单击节点
                if (e.getClickCount() == 1) {
                    TreePath path = leftTree.getSelectionPath();// 获取选中节点路径
                    IconNode node = (IconNode) path.getLastPathComponent();// 通过路径将指针指向该节点
                    // 如果该节点是叶子节点
                    if (node.isLeaf() && !node.isFlag()) {
                        node.setFlag(true);
                        if (node.getText().equalsIgnoreCase("代码生成")) {
                            List<TableModel> list = SimpleCashe.getTables();
                            DefaultTableModel tableDatas = new DefaultTableModel(GUIConvertor.model2tableModel(list), GUIConvertor.TABLENAMES);
                            tablesTable.setModel(tableDatas);
                            SwingUtilities.updateComponentTreeUI(tablesTable);
                        }
                    }
                }
            }
        });
        //table的点击事件
        tablesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 双击节点
                if (e.getClickCount() == 2) {
                    int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint()); //获得行位置
                    String tableName = tablesTable.getValueAt(row, 0).toString();
                    if (tableName != null) {
                        List<ColumnModel> models = SimpleCashe.getTableColumns(tableName);
                        textLog.append(new Date().toLocaleString() + " *当前查询[" + tableName + "]表结构信息" + NEWLINES);
                        DefaultTableModel tableDatas = new DefaultTableModel(GUIConvertor.model2tableCoumns(models), GUIConvertor.COLUMNNAMES);
                        columnsTable.setModel(tableDatas);
                        SwingUtilities.updateComponentTreeUI(columnsTable);
                        tabPane.setSelectedIndex(1);
                    }
                }
            }
        });

        //搜索点击事件
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tableName = searchField.getText().toString();
                if (tableName == null || tableName.trim().equals("")) {
                    JOptionPane.showMessageDialog(rightJpanel, "表名不能为空", "提示", JOptionPane.WARNING_MESSAGE);
                    textLog.append(new Date().toLocaleString() + " *表名不能为空" + NEWLINES);
                    return;
                }
                List<TableModel> list = SimpleCashe.getTableByName(tableName);
                DefaultTableModel tableDatas = new DefaultTableModel(GUIConvertor.model2tableModel(list), GUIConvertor.TABLENAMES);
                tablesTable.setModel(tableDatas);
                SwingUtilities.updateComponentTreeUI(tablesTable);
            }
        });

        //选择文件点击事件
        dirBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));
                jfc.showDialog(new JLabel(), "选择");
                File file = jfc.getSelectedFile();
                if (file != null) {
                    String path = file.getAbsolutePath();
                    textLog.append(new Date().toLocaleString() + " *选择文件路径：" + path + NEWLINES);
                    dirField.setText(path);
                }
            }
        });

        //确认生成点击事件
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //是否选中表名
                String tableName = (tablesTable.getValueAt(tablesTable.getSelectedRow(), 0).toString());
                textLog.append(new Date().toLocaleString() + " *当前选中表名：" + tableName + NEWLINES);
                if (tableName == null || tableName.trim().equals("")) {
                    textLog.append(new Date().toLocaleString() + " *须要选中您要生成的表！！！" + NEWLINES);
                    JOptionPane.showMessageDialog(rightJpanel, "须要选中您要生成的表！！！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                //复选框验证
                boolean isJavaCode = javaCheckBox.isSelected();
                textLog.append(new Date().toLocaleString() + " *否是生成JAVA文件：" + isJavaCode + NEWLINES);
                boolean isXMLCode = xmlCheckBox.isSelected();
                textLog.append(new Date().toLocaleString() + " *否是生成XML文件：" + isXMLCode + NEWLINES);
                if (!isJavaCode && !isXMLCode) {
                    JOptionPane.showMessageDialog(rightJpanel, "您没有勾选需要生成的文件！！！", "提示", JOptionPane.WARNING_MESSAGE);
                    textLog.append(new Date().toLocaleString() + " *您没有勾选需要生成的文件！！！" + NEWLINES);
                    return;
                }
                //包名
                String packageName = packageField.getText().toString();
                if (packageName == null || packageName.trim().equals("")) {
                    packageName = "";
                }
                packageName = packageName.replaceAll("\\\\", ".");
                textLog.append(new Date().toLocaleString() + " *选择生成包名：" + packageName + NEWLINES);
                //生成路径
                String filePath = dirField.getText().toString();
                textLog.append(new Date().toLocaleString() + " *选择生成路径：" + filePath + NEWLINES);
                File file = new File(filePath);
                if (!file.exists() || !file.isDirectory()) {
                    textLog.append(new Date().toLocaleString() + " *选择生成路径错误：路径不存在，或者路径不是目录！" + NEWLINES);
                    JOptionPane.showMessageDialog(rightJpanel, "选择生成路径错误：路径不存在，或者路径不是目录！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (javaCheckBox.isSelected()) {
                    textLog.append(new Date().toLocaleString() + " JAVA文件生成中..." + NEWLINES);
                    codeUtil.createCode(JDBCUtils.DB_TYPE, SimpleCashe.getTableByName(tableName).get(0), packageName, filePath);
                    textLog.append(new Date().toLocaleString() + " JAVA文件生成中完成！" + NEWLINES);
                }
                if (xmlCheckBox.isSelected()) {
                    textLog.append(new Date().toLocaleString() + " XML文件生成中..." + NEWLINES);
                    xmlUtil.createCode(JDBCUtils.DB_TYPE, SimpleCashe.getTableByName(tableName).get(0), packageName, filePath);
                    textLog.append(new Date().toLocaleString() + " XML文件生成中完成！" + NEWLINES);
                }

            }
        });
    }



    public void initTool(String title){
        if(title == null){
            setTitle("CodeCreators");
        }else{
            setTitle(title);
        }
        //定义窗口位置.
        setBounds(100, 100, 800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        initLeftTree();
        initLeftPanel();
        initHSplitPane();

        initTabs();
        initRightPanel();
        initVSplitPane();


    }

    private void initVSplitPane() {
        //分割条上方的高度为30像素.
        vSplitPane.setDividerLocation(450);
        //定义分割条宽度.
        vSplitPane.setDividerSize(8);
        vSplitPane.setOneTouchExpandable(false);//提供一个UI小部件.
        //在调整分割条位置时面板的重绘方式为连续绘制.
        vSplitPane.setContinuousLayout(true);
        hSplitPane.setEnabled(false);
        //将分割的垂直面板添加到水平面板右侧.
        hSplitPane.setRightComponent(vSplitPane);

    }

    private void initTabs() {
        //设置列表内容
        JScrollPane pan1 =new JScrollPane(tablesTable);
        JScrollPane pan2 =new JScrollPane(columnsTable);
        tablesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        columnsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabPane.addTab("表列表", pan1);
        tabPane.addTab("表结构", pan2);
    }

    private void initRightPanel() {
        rightJpanel.setLayout(null);
        JPanel searchPanel  = new JPanel();
        searchPanel.setLayout(null);
        searchPanel.setBounds(0, 0, WINDOW_WIDTH, 35);
        searchField.setBounds(WINDOW_WIDTH / 4 + 40, 10, 130, 20);
        searchBtn.setBounds(WINDOW_WIDTH / 4 + 170, 10, 60, 20);
        JLabel searchLabel = new JLabel("表名:");
        searchLabel.setBounds(WINDOW_WIDTH / 4, 10, 40, 20);
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        rightJpanel.add(searchPanel);


        tabPane.setBounds(2, 35, WINDOW_WIDTH - 142, WINDOW_HEIGHT / 2);
        rightJpanel.add(tabPane);

        JPanel rightContentPanel  = new JPanel();//放条件区
        rightContentPanel.setBounds(WINDOW_WIDTH / 6, 35 + WINDOW_HEIGHT / 2, WINDOW_WIDTH / 2, WINDOW_HEIGHT / 5);
        rightContentPanel.setLayout(new GridBagLayout());
        rightJpanel.add(rightContentPanel);

        rightContentPanel.add(new JLabel("选择生成文件："), new GBC(0, 0, 2, 1).setFill(GBC.HORIZONTAL));
        //默认选中
        javaCheckBox.setSelected(true);
        xmlCheckBox.setSelected(true);
        rightContentPanel.add(javaCheckBox, new GBC(2, 0, 1, 1).setFill(GBC.HORIZONTAL));
        rightContentPanel.add(xmlCheckBox, new GBC(3, 0, 1, 1).setFill(GBC.HORIZONTAL));
        rightContentPanel.add(new JLabel("请输入包名："), new GBC(0, 2, 2, 1).setFill(GBC.HORIZONTAL));
        rightContentPanel.add(packageField, new GBC(2, 2, 3, 1).setFill(GBC.HORIZONTAL));
        rightContentPanel.add(new JLabel("选择路径："), new GBC(0, 4, 2, 1).setFill(GBC.HORIZONTAL).setInsets(10, 0, 0, 0));
        rightContentPanel.add(dirField, new GBC(2, 4, 2, 1).setFill(GBC.BOTH).setInsets(5, 0, 0, 0));
        rightContentPanel.add(dirBtn, new GBC(4, 4, 1, 1).setFill(GBC.BOTH).setInsets(5, 0, 0, 0));
        rightContentPanel.add(submitButton, new GBC(0, 6, 5, 1).setFill(GBC.BOTH).setInsets(10, 0, 0, 0));
        vSplitPane.setTopComponent(rightJpanel);
        //添加文本框到容器中
        textLog = new JTextArea();
        textLog.setLineWrap(true);                  //激活自动换行功能
        textLog.setWrapStyleWord(true);            // 激活断行不断字功能
        textLog.setEditable(false);
        JScrollPane textPanel = new JScrollPane(textLog);
        vSplitPane.setBottomComponent(textPanel);
    }

    private void initHSplitPane() {
        // 设置中间分割条大小
        hSplitPane.setDividerSize(8);
        //分隔条左侧的宽度为120像素.
        hSplitPane.setDividerLocation(115);
        // 在分割条上添加小三角按钮可以实现JSplitPane左右/上下组件的快速展开或折叠。
        //hSplitPane.setOneTouchExpandable(true);
        hSplitPane.setEnabled(false);            //设置分隔条禁止拖动
        //添加到指定区域.
        getContentPane().add(hSplitPane, BorderLayout.CENTER);
        hSplitPane.setLeftComponent(leftJPanel);
        hSplitPane.setRightComponent(rightJpanel);
    }

    private void initLeftPanel() {
        leftJPanel.setLayout(null);
        leftJPanel.add(leftTree);
    }

    private void initLeftTree() {
        IconNode Root = new IconNode(new ImageIcon(this.getClass().getClassLoader().getResource("image/folder.png")), "代码管理");// 定义根节点
        Root.add(new IconNode(new ImageIcon(this.getClass().getClassLoader().getResource("image/page_edit.png")), "代码生成"));
        Root.add(new IconNode(new ImageIcon(this.getClass().getClassLoader().getResource("image/page_edit.png")), "后期扩展"));
        leftTree = new JTree(Root);
        leftTree.setCellRenderer(new IconNodeRenderer()); // 设置单元格描述
        leftTree.setEditable(false); // 设置树是否可编辑
        leftTree.setToggleClickCount(1);// 设置单击几次展开数节点
        leftTree.setBounds(10, 10, 100, WINDOW_HEIGHT);
    }


    //主方法.
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                try {
                    UIManager.setLookAndFeel(new SubstanceOfficeSilver2007LookAndFeel());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CodeToolGUI frame = new CodeToolGUI("代码自动生成器");
                frame.setVisible(true);
            }
        });
    }

}