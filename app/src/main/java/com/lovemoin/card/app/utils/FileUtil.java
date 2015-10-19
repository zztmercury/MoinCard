package com.lovemoin.card.app.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
    private static FileUtil fileUtil;
    public String SDPATH;
    public int FILESIZE = 1 * 1024;

    private FileUtil() {
        // 得到当前外部存储设备的目/SDCARD )
        SDPATH = Environment.getExternalStorageDirectory() + "/";
    }

    public static FileUtil getInstance() {
        if (fileUtil == null)
            fileUtil = new FileUtil();
        return fileUtil;
    }

    public String getSDPATH() {
        return SDPATH;
    }

    /**
     * 在SD卡上创建文件
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public File createSDFile(String fileName) throws IOException {
        File file = new File(SDPATH + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName
     * @return
     */
    public File createSDDir(String dirName) {
        File dir = new File(SDPATH + dirName);
        dir.mkdir();
        return dir;
    }

    /**
     * 判断SD卡上的文件夹是否存在
     *
     * @param fileName
     * @return
     */
    public boolean isFileExist(String fileName) {
        File file = new File(SDPATH + fileName);
        return file.exists();
    }

    public void deleteHisFiles(String dirName) {
        File dir = new File(SDPATH + dirName);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.getName().startsWith("FindPro")) {
                    file.delete();
                }
            }
        }
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     *
     * @param path
     * @param fileName
     * @param input
     * @return
     */
    public File write2SDFromInput(String path, String fileName,
                                  InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            createSDDir(path);
            file = createSDFile(path + "/" + fileName);
            output = new FileOutputStream(file);
            byte[] buffer = new byte[FILESIZE];
            int leng;
            while ((leng = input.read(buffer)) != -1) {
                output.write(buffer, 0, leng);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

}