package com.iconectiv.irsf.portal.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
/**
 * Created by echang on 1/13/2017.
 */
@Service
class FileHandleerService {
    private static Logger log = LoggerFactory.getLogger(FileHandleerService.class)

    Boolean saveFile(String dirName, MultipartFile fileStream, Boolean override = true) {
        if (fileStream.empty) {
            log.warn("File {} is empty!", fileStream.getOriginalFilename())
            return false
        }
        checkDirectory(dirName)
        def targetFile = new File(dirName + "/" + fileStream.getOriginalFilename())

        if (targetFile.exists() && override) {
            log.info("Delete existing file {}", targetFile.getAbsolutePath())
            targetFile.delete()
        }
        targetFile.setBytes(fileStream.getBytes())
        log.info("Successfully save file {}", targetFile.getAbsolutePath())
        return true
    }

    private void checkDirectory(dirName) {
        def location = new File(dirName)
        if (!location.exists()) {
            location.mkdirs()
        }
    }
}
