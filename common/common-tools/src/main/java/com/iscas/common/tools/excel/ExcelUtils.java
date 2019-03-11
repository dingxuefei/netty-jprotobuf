package com.iscas.common.tools.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <p>Excel操作工具类<p/>
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/13 18:16
 * @since jdk1.8
 */
public class ExcelUtils {

    private ExcelUtils(){}
    public static class ExcelHandlerException extends Exception{
        public ExcelHandlerException() {
            super();
        }

        public ExcelHandlerException(String message) {
            super(message);
        }

        public ExcelHandlerException(String message, Throwable cause) {
            super(message, cause);
        }

        public ExcelHandlerException(Throwable cause) {
            super(cause);
        }

        protected ExcelHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }

    /**
     * Excel结果类bean
     * */
    public static class ExcelResult<T>{
        /**
         * sheet名称
         * */
        private String sheetName;
        /**表头键值对 key : en ; value :ch*/
        private LinkedHashMap<String,String> header;
        /**Excel数据*/
        private List<T> content;
        /**列的样式*/
        private LinkedHashMap<String,Object> cellStyle;
        /**表头的样式*/
        private Object headerStyle;
        public String getSheetName() {
            return sheetName;
        }
        public void setSheetName(String sheetName) {
            this.sheetName = sheetName;
        }
        public LinkedHashMap<String, String> getHeader() {
            return header;
        }
        public void setHeader(LinkedHashMap<String, String> header) {
            this.header = header;
        }
        public List<T> getContent() {
            return content;
        }
        public void setContent(List<T> content) {
            this.content = content;
        }
        public LinkedHashMap<String, Object> getCellStyle() {
            return cellStyle;
        }
        public void setCellStyle(LinkedHashMap<String, Object> cellStyle) {
            this.cellStyle = cellStyle;
        }
        public Object getHeaderStyle() {
            return headerStyle;
        }
        public void setHeaderStyle(Object headerStyle) {
            this.headerStyle = headerStyle;
        }
    }
    /**未定义的字段*/
    public static String NO_DEFINE = "no_define";
    public static int DEFAULT_COLOUMN_WIDTH = 17;


    /**
     * Excel写入文件,支持xls
     * @version 1.0
     * @since jdk1.8
     * @date 2018/7/14
     * @param excelResults excel数据
     * @param path 输出路径
     * @throws Exception
     * @return void
     */
    public static <T> void exportXLSExcel(List<ExcelResult<T>> excelResults , String path) throws Exception{
        File file = new File(path);
        OutputStream out = new FileOutputStream(file);
        exportXLSExcel(excelResults,DEFAULT_COLOUMN_WIDTH,out);
    }


    /**
     * Excel写入文件,支持xls
     * @version 1.0
     * @since jdk1.8
     * @date 2018/7/14
     * @param excelResults excel数据
     * @param colWidth 列宽
     * @param path 输出路径
     * @throws Exception
     * @return void
     */
    public static <T> void exportXLSExcel(List<ExcelResult<T>> excelResults,int colWidth,
                                          String path) throws Exception{
        File file = new File(path);
        OutputStream out = new FileOutputStream(file);
        exportXLSExcel(excelResults,colWidth,out);
    }

    /**
     * Excel写入文件，支持xlsx
     * @version 1.0
     * @since jdk1.8
     * @date 2018/7/13
     * @param excelResults excel数据
     * @param path 输出路径
     * @throws Exception
     * @return void
     */
    public static <T> void exportXLSXExcel(List<ExcelResult<T>> excelResults ,
                                           String path) throws Exception{
        File file = new File(path);
        OutputStream out = new FileOutputStream(file);
        exportXLSXExcel(excelResults, DEFAULT_COLOUMN_WIDTH, out);
    }

