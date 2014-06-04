package com.xiongyingqi.util.file.search;

import com.xiongyingqi.util.EntityHelper;

import java.io.File;
import java.util.Collection;

/**
 * Created by 瑛琪<a href="http://xiongyingqi.com">xiongyingqi.com</a> on 2014/6/4 0004.
 */
public class FileSearchResult extends EntityHelper {
    private File file;
    private int foundResult;
    private Collection<FileSearchResultMark> fileSearchResultMarks;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getFoundResult() {
        return foundResult;
    }

    public void setFoundResult(int foundResult) {
        this.foundResult = foundResult;
    }

    public Collection<FileSearchResultMark> getFileSearchResultMarks() {
        return fileSearchResultMarks;
    }

    public void setFileSearchResultMarks(Collection<FileSearchResultMark> fileSearchResultMarks) {
        this.fileSearchResultMarks = fileSearchResultMarks;

    }

}
