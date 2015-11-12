package com.lovemoin.card.app.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.lovemoin.card.app.R;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.utils.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class FileDownloader {
    private Context context;
    private ProgressDialog pd;
    private long downloadedSize;
    private long maxSize;
    private FileUtil fileUtil;
    private String apkName;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 0:
                        Toast.makeText(context, "无法获取文件大小", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                }
            }
            super.handleMessage(msg);
        }
    };

    public FileDownloader(Context context) {
        this.context = context;
        fileUtil = FileUtil.getInstance();
    }

    public void download(String sourceName, final String path, String targetName) {
        pd = new ProgressDialog(context);
        pd.setMessage(context.getString(R.string.hint_download_upgrade_file));
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setProgressNumberFormat("%1d kb/%2d kb");
        pd.show();
        final AsyncTask task = new AsyncTask<String, Long, File>() {
            @Override
            protected File doInBackground(String... params) {
                String sourceUrl = params[0];
                String path = params[1];
                String targetName = params[2];
                if (fileUtil.isFileExist(targetName)) {
                    return null;
                }
                InputStream input = getInputStreamFromURL(sourceUrl);
                OutputStream output = null;
                publishProgress(0l);
                File resultFile = null;
                fileUtil.deleteHisFiles(path);
                fileUtil.createSDDir(path);
                try {
                    resultFile = fileUtil.createSDFile(path + "/" + targetName);
                    output = new FileOutputStream(resultFile);
                    byte[] buf = new byte[fileUtil.FILESIZE];
                    int length;
                    while ((length = input.read(buf)) != -1) {
                        output.write(buf, 0, length);
                        downloadedSize += length;
                        publishProgress(downloadedSize);
                    }
                    output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (input != null)
                            input.close();
                        if (output != null)
                            output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return resultFile;
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                pd.dismiss();
                Toast.makeText(context, R.string.download_finished, Toast.LENGTH_SHORT).show();
                openFile(file);
            }

            @Override
            protected void onProgressUpdate(Long... values) {
                if (values[0] == 0l) {
                    pd.setMax((int) (maxSize / 1024));
                }
                pd.setProgress((int) (values[0] / 1024));
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                Toast.makeText(context, R.string.download_canceled, Toast.LENGTH_SHORT).show();
                fileUtil.deleteHisFiles(path);
            }
        }.execute(Config.SERVER_URL + sourceName, path, targetName);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                task.cancel(true);
                Toast.makeText(context, R.string.download_canceled, Toast.LENGTH_SHORT).show();
                fileUtil.deleteHisFiles(path);
            }
        });
    }

    public InputStream getInputStreamFromURL(String urlStr) {
        HttpURLConnection urlConn;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlStr);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.connect();
            inputStream = urlConn.getInputStream();
            this.maxSize = urlConn.getContentLength();
            if (this.maxSize <= 0) {
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
                //Toast.makeText(context, "无法获取文件大小", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputStream;
    }

    private void openFile(File file) {
        if (file == null)
            return;
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