    /**
     * Excel写入文件，支持xlsx
     * @version 1.0
     * @since jdk1.8
     * @date 2018/7/13
     * @param excelResults excel数据
     * @param colWidth 列宽
     * @param path 输出路径
     * @throws Exception
     * @return void
     */
    public static <T> void exportXLSXExcel(List<ExcelResult<T>> excelResults , int colWidth,
                                           String path) throws Exception{
        File file = new File(path);
        OutputStream out = new FileOutputStream(file);
        exportXLSXExcel(excelResults,colWidth,out);
    }

    /**
     * Excel写入输出流，支持xlsx
     * @version 1.0
     * @since jdk1.8
     * @date 2018/7/13
     * @param excelResults excel数据
     * @param colWidth 列宽
     * @param out {@link OutputStream} 输出流
     * @throws Exception
     * @return void
     */
    public static <T> void exportXLSXExcel(List<ExcelResult<T>> excelResults ,int colWidth, OutputStream out) throws ExcelHandlerException {
        // 声明一个工作薄
        SXSSFWorkbook   workbook = new SXSSFWorkbook();
        try {
            for (Iterator<ExcelResult<T>> i = excelResults.iterator(); i.hasNext(); ) {
                ExcelResult<T> excelResult = i.next();
                List<T> list = excelResult.getContent();
                LinkedHashMap<String, String> headerMap = excelResult.getHeader();
                LinkedHashMap<String, ?> styleMap = excelResult.getCellStyle();
                CellStyle headerStyle = (CellStyle) excelResult.getHeaderStyle();
                Sheet sheet = workbook.createSheet(excelResult.getSheetName());
                int minBytes = colWidth < DEFAULT_COLOUMN_WIDTH ? DEFAULT_COLOUMN_WIDTH : colWidth;//至少字节数
                int[] arrColWidth = new int[headerMap.size()];
                // 产生表格标题行,以及设置列宽
                String[] properties = new String[headerMap.size()];
                String[] headers = new String[headerMap.size()];
                int ii = 0;
                for (Iterator<String> iter = headerMap.keySet().iterator(); iter.hasNext(); ) {
                    String fieldName = iter.next();
                    properties[ii] = fieldName;
                    headers[ii] = fieldName;
                    int bytes = fieldName.getBytes().length;
                    arrColWidth[ii] = bytes < minBytes ? minBytes : bytes;
                    sheet.setColumnWidth(ii, arrColWidth[ii] * 256);
                    ii++;
                }
                Row headerRow = sheet.createRow(0); //列头 rowIndex =1
                for (int j = 0; j < headers.length; j++) {
                    headerRow.createCell(j).setCellValue(headerMap.get(headers[j]));
                    if (headerStyle != null) {
                        headerRow.getCell(j).setCellStyle(headerStyle);
                    } else {
                        CellStyle cellStyle = workbook.createCellStyle();
                        DataFormat format = workbook.createDataFormat();
                        cellStyle.setDataFormat(format.getFormat("@"));
                        headerRow.getCell(j).setCellStyle(cellStyle);
                        headerRow.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                    }
                }
                if (list != null && list.size() > 0) {
                    for (int m = 0; m < list.size(); m++) {
                        Row dataRow = sheet.createRow(m + 1);
                        T t = list.get(m);
                        for (int j = 0; j < headers.length; j++) {
                            Cell newCell = dataRow.createCell(j);
                            Object cellValue = "";
                            if (t instanceof Map) {
                                cellValue = ((Map) t).get(headers[j]);
                            } else {
                                //如果是Java对象，利用反射
                                PropertyDescriptor pd = new PropertyDescriptor(headers[j], t.getClass());
                                Method getMethod = pd.getReadMethod();//获得get方法
                                cellValue = getMethod.invoke(t);//执行get方法返回一个Object
                            }
                            newCell.setCellValue(cellValue == null ? "" : String.valueOf(cellValue));
                            if (styleMap != null && styleMap.get(headers[j]) != null) {
                                newCell.setCellStyle((CellStyle) styleMap.get(headers[j]));
                            } else {
                                CellStyle cellStyle = workbook.createCellStyle();
                                DataFormat format = workbook.createDataFormat();
                                cellStyle.setDataFormat(format.getFormat("@"));
                                newCell.setCellStyle(cellStyle);
                                newCell.setCellType(Cell.CELL_TYPE_STRING);
                            }
                        }
                    }
                }
            }
            workbook.write(out);
            out.flush();
        } catch (Exception e){
            throw new ExcelHandlerException(e);
        }
    }

