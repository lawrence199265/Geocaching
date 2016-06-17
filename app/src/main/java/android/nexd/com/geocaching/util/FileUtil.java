package android.nexd.com.geocaching.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


/**
 * 文件处理工具类
 * Created by xu on 2015/8/14.
 */
public class FileUtil {
    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值
    private static final int BUFF_SIZE = 8192;

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormetFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @param file 目标文件
     * @return 文件的大小
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f 文件
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    /**
     * 剪切文件
     *
     * @param srcFile   源文件
     * @param targetDir 目标地址
     * @throws IOException
     */
    public static void shearFile(File srcFile, String targetDir) throws IOException {
        copyFileToTargetDir(srcFile, targetDir);
        deleteFileOrDir(srcFile);
    }

    public static void shearFile(String srcFile, String targetDir) throws IOException {
        copyFileToTargetDir(buildFile(srcFile, false), targetDir);
        deleteFileOrDir(srcFile);
    }

    public static void unCompressZipFile(String zipPath, String folderPath) throws IOException {
        unCompressZipFile(buildFile(zipPath, false), folderPath);
    }

    /**
     * 解压缩一个文件
     *
     * @param zip        要解压的压缩文件
     * @param folderPath 解压缩的目标目录
     * @throws IOException 当解压缩过程出错时抛出
     */
    public static void unCompressZipFile(File zip, String folderPath) throws IOException {

        try {
            // 先指定压缩档的位置和档名，建立FileInputStream对象
            FileInputStream fins = new FileInputStream(zip);
            // 将fins传入ZipInputStream中
            ZipInputStream zins = new ZipInputStream(fins);
            ZipEntry ze = null;
            byte[] ch = new byte[256];
            while ((ze = zins.getNextEntry()) != null) {
                File zfile = new File(folderPath + ze.getName());
                File fpath = new File(zfile.getParentFile().getPath());
                if (ze.isDirectory()) {
                    if (!zfile.exists())
                        zfile.mkdirs();
                    zins.closeEntry();
                } else {
                    if (!fpath.exists())
                        fpath.mkdirs();
                    FileOutputStream fouts = new FileOutputStream(zfile);
                    int i;
                    while ((i = zins.read(ch)) != -1)
                        fouts.write(ch, 0, i);
                    zins.closeEntry();
                    fouts.close();
                }
            }
            fins.close();
            zins.close();
        } catch (Exception e) {
            System.err.println("Extract error:" + e.getMessage());
        }
    }


    /**
     * 生产文件 如果文件所在路径不存在则生成路径
     *
     * @param fileName    文件名 带路径
     * @param isDirectory 是否为路径
     * @return
     */
    public static File buildFile(String fileName, boolean isDirectory) {
        File target = new File(fileName);
        if (isDirectory) {
            target.mkdirs();
        } else {
            if (!target.getParentFile().exists()) {
                target.getParentFile().mkdirs();
                target = new File(target.getAbsolutePath());
            }
        }
        return target;
    }


    /**
     * copy file to target directory by channel
     *
     * @param srcFile   源文件
     * @param targetDir 目标文件目录
     * @throws IOException
     */
    public static void copyFileToTargetDir(File srcFile, String targetDir) throws IOException {
        if (srcFile.isDirectory()) {
            String parentPathName = srcFile.getName();
            File[] childFiles = srcFile.listFiles();
            for (File childFile : childFiles) {
                copyFileToTargetDir(childFile, targetDir + File.separator + parentPathName);
            }
        } else {
            FileInputStream fis = new FileInputStream(srcFile);
            File targetFile = buildFile(targetDir + "/" + srcFile.getName(), false);
            if (targetFile.exists()) {
                targetFile.delete();
            }
//            new File(targetDir + "/" + srcFile.getName());

//            if (!targetFile.exists()) {
//                buildFile(targetFile.getPath(), false);
//            } else {
//                targetFile.delete();
//            }
//            if (!targetFile.getParentFile().exists()) {
//                targetFile.mkdirs();
//            }
//            if (targetFile.exists()) {
//                targetFile.delete();
//            }
            targetFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(targetFile);
            FileChannel inChannel = fis.getChannel();// 得到对应的文件通道
            FileChannel outChannel = fos.getChannel();// 得到对应的文件通道
            inChannel.transferTo(0, inChannel.size(), outChannel); // 连接两个通道，并且从inChannel通道中读取，写入到outChannel中

            // 完成后关闭流操作
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    public static void copyFileToTargetDir(String srcPath, String targetDir) throws IOException {
        copyFileToTargetDir(new File(srcPath), targetDir);
    }

    /**
     * 压缩文件/文件夹
     *
     * @param resFile     源文件
     * @param zipFilePath 压缩文件存储路径
     * @throws Exception
     */
    public static void compressFile(File resFile, String zipFilePath) throws Exception {
        ZipOutputStream zip = new ZipOutputStream(
                new FileOutputStream(new File(zipFilePath + ".zip")));
        compressFile(resFile, zip, "");
        zip.finish();
        zip.close();
    }

    /* 压缩文件 */
    private static void compressFile(File resFile, ZipOutputStream zipOutputStream, String dir) throws Exception {
        if (resFile.isDirectory()) {
            File[] fileList = resFile.listFiles();

            zipOutputStream.putNextEntry(new ZipEntry(dir + "/"));

            dir = dir.length() == 0 ? "" : dir + "/";
            for (File file : fileList) {
                compressFile(file, zipOutputStream, dir + file.getName());
            }
        } else if (resFile.isFile()) {
            byte buffer[] = new byte[BUFF_SIZE];
            BufferedInputStream in = new BufferedInputStream(
                    new FileInputStream(resFile), BUFF_SIZE);
            zipOutputStream.putNextEntry(new ZipEntry(dir));
            int realLength;
            while ((realLength = in.read(buffer)) != -1) {
                zipOutputStream.write(buffer, 0, realLength);
            }
            in.close();
            zipOutputStream.flush();
            zipOutputStream.closeEntry();
        }
    }

    /**
     * 删除目标文件或文件夹
     *
     * @param file Target file
     * @return is delete success
     */
    public static void deleteFileOrDir(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File childrenFile : files) {
                deleteFileOrDir(childrenFile);
            }
        }
        // 如果是文件夹，删除最外面一层文件夹
        file.delete();
    }

