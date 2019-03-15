package com.cnc.util.mybatis.tool.utils;


import com.cnc.util.mybatis.tool.model.ColumnModel;
import com.cnc.util.mybatis.tool.model.TableModel;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016-12-16.
 */
public class CodeAutoUtil extends AutoCoreUtil {


    //tostring的模板
    public static final String TOSTR_TEMP="\"[CLASS_NAME]{\"+[TOSTR]+\"}\"";
    //字段的模板
    public static final String FILE_TEMP ="\t@ApiModelProperty(\"[REMARKS]\")" +NEWLINES+"\tprivate [FILED_TYPE] [FILED_NAME];"+NEWLINES;

    //各个模板的路径
    public static final String BEAN_TEMPLATE = BASE_TEMP+"bean.template";
    public static final String MAPPER_TEMPLATE = BASE_TEMP+"beanMapper.template";
    public static final String SERVICE_TEMPLATE = BASE_TEMP+"beanService.template";

    //后缀
    public static final String BEAN_SUFFIX=".java";
    public static final String MAPPER_SUFFIX="Mapper.java";
    public static final String SERVICE_SUFFIX="Service.java";
    @Override
    protected void create(DBType type, TableModel tableModel, String packageName, String saveDirectory) {
        File beanSavePath = new File(saveDirectory+"/model/entity");
        File mapperSavePath = new File(saveDirectory+"/mapper");
        File serviceSavePath = new File(saveDirectory+"/service");
        mkdirs(beanSavePath, mapperSavePath, serviceSavePath);

        //bean
        String BEAN_IMPORT = "";//引包
        String NOW_DATE = new Date().toLocaleString();//创建日期
        String BEAN_NAME = "";//类名
        String BEAN_NAME_LESS ="";//类名首字母小写
        String BEAN_FILE = "";//类的字段
        String BEAN_METHOD = "";//类的方法
        String BEAN_TOSTR_STR = "";//tostring返回类型
        String BEAN_PACKAGE = "";//实体类包名
        //mapper
        String MAPPER_PACKAGE="";
        String MAPPER_IMPORT = "";

        //service
        String SERVICE_PACKAGE="";
        String SERVICE_IMPORT="";

        String tableName = tableModel.getTableName();
        //t_xx => xx
        if(tableName.toLowerCase().startsWith("t_")){
            tableName = tableName.substring(2);
        }
        BEAN_NAME =  Character.toUpperCase(tableName.charAt(0))+toCamelString(tableName.substring(1));
        BEAN_NAME_LESS =  Character.toLowerCase(BEAN_NAME.charAt(0))+BEAN_NAME.substring(1);
        if(packageName != null && !packageName.trim().equals("")){
            BEAN_PACKAGE = "package "+packageName+".model.entity;";

            MAPPER_PACKAGE  = "package "+packageName+".mapper;";
            MAPPER_IMPORT = "import "+packageName+".model.entity."+BEAN_NAME+";";


            SERVICE_PACKAGE = "package "+packageName+".service;";
            SERVICE_IMPORT =  "import "+packageName+".model.entity."+BEAN_NAME+";"
                    + "import "+packageName+".mapper."+BEAN_NAME+"Mapper;";

        }else{
            MAPPER_PACKAGE  = "package mapper;";
            MAPPER_IMPORT = "import model.entity."+BEAN_NAME+";";

            SERVICE_PACKAGE = "package service;";
            SERVICE_IMPORT =  "import model.entity."+BEAN_NAME+";"
                + "import mapper."+BEAN_NAME+"Mapper;";

        }
        List<ColumnModel> cls  = tableModel.getColumnModels();
        if(cls != null && cls.size()>0){
            StringBuffer fieldsStr = new StringBuffer();
            //不能直接拼接，有重复导包的BUG
            //StringBuffer importStr = new StringBuffer();
            Set<String> improtSet = new HashSet<String>();
            StringBuffer methodStr = new StringBuffer();
            StringBuffer toStr = new StringBuffer();
            for(ColumnModel model: cls){
                if(checkColumn(model)){
                    String filedName = toCamelString(model.getColumnName());
                    String filedType = "String";//如果没有找打对应类型，默认为String
                    String methodName = Character.toUpperCase(filedName.charAt(0))+filedName.substring(1);
                    String javaType = SqlTypeConvertor.convert(JDBCUtils.DB_TYPE,model.getColumnType(),model.getDatasize(),model.getDigits());
                    if(javaType != null){
                        if(!javaType.startsWith("java.lang")){
                            improtSet.add(new StringBuffer().append("import ").append(javaType).append(";").append(NEWLINES).toString());
                        }
                        filedType = javaType.substring(javaType.lastIndexOf(".")+1);
                    }
                    fieldsStr.append(FILE_TEMP.replace("[FILED_TYPE]", filedType).replace("[FILED_NAME]", filedName).replace("[REMARKS]",model.getRemarks() == null?"":model.getRemarks()));
                    toStr.append("\""+filedName+":\"").append("+" + filedName + "+").append("\",\"").append("+");
                }
            }

            BEAN_FILE = fieldsStr.toString();
            BEAN_METHOD = methodStr.toString();
            StringBuffer importStr = new StringBuffer();
            if(improtSet != null && improtSet.size()>0){
                for(String s : improtSet){
                    importStr.append(s);
                }
            }
            BEAN_IMPORT = importStr.toString();
            BEAN_TOSTR_STR = TOSTR_TEMP.replace("[CLASS_NAME]",BEAN_NAME).replace("[TOSTR]",toStr.substring(0,toStr.length()-5));
        }
        //entity
        String beanTmpStr = file2String(BEAN_TEMPLATE);
        beanTmpStr = beanTmpStr.replace("[BEAN_PACKAGE]", BEAN_PACKAGE).replace("[BEAN_IMPORT]",BEAN_IMPORT)
                .replace("[NOW_DATE]", NOW_DATE).replace("[BEAN_NAME]",BEAN_NAME)
                .replace("[BEAN_FILE]", BEAN_FILE).replace("[BEAN_METHOD]",BEAN_METHOD)
                .replace("[BEAN_TOSTR_STR]", BEAN_TOSTR_STR);
        saveCode(beanTmpStr,new File(beanSavePath,BEAN_NAME+BEAN_SUFFIX));
        System.out.println(BEAN_NAME+BEAN_SUFFIX+"==自动生成完成！");
        //mapper
        String daoTmpStr = file2String(MAPPER_TEMPLATE);
        daoTmpStr = daoTmpStr.replace("[MAPPER_PACKAGE]",MAPPER_PACKAGE)
                .replace("[MAPPER_IMPORT]",MAPPER_IMPORT)
                .replace("[NOW_DATE]", NOW_DATE)
                .replace("[BEAN_NAME]",BEAN_NAME)
                .replace("[BEAN_NAME_LESS]",BEAN_NAME_LESS);
        saveCode(daoTmpStr, new File(mapperSavePath, BEAN_NAME + MAPPER_SUFFIX));
        System.out.println(BEAN_NAME + MAPPER_SUFFIX + "==自动生成完成！");

        //service
        String serviceStr = file2String(SERVICE_TEMPLATE);

        serviceStr = serviceStr.replace("[SERVICE_PACKAGE]", SERVICE_PACKAGE).replace("[SERVICE_IMPORT]", SERVICE_IMPORT)
                .replace("[MAPPER_PACKAGE]",MAPPER_PACKAGE).replace("[MAPPER_IMPORT]",MAPPER_IMPORT)
                .replace("[BEAN_NAME_LESS]", BEAN_NAME_LESS)
                .replace("[NOW_DATE]", NOW_DATE).replace("[BEAN_NAME]", BEAN_NAME);
        saveCode(serviceStr,new File(serviceSavePath,BEAN_NAME+SERVICE_SUFFIX));
        System.out.println(BEAN_NAME + SERVICE_SUFFIX + "==自动生成完成！");
    }

    public boolean checkColumn(ColumnModel model){
        String columnName = model.getColumnName();
        return !columnName.equalsIgnoreCase("ID") &&
                !columnName.equalsIgnoreCase("SYS_FLAG") &&
                !columnName.equalsIgnoreCase("REMARK") &&
                !columnName.equalsIgnoreCase("CREATOR") &&
                !columnName.equalsIgnoreCase("CREATE_TIME") &&
                !columnName.equalsIgnoreCase("LAST_MODIFIED_TIME") &&
                !columnName.equalsIgnoreCase("LAST_MODIFIER") &&
                !columnName.equalsIgnoreCase("F_ID") &&
                !columnName.equalsIgnoreCase("F_SYS_FLAG") &&
                !columnName.equalsIgnoreCase("F_REMARK") &&
                !columnName.equalsIgnoreCase("F_CREATOR") &&
                !columnName.equalsIgnoreCase("F_CREATE_TIME") &&
                !columnName.equalsIgnoreCase("F_LAST_MODIFIED_TIME") &&
                !columnName.equalsIgnoreCase("F_LAST_MODIFIER");
    }
}
