package com.execel.demo.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;


import java.io.*;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author devon
 * @version V1.0
 * @Description: TODO
 * @date ${date} ${time}
 */
public class ExecelUtil {

    public static HSSFWorkbook createExecel(String sheetName, String[] headers) {

        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        Sheet sheet = hssfWorkbook.createSheet(sheetName);
        Row row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
        }
        return hssfWorkbook;
    }


    public static byte[] createExecelWithbytes(String sheetName, String[] headers) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        Sheet sheet = hssfWorkbook.createSheet(sheetName);
        Row row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
        }
        byte[] bytes = null;
        try {
            hssfWorkbook.write(byteArrayOutputStream);
            bytes = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            byteArrayOutputStream.close();
        }

        return bytes;
    }

    public void createExecelTemplateById(String templateId) {
        String templateName = null;
       ResourceBundle resourceBundle = ResourceBundle.getBundle(templateId);
        String path = System.getProperty("user.dir");
        File file = new File(path, templateId + ".xml");
        SAXBuilder builder = new SAXBuilder();
        try {
            //解析xml文件
            Document parse = builder.build(file);
            //创建Excel
            HSSFWorkbook wb = new HSSFWorkbook();
            //创建sheet
            HSSFSheet sheet = wb.createSheet("Sheet0");

            //获取xml文件跟节点
            Element root = parse.getRootElement();
            //获取模板名称
            templateName = root.getAttribute("name").getValue();

            int rownum = 0;
            int column = 0;
            //设置列宽
            Element colgroup = root.getChild("colgroup");
            setColumnWidth(sheet, colgroup);

            //设置标题
            Element title = root.getChild("title");
            List<Element> trs = title.getChildren("tr");
            for (int i = 0; i < trs.size(); i++) {
                Element tr = trs.get(i);
                List<Element> tds = tr.getChildren("td");
                HSSFRow row = sheet.createRow(rownum);
                HSSFCellStyle cellStyle = wb.createCellStyle();
                cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                for (column = 0; column < tds.size(); column++) {
                    Element td = tds.get(column);
                    HSSFCell cell = row.createCell(column);
                    Attribute rowSpan = td.getAttribute("rowspan");
                    Attribute colSpan = td.getAttribute("colspan");
                    Attribute value = td.getAttribute("value");
                    if (value != null) {
                        String val = value.getValue();
                        cell.setCellValue(val);
                        int rspan = rowSpan.getIntValue() - 1;
                        int cspan = colSpan.getIntValue() - 1;

                        //设置字体
                        HSSFFont font = wb.createFont();
                        font.setFontName("仿宋_GB2312");
                        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//字体加粗
//						font.setFontHeight((short)12);
                        font.setFontHeightInPoints((short) 12);
                        cellStyle.setFont(font);
                        cell.setCellStyle(cellStyle);
                        //合并单元格居中
                        sheet.addMergedRegion(new CellRangeAddress(rspan, rspan, 0, cspan));
                    }
                }
                rownum++;
            }
            //设置表头
            Element thead = root.getChild("thead");
            trs = thead.getChildren("tr");
            for (int i = 0; i < trs.size(); i++) {
                Element tr = trs.get(i);
                HSSFRow row = sheet.createRow(rownum);
                List<Element> ths = tr.getChildren("th");
                for (column = 0; column < ths.size(); column++) {
                    Element th = ths.get(column);
                    Attribute valueAttr = th.getAttribute("value");
                    HSSFCell cell = row.createCell(column);
                    if (valueAttr != null) {
                        String value = valueAttr.getValue();
                        cell.setCellValue(value);
                    }
                }
                rownum++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void setColumnWidth(HSSFSheet sheet, Element colgroup) {
        List<Element> cols = colgroup.getChildren("col");
        for (int i = 0; i < cols.size(); i++) {
            Element col = cols.get(i);
            Attribute width = col.getAttribute("width");
            String unit = width.getValue().replaceAll("[0-9,\\.]", "");
            String value = width.getValue().replaceAll(unit, "");
            int v=0;
            if(StringUtils.isBlank(unit) || "px".endsWith(unit)){
                v = Math.round(Float.parseFloat(value) * 37F);
            }else if ("em".endsWith(unit)){
                v = Math.round(Float.parseFloat(value) * 267.5F);
            }
            sheet.setColumnWidth(i, v);
        }
    }

}
