package com.cnc.util.mybatis.tool.model;

/**
 * Created by Administrator on 2016-12-16.
 */
public class ColumnModel {
    String columnName;  //列名
    String columnType;  //类型
    int datasize;       //大小
    int digits;         //小数部分的位数。对于 DECIMAL_DIGITS 不适用的数据类型，则返回 0。
    int nullable;       //是否可空
    String remarks;     //列注释
    String isAutoincrement;//是否自增

    public ColumnModel(String columnName, String columnType, int datasize, int digits, int nullable, String remarks, String isAutoincrement) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.datasize = datasize;
        this.digits = digits;
        this.nullable = nullable;
        this.remarks = remarks;
        this.isAutoincrement = isAutoincrement;
    }

    public ColumnModel() {

    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public int getDatasize() {
        return datasize;
    }

    public void setDatasize(int datasize) {
        this.datasize = datasize;
    }

    public int getDigits() {
        return digits;
    }

    public void setDigits(int digits) {
        this.digits = digits;
    }

    public int getNullable() {
        return nullable;
    }

    public void setNullable(int nullable) {
        this.nullable = nullable;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getIsAutoincrement() {
        return isAutoincrement;
    }

    public void setIsAutoincrement(String isAutoincrement) {
        this.isAutoincrement = isAutoincrement;
    }

    @Override
    public String toString() {
        return "ColumnModel{" +
                "columnName='" + columnName + '\'' +
                ", columnType='" + columnType + '\'' +
                ", datasize=" + datasize +
                ", digits=" + digits +
                ", nullable=" + nullable +
                ", remarks='" + remarks + '\'' +
                ", isAutoincrement='" + isAutoincrement + '\'' +
                '}';
    }
}
