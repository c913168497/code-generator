package com.cnc.util.mybatis.tool.utils;


import com.cnc.util.mybatis.tool.model.ColumnModel;
import com.cnc.util.mybatis.tool.model.TableModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 只针对表结构的简单缓存处理.
 */
public class SimpleCashe {
    private static List<TableModel> tables;

    static {
        tables  = TableUtils.getInstance().getTableList(false);
    }

    public static List<TableModel> getTables() {
        return tables;
    }


    public static List<TableModel> getTableByName(String tableName){
        List<TableModel> list = null;
        if(tables!= null && tables.size()>0){
            list = new ArrayList<TableModel>();
            for (int i = 0; i < tables.size(); i++) {
                TableModel table = tables.get(i);
                if (table.getTableName().toUpperCase().indexOf(tableName.toUpperCase()) != -1) {
                    if(table.getColumnModels()== null || table.getColumnModels().size()<=0){
                        table.setColumnModels(TableUtils.getInstance().getColumnModelsByName(tableName));
                    }
                    list.add(table);
                }
            }
        }
        return list;
    }

    public static List<ColumnModel> getTableColumns(String tableName){
        if(tables!= null && tables.size()>0){
            for (int i = 0; i < tables.size(); i++) {
                TableModel table = tables.get(i);
                if (table.getTableName().equalsIgnoreCase(tableName)) {
                    if (table.getColumnModels() == null || table.getColumnModels().size()<=0) {
                        List<ColumnModel> columns = TableUtils.getInstance().getColumnModelsByName(tableName);
                        table.setColumnModels(columns);
                        return columns;
                    }else {
                        System.out.println("=====use cache =====");
                        return table.getColumnModels();
                    }
                }
            }
        }
        return  null;
    }

}
