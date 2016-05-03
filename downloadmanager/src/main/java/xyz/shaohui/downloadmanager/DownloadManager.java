package xyz.shaohui.downloadmanager;

import android.content.Context;
import android.os.AsyncTask;

import xyz.shaohui.downloadmanager.Listener.DownloadListener;
import xyz.shaohui.downloadmanager.downloader.Downloader;
import xyz.shaohui.downloadmanager.downloader.OkHttpDownloader;

/**
 * Created by kpt on 16/5/3.
 */
public class DownloadManager extends AsyncTask<String, Long, Boolean> {

    private Downloader downloader;
    private DownloadListener listener;
    private String url;
    private String targetUri;

    public DownloadManager() {
        this.downloader = new OkHttpDownloader();
    }

    public DownloadManager(Context context, DownloadListener listener, String url,
                           String targetUri, Downloader downloader) {
        this.listener = listener;
        this.url = url;
        this.targetUri = targetUri;
        this.downloader = downloader;
    }

    public void download(String url, String targetUri, DownloadListener listener) {
        listener.setDownloadManager(this);
        listener.setUrl(url);
        listener.setTargetUrl(targetUri);

        this.url = url;
        this.targetUri = targetUri;
        this.listener = listener;
        execute(new String[]{});
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return downloader.begin(url, targetUri, new Downloader.ProgressListener() {
            @Override
            public void update(long doneSize, long totalSize, boolean isDone) {
                publishProgress(new Long[]{(long)(doneSize* 1.f / totalSize * 100), totalSize, doneSize});
            }
        });
    }

    @Override
    protected void onPreExecute() {
        listener.start();
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        listener.progress(values[0], values[1], values[2]);
    }

    @Override
    protected void onPostExecute(Boolean isSuccess) {
        listener.result(isSuccess);
    }

    @Override
    protected void onCancelled() {
        listener.cancel();
    }

    public static class Builder {

        private Context context;
        private Downloader downloader;
        private DownloadListener listener;
        private String url;
        private String uri;

        public Builder(Context context) {
            this.context = context;
            this.downloader = new OkHttpDownloader();
            //TODO init
        }

        public Builder addListener(DownloadListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setTargetUri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder setDownloader(Downloader downloader) {
            this.downloader = downloader;
            return this;
        }

        public DownloadManager build() {
            DownloadManager manager = new DownloadManager(context, listener, url, uri, downloader);
            return manager;
        }
    }

}
