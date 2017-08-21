package cn.edu.swpu.cins.service.impl;

import cn.edu.swpu.cins.config.FtpConfig;
import cn.edu.swpu.cins.service.FileService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Transactional
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        logger.info("start upload file");
        logger.info("upload file name: {}", fileName);
        logger.info("upload path:{}", path);
        logger.info("new file name:{}", fileExtensionName);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);
        try {
            file.transferTo(targetFile);
            FtpConfig.uploadFile(Lists.newArrayList(targetFile));
            targetFile.delete();
        } catch (IOException e) {
            logger.error("upload exception", e);
            return null;
        }
        return targetFile.getName();
    }

}
