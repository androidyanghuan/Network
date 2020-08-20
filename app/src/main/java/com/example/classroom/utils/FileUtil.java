package com.example.classroom.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by on 2018/1/8 16:19.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class FileUtil {
    /**
     * Convert byte[] to hex string.将byte转换成int，
     * 然后利用Integer.toHexString(int)来转换成16进制字符串。
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] src) {
        StringBuffer sb = new StringBuffer("");
        if (src == null || src.length <= 0)
            return null;
        int len = src.length;
        for (int i = 0; i < len; i++){
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2)
                sb.append(0);
            sb.append(hv);
        }
        return sb.toString();
    }

    /**
     * 根据文件名称和路径，获取sd卡中的文件，以File形式返回
     */
    public static File getFile(String fileName, String folder)
            throws IOException {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)){
            File path = new File(Environment.getExternalStorageDirectory() + folder);
            if (!path.exists())
                path.mkdirs();
            return new File(path,fileName);
        }
        return null;
    }

    /**
     * 根据文件名称和路径，获取sd卡中的文件，判断文件是否存在，存在返回true
     */
    public static Boolean checkFile(String fileName, String folder)
            throws IOException {
        File targetFile = getFile(fileName,folder);
        return targetFile != null && targetFile.exists();
    }


    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(scheme))
            data = uri.getPath();
        else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(scheme)){
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor){
                if (cursor.moveToFirst()){
                    int columnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (columnIndex > -1)
                        data = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 检查文件是否存在
     */
    public static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath))
            return null;
        File dir = new File(dirPath);
        if (!dir.exists())
            dir.mkdirs();
        return dirPath;
    }

    /** 复制文件 */
    public static void copyFile(File sourcefile, File targetFile) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new BufferedInputStream(new FileInputStream(sourcefile));
            os = new BufferedOutputStream(new FileOutputStream(targetFile));
            byte[] buf = new byte[1024 * 8];
            int len = 0;
            while ((len = is.read(buf)) != -1){
                os.write(buf,0,len);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
                if (os != null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 保存图片到本机
     * @param context            context
     * @param fileName           文件名
     * @param file               file
     * @param saveResultCallback 保存结果callback
     */
    public static void saveImage(final Context context, final String fileName, final File file,
                                 final SaveResultCallback saveResultCallback) {
        new Thread(() -> {
            File appDir = new File(Environment.getExternalStorageDirectory(),"Information");
            FileUtil.checkDirPath(appDir.getPath());
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            String saveFileName = "pic";
            if (fileName.endsWith(".png") || fileName.endsWith(".gif") || fileName.endsWith(".jpg")){
                String substring = fileName.substring(fileName.lastIndexOf("."));
                saveFileName  += System.currentTimeMillis() + substring;
            }else
                saveFileName += System.currentTimeMillis() + ".png";
            File saveFile = new File(appDir, saveFileName);
            InputStream is = null;
            OutputStream os = null;
            try {
                is = new BufferedInputStream(new FileInputStream(file));
                os = new BufferedOutputStream(new FileOutputStream(saveFile));
                byte[] buf = new byte[1024 * 8];
                int len = 0;
                while ((len = is.read(buf)) != -1){
                    os.write(buf,0,len);
                }
                os.flush();
                saveResultCallback.onSavedSuccess();
                //保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(saveFile);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri));
            } catch (Exception e) {
                e.printStackTrace();
                saveResultCallback.onSavedFailed(e.toString());
            } finally {
                try {
                    if (is != null)
                        is.close();
                    if (os != null)
                        os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 保存Bitmap到本机
     * @param context            context
     * @param fileName           bitmap文件名
     * @param bmp                bitmap
     * @param saveResultCallback 保存结果callback
     */
    public static void saveBitmap(final Context context, final String fileName, final Bitmap bmp,
                                  final SaveResultCallback saveResultCallback) {
        new Thread(() -> {
            File appDir = new File(Environment.getExternalStorageDirectory() + "/yizi");
            if (!appDir.exists())
                appDir.mkdirs();
            String saveFileName = "yizi_pic";
            if (fileName.contains(".png") || fileName.contains(".gif") || fileName.contains(".jpg")){
                String substring = fileName.substring(fileName.lastIndexOf(".") + 1);
                saveFileName += System.currentTimeMillis() + substring;
            } else {
                saveFileName += System.currentTimeMillis() + ".png";
            }
            File saveFile = new File(appDir,saveFileName);
            OutputStream os = null;
            try {
                os = new BufferedOutputStream(new FileOutputStream(saveFile));
                bmp.compress(Bitmap.CompressFormat.PNG,100,os);
                os.flush();
                saveResultCallback.onSavedSuccess();
                //保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(saveFile);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri));
            } catch (Exception e) {
                e.printStackTrace();
                saveResultCallback.onSavedFailed(e.toString());
            } finally {
                try {
                    if (os != null)
                        os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public interface SaveResultCallback{
        void onSavedSuccess();
        void onSavedFailed(String error);
    }
}
