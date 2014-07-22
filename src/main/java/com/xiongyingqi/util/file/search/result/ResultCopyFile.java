package com.xiongyingqi.util.file.search.result;

import com.xiongyingqi.util.EntityHelper;
import com.xiongyingqi.util.FileHelper;
import com.xiongyingqi.util.file.search.FileSearchObserver;
import com.xiongyingqi.util.file.search.FileSearchResult;

import java.io.File;
import java.io.IOException;

/**
 * Created by 瑛琪<a href="http://xiongyingqi.com">xiongyingqi.com</a> on 2014/7/22 0022.
 */
public class ResultCopyFile implements FileSearchObserver {
    /**
     * 回收文件夹
     */
    private File trashFolder;

    public ResultCopyFile() {
        this(new File("_____copy_____"));
    }

    public ResultCopyFile(File trashFolder) {
        this.trashFolder = trashFolder;
        if (trashFolder.exists()) {
            trashFolder.mkdirs();
        }
        try {
            EntityHelper.print(trashFolder.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void findFile(FileSearchResult result) {
        EntityHelper.print(result);

        File file = result.getFile();
//        boolean isDelete = file.delete();

        try {
            if (file.getCanonicalPath().startsWith(trashFolder.getCanonicalPath())) {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileName = file.toString();
        fileName = fileName.replaceAll(":", "");
        File toMoveFile = new File(this.trashFolder, fileName);
        FileHelper.copyFile(file, toMoveFile);
    }

}