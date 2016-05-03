package xyz.shaohui.downloadmanager.downloader;

/**
 * Created by kpt on 16/5/3.
 */
public interface Downloader {

    boolean begin(String url, String target, ProgressListener listener);

    interface ProgressListener {
        void update(long doneSize, long totalSize, boolean isDone);
    }

}
