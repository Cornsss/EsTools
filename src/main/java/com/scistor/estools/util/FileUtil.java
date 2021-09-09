package com.scistor.estools.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * @author wuzh
 * @version V1.0
 * @Package com.kdom.irs.business.file.util
 * @Description: 文件操作工具类
 * @date 2019-11-30
 */
public class FileUtil {

    public final static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 递归删除目录及子目录
     * 
     * @param path 目录路径
     * @return
     */
    public static boolean deleteDir(String path) {
        try {
            File file = new File(path);
            if (file.isFile()) {
                file.delete();
            } else {
                File[] files = file.listFiles();
                if (files == null) {
                    file.delete();
                } else {
                    for (File subFile : files) {
                        String absolutePath = subFile.getAbsolutePath();
                        deleteDir(absolutePath);
                    }
                    file.delete();
                }
            }
            return true;
        } catch (Exception e) {
            logger.error("删除目录失败：" + path, e);
            return false;
        }
    }

    /**
     * 读取文件内容
     *
     * @param file 文件
     * @return
     */
    public static String readFile(MultipartFile file) {
        BufferedReader reader = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (Exception e) {
            logger.error("读取文件失败：" + file, e);
        }
        return stringBuffer.toString();
    }
}