    /**
     * Excel写入输出流，支持xls
     * @version 1.0
     * @since jdk1.8
     * @date 2018/7/13
     * @param results excel数据
     * @param colWidth 列宽
     * @param out {@link OutputStream} 输出流
     * @throws Exception
     * @return void
     */
    public static <T> void exportXLSExcel(List<ExcelResult<T>> results ,int colWidth,
                                          OutputStream out) throws ExcelHandlerException {
        try {
            // 声明一个工作薄
            HSSFWorkbook workbook = new HSSFWorkbook();
            for (Iterator<ExcelResult<T>> i = results.iterator(); i.hasNext(); ) {
                ExcelResult<T> excelResult = i.next();
                List<T> list = excelResult.getContent();
                if (list != null && list.size() > 0 && list.size() > 65534) {
                    throw new ExcelHandlerException("Excel超过了允许的大小");
                }
                LinkedHashMap<String, String> headerMap = excelResult.getHeader();
                LinkedHashMap<String, ?> styleMap = excelResult.getCellStyle();
                HSSFCellStyle headerStyle = (HSSFCellStyle) excelResult.getHeaderStyle();
                HSSFSheet sheet = workbook.createSheet(excelResult.getSheetName());
                int minBytes = colWidth < DEFAULT_COLOUMN_WIDTH ? DEFAULT_COLOUMN_WIDTH : colWidth;//至少字节数
                int[] arrColWidth = new int[headerMap.size()];
                // 产生表格标题行,以及设置列宽
                String[] properties = new String[headerMap.size()];
                String[] headers = new String[headerMap.size()];
                int ii = 0;
                for (Iterator<String> iter = headerMap.keySet().iterator(); iter.hasNext(); ) {
                    String fieldName = iter.next();
                    properties[ii] = fieldName;
                    headers[ii] = fieldName;
                    int bytes = fieldName.getBytes().length;
                    arrColWidth[ii] = bytes < minBytes ? minBytes : bytes;
                    sheet.setColumnWidth(ii, arrColWidth[ii] * 256);
                    ii++;
                }
                HSSFRow headerRow = sheet.createRow(0); //列头 rowIndex =1
                for (int j = 0; j < headers.length; j++) {
                    headerRow.createCell(j).setCellValue(headerMap.get(headers[j]));
                    if (headerStyle != null) {
                        headerRow.getCell(j).setCellStyle(headerStyle);
                    } else {
                        HSSFCellStyle cellStyle = workbook.createCellStyle();
                        DataFormat format = workbook.createDataFormat();
                        cellStyle.setDataFormat(format.getFormat("@"));
                        headerRow.getCell(j).setCellStyle(cellStyle);
                        headerRow.getCell(j).setCellType(HSSFCell.CELL_TYPE_STRING);
                    }
                }
                if (list != null && list.size() > 0) {
                    for (int m = 0; m < list.size(); m++) {
                        HSSFRow dataRow = sheet.createRow(m + 1);
                        T t = list.get(m);
                        for (int j = 0; j < headers.length; j++) {
                            HSSFCell newCell = dataRow.createCell(j);
                            Object cellValue = "";
                            if (t instanceof Map) {
                                cellValue = ((Map) t).get(headers[j]);
                            } else {
                                //如果是Java对象，利用反射
                                PropertyDescriptor pd = new PropertyDescriptor(headers[j], t.getClass());
                                Method getMethod = pd.getReadMethod();//获得get方法
                                cellValue = getMethod.invoke(t);//执行get方法返回一个Object
                            }
                            newCell.setCellValue(cellValue == null ? "" : String.valueOf(cellValue));
                            if (styleMap != null && styleMap.get(headers[j]) != null) {
                                newCell.setCellStyle((HSSFCellStyle) styleMap.get(headers[j]));
                            } else {
                                HSSFCellStyle cellStyle = workbook.createCellStyle();
                                HSSFDataFormat format = workbook.createDataFormat();
                                cellStyle.setDataFormat(format.getFormat("@"));
                                newCell.setCellStyle(cellStyle);
                                newCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                            }
                        }
                    }
                }
            }

            workbook.write(out);
            out.flush();
        }catch (ExcelHandlerException e){
            throw e;
        } catch (Exception e){
            throw new ExcelHandlerException(e);
        }
    }






