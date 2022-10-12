package com.blkk665.fileen;

import com.blkk665.fileen.utils.DESUtil;
import com.blkk665.fileen.utils.FileCryptoUtil;
import org.apache.commons.io.FileUtils;

import java.io.*;

/**
 * @Description
 * @Author blkk665
 * @Date 2022/10/12
 */
public class main {
    public static void main(String[] args) throws IOException {
        File sourceFile;
        File encFile;
        sourceFile = new File("/Users/pluttt/Downloads/jm/1.mkv");
        encFile = new File("/Users/pluttt/Downloads/jm/1.mkv.llcc");



        // 加密
        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(encFile, true)) {
            DESUtil.encryptFile2("12345678", fis, fos);
        }




    }
}
