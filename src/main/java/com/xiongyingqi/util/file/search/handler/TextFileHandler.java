package com.xiongyingqi.util.file.search.handler;

import com.xiongyingqi.util.file.search.FileHandler;
import com.xiongyingqi.util.file.search.FileSearchResult;
import com.xiongyingqi.util.file.search.FileSearchResultMark;
import com.xiongyingqi.util.file.search.IFileHandler;

import java.io.*;
import java.util.*;

/**
 * 普通文本的支持
 * Created by 瑛琪<a href="http://xiongyingqi.com">xiongyingqi.com</a> on 2014/6/4 0004.
 */
public class TextFileHandler implements IFileHandler {
    @Override
    public boolean supportFileHandler(FileHandler fileHandler) {
        if(fileHandler.getCurrentFile() == null){
            return false;
        }
        if(fileHandler.getCurrentFile().getName().endsWith("txt")){
            return true;
        }
        return false;
    }

    @Override
    public FileSearchResult handlerFile(FileHandler fileHandler) {
        File file = fileHandler.getCurrentFile();
        FileSearchResult result = null;
        try {
//            FileInputStream fileInputStream = new FileInputStream(file);
//            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            Reader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            try {
                int lineNumber = 0;
                String line = null;
                while((line = bufferedReader.readLine()) != null){
                    Collection<FileSearchResultMark> fileSearchResultMarks = columnNumbersOfKeywords(fileHandler, line, lineNumber);

                    if(fileSearchResultMarks != null && fileSearchResultMarks.size() > 0){
                        if(result == null){
                            result = new FileSearchResult();
                        }

                        Collection<FileSearchResultMark> marks = result.getFileSearchResultMarks();
                        if(marks == null){
                            marks = new HashSet<FileSearchResultMark>();
                        }
                        if(fileSearchResultMarks != null){
                            marks.addAll(fileSearchResultMarks);
                            result.setFileSearchResultMarks(marks);
                            result.setFoundResult(result.getFoundResult() + fileSearchResultMarks.size());
                        }

                        result.setFile(fileHandler.getCurrentFile());
                    }

                    lineNumber++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Collection<FileSearchResultMark> columnNumbersOfKeywords(FileHandler fileHandler, String line, int lineNumber){
        Collection<String> keywords = fileHandler.getFileSearch().getKeywords();
        Map<String, Integer> keywordIndexMap = new HashMap<String, Integer>();
        Collection<FileSearchResultMark> fileSearchResultMarks = new ArrayList<FileSearchResultMark>();
//        int lastIndex = index;

        System.out.println("line ====== " + line);
        for (String keyword : keywords) {
            System.out.println("keyword ====== " + keyword);
            int index;
            if(keywordIndexMap.get(keyword) == null){
                keywordIndexMap.put(keyword, 0);
            }

            while((index = keywordIndexMap.get(keyword)) >= 0){
                index = line.indexOf(keyword, index);
                System.out.println("index ======= " + index);
                keywordIndexMap.put(keyword, index + keyword.length());
                if(index >= 0){
                    FileSearchResultMark fileSearchResultMark = new FileSearchResultMark();
                    fileSearchResultMark.setColumn(index);
                    fileSearchResultMark.setRow(lineNumber);
                    fileSearchResultMark.setKeyword(keyword);
                    fileSearchResultMarks.add(fileSearchResultMark);
                } else {
                    break;
                }
            }
        }
        return fileSearchResultMarks;
    }
}
