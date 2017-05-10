package com.iconectiv.irsf.util;

import java.io.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>iconectiv: </p>
 * @author whsiung
 * @version 1.0
 */

public class FileReaderAndWriter {
	String dirName; // the directory name
	String fileName; // the file name
	File file; // File with dir_name and file_name
	FileReader fReader; // the FileReader
	BufferedReader bReader; // the BufferedReader for faster reading
	PrintWriter pWriter; // all the writing stuff
	BufferedWriter bWriter;
	FileWriter fWriter;

	/**
	 * FileReaderAndWriter constructor comment.
	 */
	public FileReaderAndWriter(String dirName, String fileName) {
		super();
		this.dirName = dirName;
		this.fileName = fileName;
	}
    public void deleteFile() {
    	 close();
    	 if (file != null)
    		 file.delete();
    }
	public boolean close() {
		boolean ok = true;
		try {
			if (bReader != null) {
				bReader.close();
			}
			if (fReader != null) {
				fReader.close();
			}
			if (pWriter != null) {
				pWriter.close();
			}
			if (bWriter != null) {
				bWriter.close();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			ok = false;
		}
		return ok;
	}

	public boolean openRead() {

		boolean ok = true;
		try {
			file = new File(dirName, fileName);
			fReader = new FileReader(file);
			bReader = new BufferedReader(fReader);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			ok = false;
		}
		return ok;
	}

	public boolean openWrite() {

		boolean ok = true;
		try {
			file = new File(dirName, fileName);
			fWriter = new FileWriter(file);
			bWriter = new BufferedWriter(fWriter);
			pWriter = new PrintWriter(bWriter, false);
		} catch (IOException e) {
			System.out.println("openWrite()::IOException: " + e.getMessage());
			ok = false;
		}
		return ok;
	}

	public String readAsStr() {

		String line = null;
		try {
			line = bReader.readLine();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return line;
	}

	public boolean writeAsStr(String str2file) {

		boolean ok = true;
		pWriter.write(str2file);
		pWriter.flush();
		return ok;
	}

	// Convenience method
	public boolean writelnAsStr(String str2file) {
		return printlnAsStr(str2file);
	}

	public boolean printlnAsStr(String str2file) {

		boolean ok = true;
		pWriter.println(str2file);
		pWriter.flush();
		return ok;
	}

	public String getDirName() {
		return dirName;
	}

	
	public String getFileName() {
		return fileName;
	}

	public BufferedReader getbReader() {
		return bReader;
	}

	public BufferedWriter getbWriter() {
		return bWriter;
	}


}