    public static Map<String, Object> readExcelWithHeader(InputStream inputStream) throws ExcelHandlerException {
        Map<String, Object> result = new HashMap<>();
        // IO流读取文件
        try {
            Workbook wb = WorkbookFactory.create(inputStream);
            if (wb instanceof XSSFWorkbook) {
                XSSFWorkbook xssfWb = (XSSFWorkbook) wb;
                LinkedHashMap<String, List<String>> headerMap = readXLSXHeader(xssfWb);
                LinkedHashMap<String, List<String>> data = readXLSXHeader(xssfWb);
                result.put("header", headerMap);
                result.put("data", data);
            } else if (wb instanceof HSSFWorkbook) {
                HSSFWorkbook hssfWb = (HSSFWorkbook) wb;
                LinkedHashMap<String, List<String>> headerMap = readXLSHeader(hssfWb);
                LinkedHashMap<String, List<String>> data = readXLSHeader(hssfWb);
                result.put("header", headerMap);
                result.put("data", data);
            }
        }catch (Exception e) {
            throw new ExcelHandlerException(e);
        }

        return result;
    }


    /**
     * 将一个文件输入的的Excel表头读出来
     * @version 1.0
     * @since jdk1.8
     * @date 2018/9/18
     * @param file 文件
     * @throws
     * @return java.util.List<java.lang.String>
     */
    public static LinkedHashMap<String, List<String>> readExcelHeader(File file) throws Exception{
        InputStream is = new FileInputStream(file);
        return readExcelHeader(is);
    }

    /**
     * 将一个输入流输入的的Excel表头读出来
     * @version 1.0
     * @since jdk1.8
     * @date 2018/9/18
     * @param is 输入流
     * @throws
     * @return java.util.List<java.lang.String>
     */
    public static LinkedHashMap<String, List<String>> readExcelHeader(InputStream is) throws ExcelHandlerException {
        LinkedHashMap<String, List<String>> headerMap = new LinkedHashMap<>();
        try {
            Workbook wb = WorkbookFactory.create(is);
            if (wb instanceof XSSFWorkbook) {
                headerMap = readXLSXHeader((XSSFWorkbook) wb);
            } else if (wb instanceof HSSFWorkbook) {
                headerMap = readXLSHeader((HSSFWorkbook) wb);
            }
        } catch (Exception e){
            throw new ExcelHandlerException(e);
        }
        return headerMap;
    }

    public static void readExcelToListMap(String path, Map<String, List> resultMap) throws ExcelHandlerException {

        try (
                InputStream is = new FileInputStream(path);
                ){
            readExcelToListMap(is, resultMap);
        } catch (Exception e) {
            throw new ExcelHandlerException(e);
        }
    }



    /**
     * 将EXCEL读取到map中<br/>
     * map以sheet名称为key,以List<Map>为值
     *
     * @version 1.0
     * @since jdk1.8
     * @date 2018/7/14
     * @param inputStream 文件输入流
     * @param resultMap 结果
     * @throws
     * @return void
     */
    public static void readExcelToListMap(InputStream inputStream, Map<String, List> resultMap) throws ExcelHandlerException {
        Workbook wb = null;
        try {
            wb = WorkbookFactory.create(inputStream);
            if (wb instanceof XSSFWorkbook) {
                readXLSXToListMap((XSSFWorkbook) wb, resultMap);
            } else if (wb instanceof HSSFWorkbook) {
                readXLSToListMap((HSSFWorkbook) wb, resultMap);
            } else {
                throw new ExcelHandlerException("excel文件格式错误，必须是xls或xlsx格式");
            }
        } catch (ExcelHandlerException e) {
            throw e;
        } catch (Exception e) {
            throw new ExcelHandlerException(e);
        }

    }

