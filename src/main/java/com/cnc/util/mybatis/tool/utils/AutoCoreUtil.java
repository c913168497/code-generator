package com.cnc.util.mybatis.tool.utils;


import com.cnc.util.mybatis.tool.model.TableModel;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016-12-17.
 */
public abstract class AutoCoreUtil {


    //换行符
    public static final String NEWLINES = System.getProperty("line.separator");
    //存放模板的目录
    public static final String BASE_TEMP = System.getProperty("user.dir")+"/src/main/resources/template/";

    public void createCode(DBType type,TableModel tableModel,String packageName,String saveDirectory){
        //先数据验证
        if(tableModel == null){
            throw new NullPointerException("tableModel can not be null");
        }
        String tableName = tableModel.getTableName();
        if(tableName == null || "".equals(tableName.trim())){
            throw new RuntimeException("table name can not be null or ''");
        }
        if(saveDirectory == null){
            throw new NullPointerException("saveDirectory can not be null");
        }
        create(type,tableModel,packageName,saveDirectory);
    }
    protected abstract void create(DBType type,TableModel tableModel,String packageName,String saveDirectory);

    protected static void mkdirs(File... dirs){
        if(dirs == null){
            throw new NullPointerException("the directory you need create can not be null");
        }
        for (int i = 0; i <dirs.length ; i++) {
            if(!dirs[i].exists()){
                boolean flag = dirs[i].mkdirs();
                if(!flag) throw new RuntimeException("the directory ["+dirs[i]+"] can not create!!!");
            }
        }
    }
    /**
     * 读取指定的文件内容，以字符串的方式返回
     * @param descPath 文件路径
     * @return 文件内容
     */
    protected static String file2String(String descPath){
        BufferedReader reader = null;
        StringBuffer srcFileStr = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(descPath));
            String content = "";
            while((content = reader.readLine())!=null){
                srcFileStr.append(content).append(NEWLINES);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(reader!= null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return srcFileStr.toString();
    }

    /**
     * 将字符串保存到指定的目录文件中
     * @param codeStr 要保存的字符串
     * @param savePath 保存路径
     * @return 是否保存成功
     */
    protected static boolean saveCode(String codeStr,File savePath){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(savePath);
            fos.write(codeStr.getBytes());
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    /**
     * 改字符串为驼峰标致：要求字符串是以_风格命名。如: user_add =>userAdd
     * @param param _风格命名字符串
     * @return 驼峰型的字符串
     */
    protected static String toCamelString(String param){
        if (param==null||"".equals(param.trim())){
            return "";
        }
        param = param.replaceFirst("F_","");
        param = param.toLowerCase();
        if(param.startsWith("_")){
            param = param.substring(1);
        }
        StringBuilder sb=new StringBuilder(param);
        Matcher mc= Pattern.compile("_").matcher(param);
        int i=0;
        while (mc.find()){
            int position=mc.end()-(i++);
            sb.replace(position-1,position+1,sb.substring(position,position+1).toUpperCase());
        }
        return sb.toString();
    }
}
