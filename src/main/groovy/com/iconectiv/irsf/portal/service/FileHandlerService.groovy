package com.iconectiv.irsf.portal.service

import com.iconectiv.irsf.core.ByteFile
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
/**
 * Created by echang on 1/13/2017.
 */
@Service
class FileHandlerService {
    private static Logger log = LoggerFactory.getLogger(FileHandlerService.class)

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
	
	Boolean saveFile(String fileName, byte[] data) {
		def targetFile = new File(fileName)

		if (targetFile.exists()) {
			log.info("Delete existing file {}", targetFile.getAbsolutePath())
			targetFile.delete()
		}
		targetFile.setBytes(data)
		log.info("Successfully save file {}", targetFile.getAbsolutePath())
		return true
	}
	
	
	void saveZipFile(String zipFileName, List<ByteFile> files) {
		ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(zipFileName))
		files.each {
			zipFile.putNextEntry(new ZipEntry(it.fileName))
			zipFile.write(it.data, 0, it.data.size())
			zipFile.closeEntry()  
		}
		zipFile.close()
	}

	byte[] zipFile(List<ByteFile> files) {
		if (files.isEmpty()) {
			return
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream()
		ZipOutputStream zipFile = new ZipOutputStream(bos)
		files.each {
			zipFile.putNextEntry(new ZipEntry(it.fileName))
			zipFile.write(it.data, 0, it.data.size())
			zipFile.closeEntry()
		}
		zipFile.close()

		return bos.toByteArray()
	}

    List<String> saveTextFile(String dirName, MultipartFile fileStream) {
        def contents = []
        if (fileStream.empty) {
            log.warn("File {} is empty!", fileStream.getOriginalFilename())
            return contents
        }
        checkDirectory(dirName)
        def targetFile = new File(dirName + "/" + fileStream.getOriginalFilename())

        if (targetFile.exists()) {
            log.info("Delete existing file {}", targetFile.getAbsolutePath())
            targetFile.delete()
        }
        targetFile.setBytes(fileStream.getBytes())

        new ByteArrayInputStream(fileStream.getBytes()).eachLine('UTF-8') {contents.add(it)}

        log.info("Successfully save file {}", targetFile.getAbsolutePath())
        return contents
    }

    private void checkDirectory(dirName) {
        def location = new File(dirName)
        if (!location.exists()) {
            location.mkdirs()
        }
    }

	int getFileSize(MultipartFile file) {
		return file.getBytes().size()
	}

	byte[] getContent(MultipartFile file) {
		return file.getBytes()
	}

	byte[] getContent(File file) {
		return file.getBytes()
	}

	byte[] getContent(String fileName) {
		def file = new File(fileName)
		return getContent(file)
	}

	List<String> getContentAsList(MultipartFile file) {
		def contents = []
		new ByteArrayInputStream(file.getBytes()).eachLine('UTF-8') {if (it) contents.add(it)}
		return contents
	}

	List<String> getContentAsList(byte[] file) {
		def contents = []
		file.eachLine('UTF-8') {if (it) contents.add(it)}
		return contents
	}

}
