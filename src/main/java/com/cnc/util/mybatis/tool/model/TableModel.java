package com.cnc.util.mybatis.tool.model;

import java.util.List;

/**
 * Created by Administrator on 2016-12-16.
 */
public class TableModel {
    /*
    TABLE_CAT       String   =>   table   catalog   (may   be   null)
    TABLE_SCHEM   String   =>   table     schema   (may   be   null)
    TABLE_NAME     String   =>   table   name
    TABLE_TYPE     String   =>   table   type.
    REMARKS           String   =>   explanatory   comment   on   the   table
    TYPE_CAT         String   =>   the   types   catalog   (may   be   null)
    TYPE_SCHEM     String   =>   the   types   schema   (may   be   null)
    TYPE_NAME       String   =>   type   name   (may   be   null)
    SELF_REFERENCING_COL_NAME   String   =>   name   of   the   designated   "identifier"   column   of   a   typed   table   (may   be   null)
    REF_GENERATION
    */
    private String tableCat;//表类别（可为 null）
    private String tableSchem;//表模式（可为 null）
    private String tableName;//表名称
    private String tableType;//表类型
    private String remarks;//描述列的注释（可为 null）
//    private String typeCat;//类型的类别（可为 null）
//    private String typeChem;//类型模式（可为 null）
//    private String typeName;//类型名称（可为 null）
//    private String selfReferencingColName;//有类型表的指定 "identifier" 列的名称（可为 null）
//    private String refGeneration;//指定在 SELF_REFERENCING_COL_NAME 中创建值的方式。这些值为 "SYSTEM"、"USER" 和 "DERIVED"。（可能为 null）
    private List<ColumnModel> columnModels;//列结构信息

    public TableModel() {
    }

    public TableModel(String tableCat, String tableSchem, String tableName, String tableType, String remarks,/* String typeCat, String typeChem, String typeName, String selfReferencingColName, String refGeneration, */List<ColumnModel> columnModels) {
        this.tableCat = tableCat;
        this.tableSchem = tableSchem;
        this.tableName = tableName;
        this.tableType = tableType;
        this.remarks = remarks;
//        this.typeCat = typeCat;
//        this.typeChem = typeChem;
//        this.typeName = typeName;
//        this.selfReferencingColName = selfReferencingColName;
//        this.refGeneration = refGeneration;
        this.columnModels = columnModels;
    }

    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(String tableCat) {
        this.tableCat = tableCat;
    }

    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(String tableSchem) {
        this.tableSchem = tableSchem;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

//    public String getTypeCat() {
//        return typeCat;
//    }
//
//    public void setTypeCat(String typeCat) {
//        this.typeCat = typeCat;
//    }
//
//    public String getTypeChem() {
//        return typeChem;
//    }
//
//    public void setTypeChem(String typeChem) {
//        this.typeChem = typeChem;
//    }
//
//    public String getTypeName() {
//        return typeName;
//    }
//
//    public void setTypeName(String typeName) {
//        this.typeName = typeName;
//    }
//
//    public String getSelfReferencingColName() {
//        return selfReferencingColName;
//    }
//
//    public void setSelfReferencingColName(String selfReferencingColName) {
//        this.selfReferencingColName = selfReferencingColName;
//    }
//
//    public String getRefGeneration() {
//        return refGeneration;
//    }
//
//    public void setRefGeneration(String refGeneration) {
//        this.refGeneration = refGeneration;
//    }

    public List<ColumnModel> getColumnModels() {
        return columnModels;
    }

    public void setColumnModels(List<ColumnModel> columnModels) {
        this.columnModels = columnModels;
    }

    @Override
    public String toString() {
        return "TableModel{" +
                "tableCat='" + tableCat + '\'' +
                ", tableSchem='" + tableSchem + '\'' +
                ", tableName='" + tableName + '\'' +
                ", tableType='" + tableType + '\'' +
                ", remarks='" + remarks + '\'' +
//                ", typeCat='" + typeCat + '\'' +
//                ", typeChem='" + typeChem + '\'' +
//                ", typeName='" + typeName + '\'' +
//                ", selfReferencingColName='" + selfReferencingColName + '\'' +
//                ", refGeneration='" + refGeneration + '\'' +
                ", columnModels=" + columnModels +
                '}';
    }
}
