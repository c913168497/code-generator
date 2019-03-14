package com.cnc.util.mybatis.tool.utils;


import com.cnc.util.mybatis.tool.model.ColumnModel;
import com.cnc.util.mybatis.tool.model.TableModel;

import java.util.List;

/**
 * Created by Administrator on 2016-12-20.
 */
public class GUIConvertor {
    public static final Object[] TABLENAMES = { "表名称", "表类型", "所属数据库","类型模式", "说明"};
    public static final Object[] COLUMNNAMES = { "列名", "类型", "大小","小数部分的位数","是否可空","是否自增","说明"};




    public static Object[][] model2tableModel(List<TableModel> tableModels){
        Object[][]  datas = new Object[tableModels.size()][TABLENAMES.length];
        for (int i = 0; i < tableModels.size(); i++) {
            TableModel model = tableModels.get(i);
            datas[i][0] = model.getTableName();
            datas[i][1] = model.getTableType();
            datas[i][2] = model.getTableCat();
            datas[i][3] = model.getTableSchem();
            datas[i][4] = model.getRemarks();
        }
        return datas;
    }

    public static Object[][] model2tableCoumns(List<ColumnModel> tableColumns){
        Object[][]  datas = new Object[tableColumns.size()][COLUMNNAMES.length];
        for (int i = 0; i < tableColumns.size(); i++) {
            ColumnModel model = tableColumns.get(i);
            datas[i][0] = model.getColumnName();
            datas[i][1] = model.getColumnType();
            datas[i][2] = model.getDatasize();
            datas[i][3] = model.getDigits();
            datas[i][4] = model.getNullable() == 1?"YES":"NO";
            datas[i][5] = model.getIsAutoincrement();
            datas[i][6] = model.getRemarks();
        }
        return datas;
    }

    public static void print(Object[][] datas){
        for (int i = 0; i <datas.length ; i++) {
            for (int j = 0; j < datas[i].length; j++) {
                System.out.print(datas[i][j]+"\t");
            }
            System.out.println();
        }
    }

}
