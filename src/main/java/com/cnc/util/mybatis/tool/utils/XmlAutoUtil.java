package com.cnc.util.mybatis.tool.utils;


import com.cnc.util.mybatis.tool.model.ColumnModel;
import com.cnc.util.mybatis.tool.model.TableModel;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2016-12-17.
 */
public class XmlAutoUtil extends AutoCoreUtil {



    //后缀
    public static final String MAPPERXML_SUFFIX="Mapper.xml";

    public static final String MAPPERXML_TEMP=BASE_TEMP+"MapperXML.template";

    //resultMap的模板
    public static final String RESULT_COLUMN_TEMP = "\t\t<result column=\"[COLUMN_NAME]\" property=\"[FILED_NAME]\" />"+NEWLINES;
    public static final String UPDATE_SQL_TEMP = "\t\t<if test=\"[FILED_NAME] != null and [FILED_NAME] != ''\"> [COLUMN_NAME] = #{[FILED_NAME]} </if>,"+NEWLINES;
    public static final String CONDITION_SQL_TEMP = "\t\t<if test=\"[FILED_NAME] != null and [FILED_NAME] != ''\"> and [COLUMN_NAME] = #{[FILED_NAME]} </if>"+NEWLINES;
    public static final String INSERT_SQL_TEMP = "\t\t([COLUMN_NAME_ARR])"+NEWLINES+"\t\tvalues ([COLUMN_VALUES_ARR])";

    @Override
    protected void create(DBType type, TableModel tableModel, String packageName, String saveDirectory) {
        File xmlSavePath = new File(saveDirectory+"/mapper");
        mkdirs(xmlSavePath);

        String NAMESPACE="";
        String TABLE_NAME="";
        String BEAN_TYPE="";
        String RESULT_COLUMN="";
        String INSERT_SQL="";
        String BEAN_ID="";
        String UPDATE_SQL="";
        String CONDITION_SQL="";
        String BEAN_NAME="";

        TABLE_NAME = tableModel.getTableName();
        String tableName = tableModel.getTableName();
        //t_xx => xx
        if(tableName.toLowerCase().startsWith("t_")){
            tableName = tableName.substring(2);
        }
        BEAN_NAME =  Character.toUpperCase(tableName.charAt(0))+toCamelString(tableName.substring(1));
        if(packageName != null){
            NAMESPACE = packageName+".mapper."+BEAN_NAME+"Mapper";
            BEAN_TYPE = packageName+".model.entity."+BEAN_NAME;
        }else{
            NAMESPACE = "mapper."+BEAN_NAME+"Mapper";
            BEAN_TYPE = "model.entity."+BEAN_NAME;
        }
        List<ColumnModel> cls  = tableModel.getColumnModels();
        if(cls != null && cls.size()>0){
            StringBuffer resultColumnStr = new StringBuffer();
            StringBuffer insertSqlNameStr = new StringBuffer();
            StringBuffer insertSqlValueStr = new StringBuffer();
            StringBuffer updateSqlStr = new StringBuffer();
            StringBuffer conditionSqlStr = new StringBuffer();
            for(ColumnModel model: cls){
                if (model.getColumnName().equalsIgnoreCase("F_ID") || model.getColumnName().equalsIgnoreCase("ID")){
                    BEAN_ID = model.getColumnName();
                }
                if(!model.getColumnName().equalsIgnoreCase("ID")){
                    String filedName = toCamelString(model.getColumnName());
                    String columnName = model.getColumnName();
                    insertSqlNameStr.append(columnName + ",");
                    insertSqlValueStr.append("#{" + filedName + "},");
                    resultColumnStr.append(RESULT_COLUMN_TEMP.replace("[COLUMN_NAME]",columnName).replace("[FILED_NAME]",filedName));
                    updateSqlStr.append(UPDATE_SQL_TEMP.replace("[COLUMN_NAME]", columnName).replace("[FILED_NAME]", filedName));
                    conditionSqlStr.append(CONDITION_SQL_TEMP.replace("[COLUMN_NAME]",columnName).replace("[FILED_NAME]", filedName));
                }
            }
            RESULT_COLUMN =  resultColumnStr.toString();
            UPDATE_SQL = updateSqlStr.substring(0,updateSqlStr.lastIndexOf(","));
            CONDITION_SQL =conditionSqlStr.toString();
            INSERT_SQL = INSERT_SQL_TEMP.replace("[COLUMN_NAME_ARR]",insertSqlNameStr.substring(0,insertSqlNameStr.length()-1))
                    .replace("[COLUMN_VALUES_ARR]", insertSqlValueStr.substring(0, insertSqlValueStr.length() - 1));

        }


        //xml
        String xmlTmpStr = file2String(MAPPERXML_TEMP);
        xmlTmpStr = xmlTmpStr.replace("[NAMESPACE]", NAMESPACE).replace("[BEAN_TYPE]",BEAN_TYPE)
                .replace("[TABLE_NAME]", TABLE_NAME).replace("[RESULT_COLUMN]",RESULT_COLUMN)
                .replace("[INSERT_SQL]", INSERT_SQL).replace("[UPDATE_SQL]",UPDATE_SQL)
                .replace("[BEAN_ID]", BEAN_ID)
                .replace("[CONDITION_SQL]", CONDITION_SQL);
        saveCode(xmlTmpStr,new File(xmlSavePath,BEAN_NAME+MAPPERXML_SUFFIX));
    }
}
