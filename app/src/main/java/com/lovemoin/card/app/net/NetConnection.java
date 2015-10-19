package com.lovemoin.card.app.net;

import android.os.AsyncTask;
import android.util.Log;

import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.entity.Response;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by zzt on 15-8-24.
 * 网络通讯基类
 *
 * @author zzt
 */
public abstract class NetConnection {
    /**
     * 网络通讯
     *
     * @param url      通讯地址
     * @param paramMap 参数对
     */
    public NetConnection(final String url, final Map<String, String> paramMap) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                Response response = new Response();
                try {
                    // 配置POST参数
                    StringBuffer paramStr = new StringBuffer();
                    for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                        paramStr.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                    }
                    paramStr.deleteCharAt(paramStr.length() - 1);

                    Log.d("NetConnection", url + "?" + paramStr.toString());

                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setConnectTimeout(4000);
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), Config.CHARSET));
                    writer.write(paramStr.toString());
                    writer.flush();
                    writer.close();

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        // 获取返回值
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Config.CHARSET));
                        String line;
                        StringBuffer resultStr = new StringBuffer();
                        while ((line = reader.readLine()) != null) {
                            resultStr.append(line);
                        }
                        System.out.println(resultStr.toString());
                        // 封装返回数据
                        response = new Response(resultStr.toString());
                    } else {
                        response.setMessage("error code: " + connection.getResponseCode());
                        response.setSuccess(false);
                        response.setParam("");
                    }

                } catch (IOException e) {
                    String message = e.getMessage();
                    if (message == null) {
                        message = "无法连接应用服务器。";
                    }
                    response.setMessage(message);
                    response.setSuccess(false);
                    response.setParam("");
                    e.printStackTrace();
                }
                return response.toString();
            }

            @Override
            protected void onPostExecute(String s) {
                Response response = new Response(s);
                if (response.isSuccess()) {
                    onSuccess(response.getParam());
                } else {
                    onFail(response.getMessage());
                }
            }
        }.execute();
    }

    public abstract void onSuccess(String result);

    public abstract void onFail(String message);

}