    /**
     * delete file by path
     *
     * @param targetFilePath target file path
     * @return is delete success
     */
    public static void deleteFileOrDir(String targetFilePath) {
        deleteFileOrDir(new File(targetFilePath));
    }

//    /**
//     * 解析XMl文件
//     *
//     * @return
//     * @throws XmlPullParserException
//     * @throws IOException
//     */
//    public static List<MallInfoVo> getMallInfo(Context context, String str_city_name) throws XmlPullParserException, IOException {
//
//        InputStream is = context.getAssets().open(str_city_name + "_mall.xml");
//        XmlPullParser parser = Xml.newPullParser();
//        parser.setInput(is, "UTF_8");
//
//        List<MallInfoVo> list = new ArrayList();
//        int eventType = parser.getEventType();
//
//        while (eventType != parser.END_DOCUMENT) {
//
//            switch (eventType) {
//                case XmlPullParser.START_DOCUMENT:
//                    break;
//                case XmlPullParser.START_TAG:
//                    if (parser.getName().equals("malls")) {
//                        break;
//                    } else if (parser.getName().equals("mall")) {
//                        MallInfoVo mallInfoVo = new MallInfoVo();
//                        for (int i = 0; i < parser.getAttributeCount(); i++) {
//                            if (parser.getAttributeName(i).equals("py")) {
//                                mallInfoVo.seteMallName(parser.getAttributeValue(i));
//                            }
//                            if (parser.getAttributeName(i).equals("name")) {
//                                mallInfoVo.setcMallName(parser.getAttributeValue(i));
//                            }
//                            if (parser.getAttributeName(i).equals("floor")) {
////                                mallInfo.setFloorNum(parser.getAttributeValue(i));
//                            }
//                        }
//                        list.add(mallInfoVo);
//                    }
//                    break;
//                case XmlPullParser.END_TAG:
//                    break;
//
//                default:
//                    break;
//            }
//            eventType = parser.next();
//        }
//        return list;
//    }


    /**
     * 获取缓存文件位置
     *
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable())
            cachePath = context.getExternalCacheDir().getPath();
        else
            cachePath = Environment.getExternalStorageDirectory().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }


    /**
     * read content from file by utf-8
     *
     * @param path the file`s path
     * @return the file`s content
     * @throws IOException
     */
    public static String readFileByLines(String path) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        File file = new File(path);
        if (!file.exists()) {
            return "文件不存在";
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String tempString;
        // 一次读入一行，直到读入null为文件结束
        while ((tempString = reader.readLine()) != null) {
            stringBuffer.append(tempString);
            stringBuffer.append("\n");
        }
        if (reader != null) {
            reader.close();
        }
        return stringBuffer.toString();
    }

    /**
     * write string to file by utf-8
     *
     * @param path target file`s path
     * @param data the content
     * @throws IOException
     */
    public static void writeFile(String path, String data) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferWritter = new BufferedWriter(fileWriter);
        bufferWritter.write(data);
        if (bufferWritter != null) {
            bufferWritter.close();
        }
    }


    public static File writeFile(String path, byte[] data) throws IOException {
        File file = buildFile(path, false);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(data);
        fileOutputStream.close();
        return file;
    }


    public static boolean checkExist(String path) {
        return checkExist(new File(path));
    }

    public static boolean checkExist(File file) {
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
