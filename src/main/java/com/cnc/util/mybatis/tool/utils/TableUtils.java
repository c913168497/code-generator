package com.cnc.util.mybatis.tool.utils;


import com.cnc.util.mybatis.tool.model.ColumnModel;
import com.cnc.util.mybatis.tool.model.TableModel;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-12-16.
 */
public class TableUtils {
    private static TableUtils instance;

    private TableUtils(){}
    public synchronized static TableUtils getInstance(){
        if(instance == null){
            instance = new TableUtils();
        }
        return instance;
    }

    /**
     * 获取该库中所有的表
     * @param isDetail 是否拿到表结构里面的详细信息(列的信息)
     * @return 表集合
     */
    public List<TableModel> getTableList(boolean isDetail){
        Connection conn  = null;
        ResultSet rs = null;
        List<TableModel> tables = new ArrayList<TableModel>();
        try {
            conn = JDBCUtils.getConnection();
            System.out.println("[conn]:"+conn);
            DatabaseMetaData metaData  = conn.getMetaData();
            //可为:"TABLE","VIEW","SYSTEM TABLE","GLOBAL TEMPORARY","LOCAL   TEMPORARY","ALIAS","SYNONYM"
            String[] types   = {"TABLE"};
            rs = metaData.getTables(null, convertDatabaseCharsetType(JDBCUtils.DB_USER, JDBCUtils.DB_TYPE), null, types);
            while (rs.next()){
                String tableCat = rs.getString("TABLE_CAT");
                String tableSchem= rs.getString("TABLE_SCHEM");
                String tableName= rs.getString("TABLE_NAME");
                String tableType= rs.getString("TABLE_TYPE");
                String remarks= rs.getString("REMARKS");
                TableModel model = new TableModel(tableCat,tableSchem,tableName,tableType,remarks,null);

                if(isDetail){
                    List<ColumnModel> cModels = getColumnModelsByName(tableName);
                    model.setColumnModels(cModels);
                }
                tables.add(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.free(rs,null,conn);
        }
        return tables;
    }

    /**
     * 根据表名获取表里面的所有的列信息
     * @param tableName 表名
     * @return 列信息集合
     */
    public List<ColumnModel>  getColumnModelsByName(String tableName){
        Connection conn  = null;
        ResultSet rs = null;
        List<ColumnModel>  cList = new ArrayList<ColumnModel>();
        try {
            conn = JDBCUtils.getConnection();
            DatabaseMetaData metaData  = conn.getMetaData();
            rs = metaData.getColumns(null,"%", tableName,"%");
            while (rs.next()){
                String columnName = rs.getString("COLUMN_NAME");
                String columnType = rs.getString("TYPE_NAME");
                int datasize = rs.getInt("COLUMN_SIZE");
                int digits = rs.getInt("DECIMAL_DIGITS");
                int nullable = rs.getInt("NULLABLE");
                String remarks = rs.getString("REMARKS");     //列注释
                String isAutoincrement = "NO";
                try{
                    isAutoincrement = rs.getString("IS_AUTOINCREMENT");//是否自增
                }catch (SQLException e){}
                ColumnModel model = new ColumnModel(columnName,columnType,datasize,digits,nullable,remarks,isAutoincrement);
                cList.add(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.free(rs,null,conn);
        }
        return cList;
    }

    /**
     * 根据DBUser和数据库名称获取查询表结构的DBUser
     * @param in DBUser
     * @param type 数据库类型
     * @return 查询表结构的DBUser
     */
    public  String convertDatabaseCharsetType(String in, DBType type) {
        String dbUser;
        if (in != null) {
            switch (type){
                case MSSQLSERVER:
                case MYSQL:
                    dbUser = null;
                    break;
                case DB2:
                case ORACLE:
                    dbUser = in.toUpperCase();
                    break;
                case POSTGRESQL:
                    dbUser = "public";
                    break;
                default:
                    dbUser = in;
            }
        } else {
            dbUser = "public";
        }
        return dbUser;
    }
}
