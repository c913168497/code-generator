package com.cnc.util.mybatis.tool.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @描述: SQL 数据类型 转换成 JAVA 数据类型
 * @作者: haw
 * @创建时间: 2017-01-16
 * @版本: 1.0
 */
public class SqlTypeConvertor {

    /**
     * SQL 数据类型 转换成 JAVA 数据类型
     * @param dbType    数据库类型
     * @param columnType SQL 数据类型
     * @param datasize 类型长度
     * @param digits   小数点大小
     * @return
     */
    public static String convert(DBType dbType,String columnType,int datasize,int digits){
        switch (dbType){
            case MYSQL:
                return convertMysql(columnType, datasize, digits);
            case ORACLE:
                return convertOralce(columnType, datasize, digits);
            default:
                return null;
        }
    }

    private static String convertOralce(String columnType, int datasize, int digits) {
        //=========================== ORACLE =========================================//
        System.out.println(columnType+":"+datasize+":"+digits);
        if (columnType.equalsIgnoreCase("NUMBER")){
            if(digits == 0){
                if(1 <= datasize &&datasize <= 9){
                    return Integer.class.getName();
                }else if(10<= datasize && datasize <=18){
                    return Long.class.getName();
                }else if(datasize > 18){
                    return BigInteger.class.getName();
                }
            }else{
                if(datasize > 18){
                    return  BigDecimal.class.getName();
                }else{
                    return Double.class.getName();
                }
            }
        }else if(columnType.equalsIgnoreCase("CHAR")||columnType.equalsIgnoreCase("VARCHAR2")||columnType.equalsIgnoreCase("NVARCHAR2")||columnType.equalsIgnoreCase("LONG")){
            return String.class.getName();
        }else if(columnType.equalsIgnoreCase("RAW")||columnType.equalsIgnoreCase("LONGRAW")){
            return byte[].class.getName();
        }else if(columnType.equalsIgnoreCase("DATE")){
            return Date.class.getName();
        }else if(columnType.equalsIgnoreCase("TIMESTAMP")){
            return Timestamp.class.getName();
        }else if(columnType.equalsIgnoreCase("CLOB")){
            return Clob.class.getName();
        }else if(columnType.equalsIgnoreCase("BLOB")){
            return Blob.class.getName();
        }else{
            return null;
        }
        return null;
    }

    private static String convertMysql(String columnType, int datasize, int digits) {
        //=========================== MYSQL =========================================//
        if(columnType.equalsIgnoreCase("BIT")){
            return Boolean.class.getName();
        }else if(columnType.equalsIgnoreCase("TINYINT") ||columnType.equalsIgnoreCase("TINYINT UNSIGNED") || columnType.equalsIgnoreCase("SMALLINT")
                ||columnType.equalsIgnoreCase("MEDIUMINT") ||columnType.equalsIgnoreCase("MEDIUMINT UNSIGNED") ||  columnType.equalsIgnoreCase("INT")
                ||columnType.equalsIgnoreCase("INTEGER")){
            return Integer.class.getName();
        }else if(columnType.equalsIgnoreCase("INTEGER UNSIGNED")){
            return Long.class.getName();
        }else if(columnType.equalsIgnoreCase("BIGINT")){
            return BigInteger.class.getName();
        }else if(columnType.equalsIgnoreCase("FLOAT")){
            return Float.class.getName();
        }else if(columnType.equalsIgnoreCase("DOUBLE")){
            return Double.class.getName();
        }else if(columnType.equalsIgnoreCase("DECIMAL")){
            return BigDecimal.class.getName();
        }else if(columnType.equalsIgnoreCase("BIGINT") || columnType.equalsIgnoreCase("BIGINT UNSIGNED")){
            return BigInteger.class.getName();
        }else if(columnType.equalsIgnoreCase("DATE")){
            return Date.class.getName();
        }else if( columnType.equalsIgnoreCase("DATETIME") || columnType.equalsIgnoreCase("TIMESTAMP")
                || columnType.equalsIgnoreCase("TIME")|| columnType.equalsIgnoreCase("YEAR")){
            return Timestamp.class.getName();
        }else if(columnType.equalsIgnoreCase("CHAR")|| columnType.equalsIgnoreCase("VARCHAR")|| columnType.equalsIgnoreCase("TEXT")
                || columnType.equalsIgnoreCase("TINYTEXT")|| columnType.equalsIgnoreCase("MEDIUMTEXT")|| columnType.equalsIgnoreCase("LONGTEXT")){
            return String.class.getName();
        }else if(columnType.equalsIgnoreCase("BINARY") || columnType.equalsIgnoreCase("VARBINARY") || columnType.equalsIgnoreCase("TINYBLOB")
                || columnType.equalsIgnoreCase("MEDIUMBLOB")|| columnType.equalsIgnoreCase("BLOB") || columnType.equalsIgnoreCase("LONGBLOB")){
            return byte[].class.getName();
        }else{
            return null;
        }
    }


}
