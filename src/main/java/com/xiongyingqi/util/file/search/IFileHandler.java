package com.xiongyingqi.util.file.search;

/**
 * Created by 瑛琪<a href="http://xiongyingqi.com">xiongyingqi.com</a> on 2014/6/4 0004.
 */
public interface IFileHandler {
    public boolean supportFileHandler(FileHandler fileHandler);

    public FileSearchResult handlerFile(FileHandler fileHandler);
}
