package com.xiongyingqi.util.file.search;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by 瑛琪<a href="http://xiongyingqi.com">xiongyingqi.com</a> on 2014/6/4 0004.
 */
public class FileSearch implements FileSearchObserver {
    private File baseFolder;
    private Collection<String> keywords;
    private boolean findFolder;
    private boolean searchContent;
    private Collection<FileSearchObserver> observers;
    public static final BlockingQueue<File> BLOCKING_QUEUE = new LinkedBlockingQueue<File>();
    public static final BlockingQueue<FileSearchResult> MESSAGE_BLOCKING_QUEUE = new LinkedBlockingQueue<FileSearchResult>();


    /**
     * 通知监听者
     *
     * @param result
     */
    private void notifyObservers(FileSearchResult result) {
        if (observers != null) {
            for (FileSearchObserver observer : observers) {
                if (observer == null) {
                    continue;
                }
                try {
                    observer.findFile(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void startNotifyObservers() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileSearchResult result = MESSAGE_BLOCKING_QUEUE.take();
                    while (result != null) {
                        notifyObservers(result);
                        result = MESSAGE_BLOCKING_QUEUE.take();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * 添加监听者
     *
     * @param observer
     */
    private void addObserver(FileSearchObserver observer) {
        if (observer == null) {
            System.out.println("监听者为空！");
            return;
        }
        if (observers == null) {
            observers = new ArrayList<FileSearchObserver>();
        }
        observers.add(observer);
    }

    public FileSearch() {
    }

    public FileSearch(File baseFolder) {
        if (!baseFolder.isDirectory()) {
            System.out.println("文件：" + baseFolder + "不是文件夹");
        }
        this.baseFolder = baseFolder;
    }

    public void addKeyword(String keyword) {
        if (keywords == null) {
            keywords = new ArrayList<String>();
        }
        keywords.add(keyword);
    }

    public void startSearch() {
        for (int i = 0; i < 5; i++) {
            FileHandler.startAHandler(this);
        }
        startNotifyObservers();
        searchFiles(baseFolder);
//        do {
//            for (File file : files) {
//                try {
//                    BLOCKING_QUEUE.put(file);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                files = searchFiles(file);
//            }
//        }
//        while (files != null);
    }

    public File[] searchFiles(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files == null) {
                return null;
            }
            for (File file : files) {
                searchFiles(file);
            }
            return files;
        } else {
            try {
                BLOCKING_QUEUE.put(folder);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public File getBaseFolder() {
        return baseFolder;
    }

    public void setBaseFolder(File baseFolder) {
        this.baseFolder = baseFolder;
    }

    public Collection<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(Collection<String> keywords) {
        this.keywords = keywords;
    }

    public boolean isFindFolder() {
        return findFolder;
    }

    public void setFindFolder(boolean findFolder) {
        this.findFolder = findFolder;
    }

    public boolean isSearchContent() {
        return searchContent;
    }

    public void setSearchContent(boolean searchContent) {
        this.searchContent = searchContent;
    }

    public static void main(String[] args) {
//        int i = 0;
//        do {
//            System.out.println(i++);
//        } while (i < 10);
//        i = 0;
//        while (i < 10){
//            System.out.println(i++);
//        }
        File baseFolder = new File("C:\\Users\\瑛琪\\Desktop");
//        File[] files = File.listRoots();
//        for (File file : files) {
            System.out.println(baseFolder);
            FileSearch fileSearch = new FileSearch(baseFolder);
            fileSearch.addObserver(fileSearch);
            fileSearch.addKeyword("你妹");
            fileSearch.startSearch();
//        }
    }

    @Override
    public void findFile(FileSearchResult result) {
        System.out.println("findFile ========== " + result.getFile());
        System.out.println("marks ========== " + result.getFileSearchResultMarks());
    }
}
