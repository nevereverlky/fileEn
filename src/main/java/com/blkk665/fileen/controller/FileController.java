package com.blkk665.fileen.controller;

import com.blkk665.fileen.utils.FileCryptoUtil;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @Description
 * @Author blkk665
 * @Date 2022/10/11
 */
@RestController
public class FileController {

    private String encKey = "UM0QMU7mWCDQTxaP";



    // 初始化
    void setUp() {
        // 加密KEY，长度不能小于16位
        encKey = "UM0QMU7mWCDQTxaP";
        System.out.println("encKey=" + encKey);

        // 测试跟目录
//        String testRootPath = FileCryptoUtilTest.class.getResource("/").getFile();
        String testRootPath = "/Users/pluttt/Downloads/jm/";
        System.out.println("testRootPath=" + testRootPath);

//        sourceFile = new File(testRootPath + "1.jpg");
//        if (!sourceFile.exists()) {
//            throw new RuntimeException("fi not exist, path=" + sourceFile.getAbsolutePath());
//        }
//        System.out.println("sourceFile=" + sourceFile.getAbsolutePath());

//        encFile = new File(testRootPath + "1-enc.jpg");
//        System.out.println("encFile=" + encFile.getAbsolutePath());
//        if (encFile.exists()) {
//            System.out.println("delete result=" + encFile.delete());
//        }

//        decFile = new File(testRootPath + "1-dec.jpg");
//        System.out.println("decFile=" + decFile.getAbsolutePath());
//        if (decFile.exists()) {
//            System.out.println("delete result=" + decFile.delete());
//        }
    }


    // 加密
    void encryptFile(String filePath, String fileName) throws Exception {
        File sourceFile;
        File encFile;
        sourceFile = new File(filePath + fileName);
        encFile = new File(filePath + fileName + ".llcc");


        // 加密
        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(encFile, true)) {
            FileCryptoUtil.encryptFile(fis, fos, encKey);
        }

    }

    // 解密
    void decryptFile(String filePath, String fileName) throws Exception {
        File encFile;
        File decFile;

        encFile = new File(filePath + fileName);
        decFile = new File(filePath + (fileName.substring(0, fileName.length() - 5)));


        // 解密
        try (FileInputStream fis = new FileInputStream(encFile);
             FileOutputStream fos = new FileOutputStream(decFile, true)) {
            FileCryptoUtil.decryptedFile(fis, fos, encKey);
        }
    }


    /**
    *
    * 加密
    */
    @GetMapping("/enFile")
    public void enFile(@RequestParam(value = "filePath") String filePath,
                       @RequestParam(value = "fileName") String fileName) throws Exception {

        encryptFile(filePath, fileName);

    }



    /**
    *
    * 解密
    */
    @GetMapping("/deFile")
    public void deFile(@RequestParam(value = "filePath") String filePath,
                       @RequestParam(value = "fileName") String fileName) throws Exception {
        decryptFile(filePath, fileName);

    }



}
