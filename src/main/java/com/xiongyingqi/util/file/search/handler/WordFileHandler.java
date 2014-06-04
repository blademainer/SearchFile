package com.xiongyingqi.util.file.search.handler;

import com.xiongyingqi.util.file.search.FileHandler;
import com.xiongyingqi.util.file.search.FileSearchResult;
import com.xiongyingqi.util.file.search.IFileHandler;

/**
 * Created by 瑛琪<a href="http://xiongyingqi.com">xiongyingqi.com</a> on 2014/6/4 0004.
 */
public class WordFileHandler implements IFileHandler {
    @Override
    public boolean supportFileHandler(FileHandler fileHandler) {
        if(fileHandler.getCurrentFile() == null){
            return false;
        }
        return false;
    }

    @Override
    public FileSearchResult handlerFile(FileHandler fileHandler) {
        return null;
    }
}
