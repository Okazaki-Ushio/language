package com.yosang.language.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
* <!--poi-->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
            <version>4.0.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>3.15</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
            <version>5.1.10.RELEASE</version>
        </dependency>
*/

public class ExcelUtils {

    private final static String xls = "xls";
    private final static String xlsx = "xlsx";

    public static Workbook getWorkBook(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Workbook workbook = null;
        InputStream is = file.getInputStream();
        assert fileName != null;
        if(fileName.endsWith(xls)){
            workbook = new HSSFWorkbook(is);
        }else if(fileName.endsWith(xlsx)){
            workbook = new XSSFWorkbook(is);
        }
        is.close();
        return workbook;
    }

    public static List<List<String>> readExcel(File excel,Integer sheetIndex) throws IOException, InvalidFormatException {
        Workbook workbook = null;
        Sheet sheet;
        if (excel.getName().endsWith(xlsx)) {
            workbook = new XSSFWorkbook(excel);
        } else if (excel.getName().endsWith(xls)) {
            workbook = new HSSFWorkbook(new FileInputStream(excel));
        }
        assert workbook != null;
        sheet = workbook.getSheetAt(sheetIndex);
        int lastRowNum = sheet.getLastRowNum();
        List<List<String>> result = new ArrayList<>();
        for (int rowNum = 1; rowNum <= lastRowNum; rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                continue;
            }
            short firstCellNum = row.getFirstCellNum();
            short lastCellNum = row.getLastCellNum();
            List<String> list = new ArrayList<>();
            for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellType(CellType.STRING);
                String value = cell.getStringCellValue();
                list.add(value);
            }
            result.add(list);
        }
        workbook.close();
        return result;
    }

    //the keys of heads is heads,the value is the name of the field
    public static Workbook writeExcel(List data, LinkedHashMap<String, String> heads, Workbook workbook) throws IllegalAccessException {
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        for (int i = 0; i < heads.keySet().size(); i++) {
            Cell cell = row.createCell(i, CellType.STRING);
            cell.setCellStyle(getHeadCellStyle(workbook));
            cell.setCellValue(getEntryFromIndex(i, heads).getKey());
        }
        List<Field> fields = Arrays.asList(data.get(0).getClass().getDeclaredFields());
        Map<String, Field> nameFieldMap = fields.stream().collect(Collectors.toMap(Field::getName, Function.identity()));
        for (int i = 0; i < data.size(); i++) {
            Row dataRow = sheet.createRow(i + 1);
            for (int j = 0; j < heads.keySet().size(); j++) {
                Cell cell = dataRow.createCell(j, CellType.STRING);
                Field field = nameFieldMap.get(getEntryFromIndex(j, heads).getValue());
                field.setAccessible(true);
                //cell.setCellStyle(getDataStyle(workbook));
                if(field.get(data.get(i))!=null){
                    cell.setCellValue(field.get(data.get(i)).toString());
                }else {
                    cell.setCellValue("");
                }
            }
        }
        return workbook;
    }

    private static Map.Entry<String, String> getEntryFromIndex(int index, LinkedHashMap<String, String> treeMap) {
        Iterator<Map.Entry<String, String>> iterator = treeMap.entrySet().iterator();
        Map.Entry<String, String> result = null;
        for (int i = 0; iterator.hasNext(); i++) {
            result = iterator.next();
            if (i == index) {
                break;
            }
        }
        return result;
    }

    private static CellStyle getHeadCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);//水平居中
        cellStyle.setWrapText(true);//自动换行
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        Font font = workbook.createFont();
        font.setFontName("仿宋_GB2312");//字体样式
        font.setFontHeightInPoints((short) 10);//字体大小
        font.setBold(true);//加粗
        cellStyle.setFont(font);
        return cellStyle;
    }

    private static CellStyle getDataStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        Font font = workbook.createFont();
        font.setFontName("仿宋_GB2312");
        font.setFontHeightInPoints((short) 10);
        cellStyle.setFont(font);
        return cellStyle;
    }
}
