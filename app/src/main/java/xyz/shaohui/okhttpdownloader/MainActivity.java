package xyz.shaohui.okhttpdownloader;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import xyz.shaohui.downloadmanager.DownloadManager;
import xyz.shaohui.downloadmanager.Listener.AlertDownloadListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://7xn9xs.com2.z0.glb.qiniucdn.com/kpt-2.1.2-160413-web.apk";
                String target = Environment.getExternalStorageDirectory() + "/fan/kpt.apk";
                text(url, target);
            }
        });
    }

    private void text(String url, String target) {
        DownloadManager manager = new DownloadManager();
        AlertDownloadListener listener = new AlertDownloadListener.Builder(this)
                .showFileSize(true)
                .build();
        manager.download(url, target, listener);
    }
}
