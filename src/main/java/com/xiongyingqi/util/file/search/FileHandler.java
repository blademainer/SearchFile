package com.xiongyingqi.util.file.search;

import com.xiongyingqi.util.file.search.handler.ExcelFileHandler;
import com.xiongyingqi.util.file.search.handler.FileNameHandler;
import com.xiongyingqi.util.file.search.handler.TextFileHandler;
import com.xiongyingqi.util.file.search.handler.WordFileHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Created by 瑛琪<a href="http://xiongyingqi.com">xiongyingqi.com</a> on 2014/6/4 0004.
 */
public class FileHandler implements Runnable {
    private FileSearch fileSearch;
    private File currentFile;
    private static Collection<IFileHandler> fileHandlers = new ArrayList<IFileHandler>();

    static {
        fileHandlers.add(new FileNameHandler());
        fileHandlers.add(new TextFileHandler());
        fileHandlers.add(new WordFileHandler());
        fileHandlers.add(new ExcelFileHandler());
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        System.out.println("开始----------- " + Thread.currentThread());
        File file = null;
        do {
            try {
                file = FileSearch.BLOCKING_QUEUE.poll(5, TimeUnit.SECONDS);
//                System.out.println(file);
                if(file == null){
                    continue;
                }
                currentFile = file;
                handler();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (file != null);//FileSearch.BLOCKING_QUEUE.peek() != null
    }

    private void handler() {
        for (IFileHandler fileHandler : fileHandlers) {
            try {
                if (fileHandler.supportFileHandler(this)) {
                    FileSearchResult fileSearchResult = fileHandler.handlerFile(this);
                    if (fileSearchResult == null) {
                        continue;
                    }

                    try {
                        FileSearch.MESSAGE_BLOCKING_QUEUE.put(fileSearchResult);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public static void startAHandler(FileSearch fileSearch) {
        FileHandler fileHandler = new FileHandler();
        fileHandler.fileSearch = fileSearch;

        Thread thread = new Thread(fileHandler);
        thread.start();
    }

    public FileSearch getFileSearch() {
        return fileSearch;
    }

    public void setFileSearch(FileSearch fileSearch) {
        this.fileSearch = fileSearch;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }

    public static Collection<IFileHandler> getFileHandlers() {
        return fileHandlers;
    }

    public static void setFileHandlers(Collection<IFileHandler> fileHandlers) {
        FileHandler.fileHandlers = fileHandlers;
    }
}