    private static LinkedHashMap<String, List<String>> readXLSHeader(HSSFWorkbook hssfWb){
        LinkedHashMap<String, List<String>> headerMap = new LinkedHashMap<>();
        for (int i = 0; i < hssfWb.getNumberOfSheets() ; i++) {
            List<String> headers = new ArrayList<>();
            //读取sheet(页)
            HSSFSheet xssfSheet = hssfWb.getSheetAt(i);
            //读取第一行
            HSSFRow xssfRow = xssfSheet.getRow(0);
            if (xssfRow != null) {
                int totalCells = xssfRow.getLastCellNum();
                //读取列，从第一列开始
                for (int c = 0; c < totalCells; c++) {
                    HSSFCell cell = xssfRow.getCell(c);
                    if (cell == null) {
                        continue;
                    }

                    Object value = cell.getStringCellValue();
                    headers.add(String.valueOf(value));

                }
            }
            headerMap.put(xssfSheet.getSheetName(), headers);
        }
        return headerMap;
    }

    private static LinkedHashMap<String, List<String>> readXLSXHeader(XSSFWorkbook xssfWb){
        LinkedHashMap<String, List<String>> headerMap = new LinkedHashMap<>();
        for (int i = 0; i < xssfWb.getNumberOfSheets() ; i++) {
            List<String> headers = new ArrayList<>();
            //读取sheet(页)
            XSSFSheet xssfSheet = xssfWb.getSheetAt(0);
            //读取第一行
            XSSFRow xssfRow = xssfSheet.getRow(0);
            if (xssfRow != null) {
                int totalCells = xssfRow.getLastCellNum();
                //读取列，从第一列开始
                for (int c = 0; c < totalCells; c++) {
                    XSSFCell cell = xssfRow.getCell(c);
                    if (cell == null) {
                        continue;
                    }
                    Object value = cell.getStringCellValue();
                    headers.add(String.valueOf(value));
                }
            }
            headerMap.put(xssfSheet.getSheetName(), headers);
        }
        return headerMap;
    }

