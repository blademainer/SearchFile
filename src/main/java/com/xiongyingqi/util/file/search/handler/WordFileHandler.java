package com.xiongyingqi.util.file.search.handler;

import com.xiongyingqi.util.file.search.FileHandler;
import com.xiongyingqi.util.file.search.FileSearchResult;
import com.xiongyingqi.util.file.search.FileSearchResultMark;
import com.xiongyingqi.util.file.search.IFileHandler;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by 瑛琪<a href="http://xiongyingqi.com">xiongyingqi.com</a> on 2014/6/4 0004.
 */
public class WordFileHandler implements IFileHandler {
    @Override
    public boolean supportFileHandler(FileHandler fileHandler) {
        if (fileHandler.getCurrentFile() == null || fileHandler.getFileSearch().getKeywords() == null) {
            return false;
        }
        String fileName = fileHandler.getCurrentFile().getName();
        if (fileName.endsWith("doc") || fileName.endsWith("docx")) {
            return true;
        }
        return false;
    }

    @Override
    public FileSearchResult handlerFile(FileHandler fileHandler) {
        File file = fileHandler.getCurrentFile();
        FileSearchResult result = null;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            if (file.getName().endsWith("doc")) {
                result = handlerWord(inputStream, fileHandler);
            } else {
                result = handlerXWord(inputStream, fileHandler);
            }

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    private FileSearchResult handlerWord(FileInputStream inputStream, FileHandler handler) {
        Collection<String> keywords = handler.getFileSearch().getKeywords();
        FileSearchResult result = null;
        try {
            HWPFDocument document = new HWPFDocument(inputStream);
//            String text = document.getDocumentText();
            Range range = document.getRange();
            if (range == null) {
                return result;
            }
            int paragraphSize = range.numParagraphs();
            for (int i = 0; i < paragraphSize; i++) {
                Paragraph paragraph = range.getParagraph(i);
                if (paragraph == null) {
                    continue;
                }
                int sectionSize = paragraph.numSections();
                for (int i1 = 0; i1 < sectionSize; i1++) {
                    Section section = paragraph.getSection(i1);
                    if (section == null) {
                        continue;
                    }
                    String sectionText = section.text();
                    for (String keyword : keywords) {

                        if (sectionText.contains(keyword)) {
                            if(result == null){
                                result = new FileSearchResult();
                                result.setFile(handler.getCurrentFile());
                            }
                            result.setFoundResult(result.getFoundResult() + 1);

                            Collection<FileSearchResultMark> marks = result.getFileSearchResultMarks();
                            if (marks == null) {
                                marks = new HashSet<FileSearchResultMark>();
                            }
                            FileSearchResultMark mark = new FileSearchResultMark();
                            mark.setKeyword(keyword);
                            mark.setRow(i);
                            mark.setColumn(i1);
                            marks.add(mark);
                            result.setFileSearchResultMarks(marks);
                        }
                    }
                }
            }

//            EntityHelper.print(text);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private FileSearchResult handlerXWord(FileInputStream inputStream, FileHandler handler) {
        Collection<String> keywords = handler.getFileSearch().getKeywords();
        FileSearchResult result = null;
        try {
            XWPFDocument document = new XWPFDocument(inputStream);
            List<XWPFParagraph> paragraphs = document.getParagraphs();

            for (XWPFParagraph paragraph : paragraphs) {
                String text = paragraph.getText();
                for (String keyword : keywords) {
                    if (text.contains(keyword)) {
                        if(result == null){
                            result = new FileSearchResult();
                            result.setFile(handler.getCurrentFile());
                        }
                        result.setFoundResult(result.getFoundResult() + 1);

                        Collection<FileSearchResultMark> marks = result.getFileSearchResultMarks();
                        if (marks == null) {
                            marks = new HashSet<FileSearchResultMark>();
                        }
                        FileSearchResultMark mark = new FileSearchResultMark();
                        mark.setKeyword(keyword);
                        marks.add(mark);
                        result.setFileSearchResultMarks(marks);
                    }
                }
            }

            List<XWPFTable> tables = document.getTables();
            for (XWPFTable table : tables) {
                if (table == null) {
                    continue;
                }
                int rowSize = table.getNumberOfRows();
                for (int i = 0; i < rowSize; i++) {
                    XWPFTableRow row = table.getRow(i);
                    if (row == null) {
                        continue;
                    }
                    List<XWPFTableCell> cells = row.getTableCells();
                    for (XWPFTableCell cell : cells) {
                        String text = cell.getText();
                        for (String keyword : keywords) {
                            if (text.contains(keyword)) {
                                if(result == null){
                                    result = new FileSearchResult();
                                    result.setFile(handler.getCurrentFile());
                                }
                                result.setFoundResult(result.getFoundResult() + 1);

                                Collection<FileSearchResultMark> marks = result.getFileSearchResultMarks();
                                if (marks == null) {
                                    marks = new HashSet<FileSearchResultMark>();
                                }
                                FileSearchResultMark mark = new FileSearchResultMark();
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
        }
        return result;
    }
}
