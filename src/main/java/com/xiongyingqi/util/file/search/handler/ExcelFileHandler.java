package com.xiongyingqi.util.file.search.handler;

import com.xiongyingqi.util.file.search.FileHandler;
import com.xiongyingqi.util.file.search.FileSearchResult;
import com.xiongyingqi.util.file.search.FileSearchResultMark;
import com.xiongyingqi.util.file.search.IFileHandler;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by 瑛琪<a href="http://xiongyingqi.com">xiongyingqi.com</a> on 2014/6/4 0004.
 */
public class ExcelFileHandler implements IFileHandler {
    @Override
    public boolean supportFileHandler(FileHandler fileHandler) {
        if(fileHandler.getCurrentFile() == null || fileHandler.getFileSearch().getKeywords() == null){
            return false;
        }
        String fileName = fileHandler.getCurrentFile().getName();
        if(fileName.endsWith("xls") || fileName.endsWith("xlsx")){
            return true;
        }

        return false;
    }

    @Override
    public FileSearchResult handlerFile(FileHandler fileHandler) {
        File file = fileHandler.getCurrentFile();
        Collection<String> keywords = fileHandler.getFileSearch().getKeywords();
        FileSearchResult result = null;
        try {
            Workbook workbook = WorkbookFactory.create(file);
            int sheetSize = workbook.getNumberOfSheets();
            for (int i = 0; i < sheetSize; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                if(sheet == null){
                    continue;
                }
                int rowSize = sheet.getLastRowNum();
                for (int i1 = 0; i1 < rowSize; i1++) {
                    Row row = sheet.getRow(i1);
                    if(row == null){
                        continue;
                    }
                    int cellSize = row.getLastCellNum();
                    for (int i2 = 0; i2 < cellSize; i2++) {
                        Cell cell = row.getCell(i2);
                        if(cell == null){
                            continue;
                        }
                        String value = getStringCellValue(cell);
                        for (String keyword : keywords) {// 检查关键字
                            if(value.contains(keyword)){
                                if(result == null){
                                    result = new FileSearchResult();
                                    result.setFile(file);
                                }
                                result.setFoundResult(result.getFoundResult() + 1);

                                Collection<FileSearchResultMark> marks = result.getFileSearchResultMarks();
                                if (marks == null) {
                                    marks = new HashSet<FileSearchResultMark>();
                                }
                                FileSearchResultMark mark = new FileSearchResultMark();
                                mark.setPage(i);
                                mark.setRow(i1);
                                mark.setColumn(i2);
                                mark.setKeyword(keyword);
                                marks.add(mark);
                                result.setFileSearchResultMarks(marks);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 获取单元格数据内容为字符串类型的数据
     *
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private String getStringCellValue(Cell cell) {
        String strCell = "";
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_BLANK:
                strCell = "";
                break;
            default:
                strCell = "";
                break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        if (cell == null) {
            return "";
        }
        return strCell;
    }

}
