package xyz.shaohui.downloadmanager.Listener;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;

import java.io.File;

import xyz.shaohui.downloadmanager.DownloadManager;
import xyz.shaohui.downloadmanager.R;

/**
 * Created by kpt on 16/5/3.
 */
public class AlertDownloadListener implements DownloadListener {

    private Context context;
    private DownloadManager manager;
    private String url;
    private String uri;
    private boolean canCancel;
    private boolean openFileAuto;
    private boolean showFileSize;
    private AlertDialog.Builder builder;
    private AlertDialog mDialog;

    // view
    private TextView title;
    private NumberProgressBar progressBar;
    private TextView actionButton;
    private TextView fileSizeTextView;

    public AlertDownloadListener(Context context, String title,
                                 boolean canCancel, boolean openFileAuto, boolean showFileSize) {

        this.context = context;
        this.canCancel = canCancel;
        this.openFileAuto = openFileAuto;
        this.showFileSize = showFileSize;

        builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        View contentView = inflater.inflate(R.layout.alert_content_view, null);
        progressBar = (NumberProgressBar) contentView.findViewById(R.id.progress_bar);
        this.title = (TextView) contentView.findViewById(R.id.download_title);
        this.title.setText(title);
        actionButton = (TextView) contentView.findViewById(R.id.download_action_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.cancel(true);
            }
        });

        // show file size
        fileSizeTextView = (TextView) contentView.findViewById(R.id.download_file_size);
        if (showFileSize) {
            fileSizeTextView.setVisibility(View.VISIBLE);
        }

        builder.setView(contentView);
        mDialog = builder.create();
        mDialog.setCancelable(canCancel);
    }

    @Override
    public void start() {
        mDialog.show();
    }

    @Override
    public void progress(long progress, long totalSize, long doneSize) {
        if (progress < 100) {
            progressBar.setProgress((int) progress);
        } else {
            progressBar.setProgress(100);
        }
        if (showFileSize) {
            fileSizeTextView.setText(Formatter.formatFileSize(context, doneSize)
                    + " / " + Formatter.formatFileSize(context, totalSize));
        }
    }

    @Override
    public void result(boolean isSuccess) {
        actionButton.setText("打开");
        if (openFileAuto) {
            openFile();
        }
        mDialog.setCancelable(true);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });
    }

    private void openFile() {
        Intent intent = new Intent();
        if (uri.endsWith("apk")) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + uri), "application/vnd.android.package-archive");
        } else {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(uri).getParentFile()), "file/*");
        }
        if (intent.resolveActivity(context.getPackageManager())!= null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "您貌似没有文件浏览器", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void cancel() {
        mDialog.dismiss();
        Toast.makeText(context, "已取消", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setDownloadManager(DownloadManager manager) {
        this.manager = manager;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setTargetUrl(String uri) {
        this.uri = uri;
    }

    public static class Builder {

        private Context context;
        private String title;
        private boolean openFileAuto;
        private boolean canCancel;
        private boolean showFileSize;

        public Builder(Context context) {
            this.context = context;
            this.title = "版本更新";
            this.openFileAuto = false;
            this.canCancel = false;
            this.showFileSize = false;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder openFileAuto(boolean bool) {
            this.openFileAuto = bool;
            return this;
        }

        public Builder setCanCancel(boolean canCancel) {
            this.canCancel = canCancel;
            return this;
        }

        public Builder showFileSize(boolean showFileSize) {
            this.showFileSize = showFileSize;
            return this;
        }

        public AlertDownloadListener build() {
            return new AlertDownloadListener(context, title, canCancel, openFileAuto, showFileSize);
        }
    }
}
