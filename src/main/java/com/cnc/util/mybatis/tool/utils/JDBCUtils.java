package com.cnc.util.mybatis.tool.utils;

import java.sql.*;
import java.util.Map;
import java.util.Properties;


/**
 * Created by Administrator on 2016-12-16.
 */
public class JDBCUtils {

    public static Map<String, String> DB = ResourceUtils.getResource("db_test").getMap();
    public static final String DB_RUL = DB.get("default.jdbc.url");
    public static final String DB_USER = DB.get("default.jdbc.username");
    public static final String DB_PWD = DB.get("default.jdbc.password.real");
    public static final String DB_DRIVER = DB.get("default.jdbc.driverClassName");
    public static final String DB_TYPE_STR = DB.get("default.jdbc.type");
    public static DBType DB_TYPE = DBType.ORACLE;
    private static Properties prop  = new Properties();

    // 注册驱动
    static {
        try {
            Class.forName(DB_DRIVER);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
        if(DB_TYPE_STR.equalsIgnoreCase("ORACLE")){
            DB_TYPE = DB_TYPE.ORACLE;
        }else if(DB_TYPE_STR.equalsIgnoreCase("MYSQL")){
            DB_TYPE = DB_TYPE.MYSQL;
        }


        if(DB_TYPE == DB_TYPE.ORACLE){
            prop.put("remarksReporting","true");
        }
        prop.put("user",DB_USER);
        prop.put("password",DB_PWD);
    }
    //建立连接
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_RUL, prop);
    }
    //释放资源
    public static void free(ResultSet rs, Statement st, Connection conn) {
        try {
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }




}
