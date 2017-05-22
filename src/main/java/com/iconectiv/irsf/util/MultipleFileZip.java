package com.iconectiv.irsf.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultipleFileZip {
	private static Logger log = LoggerFactory.getLogger(MultipleFileZip.class);

    public String zipFiles(List<String> files) throws Exception {
        
        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;
        Random rand = new Random(); 
        String outFile =  rand.nextInt((int) new Date().getTime()) + ".zip";
   
        try {
            fos = new FileOutputStream(outFile);
            zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
            for(String filePath:files){
                File input = new File(filePath);
                fis = new FileInputStream(input);
                ZipEntry ze = new ZipEntry(input.getName());
                log.info("Zipping the file: {}", input.getName());
                zipOut.putNextEntry(ze);
                byte[] tmp = new byte[4*1024];
                int size = 0;
                while((size = fis.read(tmp)) != -1){
                    zipOut.write(tmp, 0, size);
                }
                zipOut.flush();
                fis.close();
            }
            zipOut.close();
            log.info("Done... Zipped the files... to {}", outFile);
           
        } catch (FileNotFoundException e) {
             e.printStackTrace();
             throw new Exception(e.getMessage());
        } catch (IOException e) {
        	e.printStackTrace();
        	 throw new Exception(e.getMessage());
        } finally{
            try{
                if(fos != null) fos.close();
            } catch(Exception ex){
                 
            }
        }
		return outFile;
    }
     
    public static void main(String a[]){
         
        MultipleFileZip mfe = new MultipleFileZip();
        List<String> files = new ArrayList<String>();
        files.add("C:/test.txt");
        files.add("C:/test.sh");
        try {
			String zipfile = mfe.zipFiles(files);
			System.out.println(zipfile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
}
