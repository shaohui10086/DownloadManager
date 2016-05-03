package xyz.shaohui.downloadmanager.Listener;

import xyz.shaohui.downloadmanager.DownloadManager;

/**
 * Created by kpt on 16/5/3.
 */
public interface DownloadListener {

    void start();

    void progress(long progress, long totalSize, long doneSize);

    void result(boolean isSuccess);

    void cancel();

    void setDownloadManager(DownloadManager manager);

    void setUrl(String url);

    void setTargetUrl(String uri);
}