    private LinkedHashMap<String, List<LinkedHashMap>> readXLSXData(XSSFWorkbook wb){
        LinkedHashMap<String, List<LinkedHashMap>> result = new LinkedHashMap<>();
        for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
            List<LinkedHashMap> list = new ArrayList<>();
            XSSFSheet xssfSheet = wb.getSheetAt(numSheet);
            int totalRows = xssfSheet.getLastRowNum();
            //读取Row,从第二行开始
            for (int rowNum = 1; rowNum <= totalRows; rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    LinkedHashMap map = new LinkedHashMap();
                    int totalCells = xssfRow.getLastCellNum();
                    //读取列，从第一列开始
                    for (int c = 0; c < totalCells; c++) {
                        XSSFCell cell = xssfRow.getCell(c);
                        if (cell == null) {
                            continue;
                        }
                        Object value = null;
                        switch (cell.getCellType()) {
                            case XSSFCell.CELL_TYPE_NUMERIC:
                                value = cell.getNumericCellValue();
                                break;
                            case XSSFCell.CELL_TYPE_STRING: // 字符串
                                value = cell.getStringCellValue();
                                break;
                            case XSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                                value = cell.getBooleanCellValue();
                                break;
                            case XSSFCell.CELL_TYPE_FORMULA: // 公式
                                value = cell.getCellFormula() + "";
                                break;
                            case XSSFCell.CELL_TYPE_BLANK: // 空值
                                value = "";
                                break;
                            case XSSFCell.CELL_TYPE_ERROR: // 故障
                                value = "非法字符";
                                break;
                            default:
                                break;
                        }
                        String key = xssfSheet.getRow(0).getCell(c).getStringCellValue();
                        map.put(key, value);
                    }
                    Set set = map.keySet();
                    boolean addFlag = false;
                    for (Object obj : set) {
                        if (map.get(obj) != null && !"".equals(map.get(obj))) {
                            addFlag = true;
                        }
                    }
                    if (addFlag) {
                        list.add(map);
                    }
                }
            }
            result.put(xssfSheet.getSheetName(), list);
        }
        return result;
    }

    private LinkedHashMap<String, List<LinkedHashMap>> readXLSData(HSSFWorkbook wb){
        LinkedHashMap<String, List<LinkedHashMap>> result = new LinkedHashMap<>();
        for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
            List<LinkedHashMap> list = new ArrayList<>();
            HSSFSheet xssfSheet = wb.getSheetAt(numSheet);
            int totalRows = xssfSheet.getLastRowNum();
            //读取Row,从第二行开始
            for (int rowNum = 1; rowNum <= totalRows; rowNum++) {
                HSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    LinkedHashMap map = new LinkedHashMap();
                    int totalCells = xssfRow.getLastCellNum();
                    //读取列，从第一列开始
                    for (int c = 0; c < totalCells; c++) {
                        HSSFCell cell = xssfRow.getCell(c);
                        if (cell == null) {
                            continue;
                        }
                        Object value = null;
                        switch (cell.getCellType()) {
                            case XSSFCell.CELL_TYPE_NUMERIC:
                                value = cell.getNumericCellValue();
                                break;
                            case XSSFCell.CELL_TYPE_STRING: // 字符串
                                value = cell.getStringCellValue();
                                break;
                            case XSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                                value = cell.getBooleanCellValue();
                                break;
                            case XSSFCell.CELL_TYPE_FORMULA: // 公式
                                value = cell.getCellFormula() + "";
                                break;
                            case XSSFCell.CELL_TYPE_BLANK: // 空值
                                value = "";
                                break;
                            case XSSFCell.CELL_TYPE_ERROR: // 故障
                                value = "非法字符";
                                break;
                            default:
                                break;
                        }
                        String key = xssfSheet.getRow(0).getCell(c).getStringCellValue();
                        map.put(key, value);
                    }
                    Set set = map.keySet();
                    boolean addFlag = false;
                    for (Object obj : set) {
                        if (map.get(obj) != null && !"".equals(map.get(obj))) {
                            addFlag = true;
                        }
                    }
                    if (addFlag) {
                        list.add(map);
                    }
                }
            }
            result.put(xssfSheet.getSheetName(), list);
        }
        return result;
    }

    private static void readXLSXToListMap( XSSFWorkbook wb, Map<String, List> resultMap) {
        //读取sheet(页)
        for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
            List<Map> list = new ArrayList<Map>();
            XSSFSheet xssfSheet = wb.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            int totalRows = xssfSheet.getLastRowNum();
            //读取Row,从第二行开始
            for (int rowNum = 1; rowNum <= totalRows; rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    Map map = new LinkedHashMap();
                    int totalCells = xssfRow.getLastCellNum();
                    //读取列，从第一列开始
                    for (int c = 0; c < totalCells; c++) {
                        XSSFCell cell = xssfRow.getCell(c);
                        if (cell == null) {
                            continue;
                        }
                        Object value = null;
                        switch (cell.getCellType()) {
                            case XSSFCell.CELL_TYPE_NUMERIC:
                                value = cell.getNumericCellValue();
                                break;
                            case XSSFCell.CELL_TYPE_STRING: // 字符串
                                value = cell.getStringCellValue();
                                break;
                            case XSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                                value = cell.getBooleanCellValue();
                                break;
                            case XSSFCell.CELL_TYPE_FORMULA: // 公式
                                value = cell.getCellFormula() + "";
                                break;
                            case XSSFCell.CELL_TYPE_BLANK: // 空值
                                value = "";
                                break;
                            case XSSFCell.CELL_TYPE_ERROR: // 故障
                                value = "非法字符";
                                break;
                            default:
                                break;
                        }
                        String key = xssfSheet.getRow(0).getCell(c).getStringCellValue();
                        map.put(key, value);
                    }
                    Set set = map.keySet();
                    boolean addFlag = false;
                    for (Object obj : set) {
                        if (map.get(obj) != null && !"".equals(map.get(obj))) {
                            addFlag = true;
                        }
                    }
                    if (addFlag) {
                        list.add(map);
                    }
                }
            }
            if (resultMap.get(xssfSheet.getSheetName()) != null) {
                List<Map> listx = resultMap.get(xssfSheet.getSheetName());
                listx.addAll(list);
            } else {
                resultMap.put(xssfSheet.getSheetName(), list);
            }
        }

    }
    private static void  readXLSToListMap(HSSFWorkbook wb, Map<String, List> resultMap) {
        //读取sheet(页)
        for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
            List<Map> list = new ArrayList<Map>();
            HSSFSheet xssfSheet = wb.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            int totalRows = xssfSheet.getLastRowNum();
            //读取Row,从第二行开始
            for (int rowNum = 1; rowNum <= totalRows; rowNum++) {
                HSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    Map map = new LinkedHashMap();
                    int totalCells = xssfRow.getLastCellNum();
                    //读取列，从第一列开始
                    for (int c = 0; c < totalCells; c++) {
                        HSSFCell cell = xssfRow.getCell(c);
                        if (cell == null) {
                            continue;
                        }
                        Object value = null;
                        switch (cell.getCellType()) {
                            case HSSFCell.CELL_TYPE_NUMERIC:
                                value = cell.getNumericCellValue();
                                break;
                            case HSSFCell.CELL_TYPE_STRING: // 字符串
                                value = cell.getStringCellValue();
                                break;
                            case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                                value = cell.getBooleanCellValue();
                                break;
                            case HSSFCell.CELL_TYPE_FORMULA: // 公式
                                value = cell.getCellFormula() + "";
                                break;
                            case HSSFCell.CELL_TYPE_BLANK: // 空值
                                value = "";
                                break;
                            case HSSFCell.CELL_TYPE_ERROR: // 故障
                                value = "非法字符";
                                break;
                            default:
                                break;
                        }
                        String key = xssfSheet.getRow(0).getCell(c).getStringCellValue();
                        map.put(key, value);
                    }
                    Set set = map.keySet();
                    boolean addFlag = false;
                    for (Object obj : set) {
                        if (map.get(obj) != null && !"".equals(map.get(obj))) {
                            addFlag = true;
                        }
                    }
                    if (addFlag) {
                        list.add(map);
                    }
                }
            }
            if (resultMap.get(xssfSheet.getSheetName()) != null) {
                List<Map> listx = resultMap.get(xssfSheet.getSheetName());
                listx.addAll(list);
            } else {
                resultMap.put(xssfSheet.getSheetName(), list);
            }
        }

    }

    public static HSSFCellStyle getHSSFCellStyle(HSSFWorkbook workbook){
        // 单元格样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        HSSFFont cellFont = workbook.createFont();
//        cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        cellStyle.setFont(cellFont);
        return cellStyle;
    }
    public static CellStyle getSXSSFCellStyle(SXSSFWorkbook workbook){
        // 单元格样式
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font cellFont = workbook.createFont();
//        cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        cellStyle.setFont(cellFont);
        return cellStyle;
    }
    //样式
    public static HSSFCellStyle getHSSFHeaderStyle(HSSFWorkbook workbook){
        // 列头样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        HSSFFont headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
//        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        return headerStyle;
    }
    //样式
    public static CellStyle getSXSSFHeaderStyle(SXSSFWorkbook workbook){
        // 列头样式
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
//        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        return headerStyle;
    }
}
