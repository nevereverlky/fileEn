package com.blkk665.fileen;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * 大文件分块写入本地并合入
 */
public class FilePart {
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        // 调用文件分片方法
        testFilePart();
        // 调用文件合分片方法
//        testFileMerge();
        // 调用文件快速合分片方法
//        fastFileMerge();
        long end = System.currentTimeMillis();
        System.out.println("耗时=" + (end - start) + "ms");
    }

    /**
     * 大文件分片写入本地
     *
     * @throws IOException
     */
    private static void testFilePart() throws IOException {
        // mac
//        File sourceFile = new File("/Users/pluttt/Downloads/jm/1.mkv");
//        String chunkPath = "/Users/pluttt/Downloads/jm/cut/";

        //win
        File sourceFile = new File("D:\\jm\\1.mkv");
        String chunkPath = "D:\\jm\\cut\\";
        File chunkFolder = new File(chunkPath);
        // 如果该文件夹
        if (!chunkFolder.exists()) {
            chunkFolder.mkdirs();
        }
        // 设定文件分块大小,设定100M
        long chunkSize = 1024 * 1024 * 100;
        // 分块数量
        long chunkNum = (long) Math.ceil(sourceFile.length() * 1.0 / chunkSize);
        if(chunkNum<=0){
            chunkNum=1;
        }
        // 设置缓冲区
        byte[] bytes = new byte[1024];  // 和下面方式创建byte[]效率基本一样
//        byte[] bytes = new byte[(int) sourceFile.length()];
        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(sourceFile));
        // 开始分块
        for (int i = 1; i <= chunkNum; i++) {
            // 创建分块文件
            File file = new File(chunkPath + i);
            if(file.createNewFile()){
                // 向分块文件中写数据
                BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(file));
                int len = -1;
                while ((len = fis.read(bytes)) != -1) {
                    fos.write(bytes, 0, len); // 写入数据
                    if(file.length()>chunkSize){
                        break;
                    }
                }
                fos.close();
            }
        }
        fis.close();
    }

    /**
     * 把本地的文件片合并到一起
     *
     * @throws IOException
     */
    private static void testFileMerge() throws IOException {
        // 需要合并的文件所在的文件夹
        File chunkFolder = new File("/Users/pluttt/Downloads/jm/cut/");
        // 合并后的文件
        File mergeFile = new File("/Users/pluttt/Downloads/jm/cut/hexing");
        // 如果该文件夹
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
        // 创建合并后的文件
        mergeFile.createNewFile();
        BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(mergeFile));
        // 设置缓冲区
        byte[] bytes = new byte[1024];  // 和下面方式创建byte[]效率基本一样
//        byte[] bytes = new byte[(int) sourceFile.length()];
        // 获取分块列表
        File[] fileArray = chunkFolder.listFiles();
        // 把文件转成集合并排序
        ArrayList<File> fileList = new ArrayList<>(Arrays.asList(fileArray));
        // 从小到大进行排序
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if(Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())){
                    return -1;
                }
                return 1;
            }
        });
        // 合并文件
        for (File chunkFile : fileList) {
            BufferedInputStream fis = new BufferedInputStream(new FileInputStream(chunkFile));
            int len = -1;
            while ((len = fis.read(bytes)) != -1) {
                fos.write(bytes, 0, len); // 写入数据
            }
            fis.close();
        }
        fos.close();
    }

    /**
     * 快速把本地的文件片合并到一起FileChannel
     *
     * @throws IOException
     */
    private static void fastFileMerge() throws IOException {
        // 需要合并的文件所在的文件夹
        File chunkFolder = new File("/Users/pluttt/Downloads/jm/cut/");
        // 合并后的文件
        File mergeFile = new File("/Users/pluttt/Downloads/jm/he/1.mkv");
        // 如果该文件夹
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
        // 创建合并后的文件
        mergeFile.createNewFile();
        // 获取分块列表
        File[] fileArray = chunkFolder.listFiles();
        // 把文件转成集合并排序
        ArrayList<File> fileList = new ArrayList<>(Arrays.asList(fileArray));
        // 从小到大进行排序
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if(Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())){
                    return -1;
                }
                return 1;
            }
        });
        // 合并文件
        try(FileChannel fosChannel = new FileOutputStream(mergeFile).getChannel()) {
            for (File chunkFile : fileList) {
                try(FileChannel fisChannel =  new FileInputStream(chunkFile).getChannel()) {
                    fisChannel.transferTo(0, fisChannel.size(), fosChannel);
                }catch (IOException ex){
                    System.out.println("IOException" + ex);
                }
            }

        }catch (IOException ex){
            System.out.println("IOException" + ex);
        }
    }
}
