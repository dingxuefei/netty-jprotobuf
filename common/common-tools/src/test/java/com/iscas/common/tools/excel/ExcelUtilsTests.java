package com.iscas.common.tools.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.util.*;

/**
 * Excel操作工具类测试
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/13 19:30
 * @since jdk1.8
 */
public class ExcelUtilsTests {
    private static ExcelUtils.ExcelResult<Model> excelResult = new ExcelUtils.ExcelResult<>();
    private static LinkedHashMap<String, String> header = new LinkedHashMap<>();
    static {
        header.put("a", "列1");
        header.put("b", "列2");
        header.put("c", "列3");
        excelResult.setHeader(header);
        excelResult.setSheetName("sheet1");
        Model model1 = new Model("AW","WE","为我国");
        Model model2 = new Model("AWweg","WsdE","为g我国");
        Model model3 = new Model("AWsds","sdWE","为我weg国");
        List<Model> models = Arrays.asList(model1,model2,model3);
        excelResult.setContent(models);
    }

    /**创建一个测试Bean*/
    static class Model{
        private String a;
        private String b;
        private String c;
        public Model(String a, String b, String c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public Model() {
        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }
    }
    /**
     * 测试将数据写入xls格式Excel文件
     * */
    @Test
    @Ignore
    public void test1() throws Exception {
        String path = "d:/test.xls";
        ExcelUtils.exportXLSExcel(Arrays.asList(excelResult),69,path);
    }

    /**
     * 测试将数据写入xls格式Excel输出流
     * */
    @Test
    @Ignore
    public void test3() throws Exception {
        String path = "d:/test1.xls";
        File file = new File(path);
        OutputStream os = new FileOutputStream(file);
        ExcelUtils.exportXLSExcel(Arrays.asList(excelResult),69, os);
        os.close();
    }
    /**
     * 测试将数据写入xlsx格式Excel文件
     * */
    @Test
    @Ignore
    public void test2() throws Exception {
        String path = "d:/test.xlsx";
        ExcelUtils.exportXLSXExcel(Arrays.asList(excelResult),79,path);
    }

    /**
     * 测试将数据写入xlsx格式Excel输出流
     * */
    @Test
    @Ignore
    public void test4() throws Exception {
        String path = "d:/test1.xlsx";
        File file = new File(path);
        OutputStream os = new FileOutputStream(file);
        ExcelUtils.exportXLSXExcel(Arrays.asList(excelResult),79,os);
        os.close();
    }

    /**
     * 读取excel文件的输入流到一个Map中，key为sheet的名字
      */
    @Test
    @Ignore
    public void test5() throws IOException, InvalidFormatException, ExcelUtils.ExcelHandlerException {
        Map<String, List> result = new HashMap<>();
        String path = "e:/aaa.xlsx";
        ExcelUtils.readExcelToListMap(path,result);
        System.out.println(result);
        Assert.assertNotNull(result);
    }

    @Test
    @Ignore
    public void test6() throws Exception {
        LinkedHashMap<String, List<String>> result = ExcelUtils.readExcelHeader(new File("E:/aaa.xlsx"));
        System.out.println(result);
    }

    @Test
    @Ignore
    public void test7() throws Exception {
//
    }
}
