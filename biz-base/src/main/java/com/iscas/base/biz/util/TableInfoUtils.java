package com.iscas.base.biz.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iscas.templet.view.table.TableHeaderResponseData;
import lombok.Cleanup;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 读取表头信息
 *
 * @author zhuquanwen
 * @date 2017/12/28 13:58
 **/
public class TableInfoUtils {
    public static Map<String, TableHeaderResponseData> tableHeaderInfoMap =
            new ConcurrentHashMap<String,TableHeaderResponseData>();
    private TableInfoUtils(){

    }
    /*获得tableHeader,（线程安全）*/
    public static TableHeaderResponseData getTableHeader(String tableName) throws IOException {
        TableHeaderResponseData tableHeaderResponseData = tableHeaderInfoMap.get(tableName);
        if(tableHeaderResponseData == null){
            readHeaderToMap(tableName);
        }
        return tableHeaderInfoMap.get(tableName).clone();
    }
    /*读取JSON的Header至内存，（线程安全）*/
    public static void readHeaderToMap(String tableName) throws IOException {
        Resource resource = new ClassPathResource("tableInfo/" + tableName + ".json");
        @Cleanup InputStream is  = resource.getInputStream();
        @Cleanup InputStreamReader inputStreamReader = new InputStreamReader(is, "utf-8");


        @Cleanup BufferedReader br = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        ObjectMapper mapper = new ObjectMapper();
        TableHeaderResponseData headerData = mapper.readValue(sb.toString(),
                TableHeaderResponseData.class);
        tableHeaderInfoMap.put(tableName, headerData);

    }

}

