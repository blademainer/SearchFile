package com.xiongyingqi.util.file.search.handler;

import com.xiongyingqi.util.file.search.FileHandler;
import com.xiongyingqi.util.file.search.FileSearchResult;
import com.xiongyingqi.util.file.search.IFileHandler;

import java.util.Collection;

/**
 * Created by 瑛琪<a href="http://xiongyingqi.com">xiongyingqi.com</a> on 2014/6/4 0004.
 */
public class FileNameHandler implements IFileHandler {
    @Override
    public boolean supportFileHandler(FileHandler fileHandler) {
//        System.out.println(" ------------ FileNameHandler --------------- ");
        if (fileHandler.getCurrentFile() == null || fileHandler.getFileSearch().getKeywords() == null) {
            return false;
        }
        Collection<String> keywords = fileHandler.getFileSearch().getKeywords();
        String fileName = fileHandler.getCurrentFile().getName();
        for (String keyword : keywords) {
            if(fileName.contains(keyword)){
                return true;
            }
        }
        return false;
    }

    @Override
    public FileSearchResult handlerFile(FileHandler fileHandler) {
        FileSearchResult result = new FileSearchResult();
        Collection<String> keywords = fileHandler.getFileSearch().getKeywords();
        String fileName = fileHandler.getCurrentFile().getName();
        int found = 0;
        for (String keyword : keywords) {
            if(fileName.contains(keyword)){
                found++;
            }
        }
        result.setFoundResult(found);
        result.setFile(fileHandler.getCurrentFile());
        return result;
    }
}
