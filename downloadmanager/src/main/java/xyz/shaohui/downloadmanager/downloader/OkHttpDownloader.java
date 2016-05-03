package xyz.shaohui.downloadmanager.downloader;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by kpt on 16/5/3.
 */
public class OkHttpDownloader implements Downloader{

    public boolean run(String url, String target, final ProgressListener listener) throws IOException {
        Request request = new Request.Builder().url(url).build();

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response response = chain.proceed(chain.request());
                        return response.newBuilder()
                                .body(new ProgressBody(response.body(), listener))
                                .build();
                    }
                }).build();
        Response response = client.newCall(request).execute();
        int len;
        byte[] buf = new byte[2048];
        InputStream inputStream = response.body().byteStream();
        //可以在这里自定义路径
        File appFile = new File(target);
        FileOutputStream fileOutputStream = new FileOutputStream(appFile);

        while ((len = inputStream.read(buf)) != -1) {
            fileOutputStream.write(buf, 0, len);
        }

        fileOutputStream.flush();
        fileOutputStream.close();
        inputStream.close();
        return true;
    }

    @Override
    public boolean begin(String url, String target, Downloader.ProgressListener listener) {
        try {
            return run(url, target, listener);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static class ProgressBody extends ResponseBody{

        private ProgressListener listener;
        private ResponseBody response;
        private BufferedSource bufferedSource;

        public ProgressBody(ResponseBody responseBody, ProgressListener listener) {
            this.response = responseBody;
            this.listener = listener;
        }

        @Override
        public MediaType contentType() {
            return response.contentType();
        }

        @Override
        public long contentLength() {
            return response.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(response.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {

                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    listener.update(totalBytesRead, response.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }


}
