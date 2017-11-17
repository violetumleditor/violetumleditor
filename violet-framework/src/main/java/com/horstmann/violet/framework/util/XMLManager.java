package com.horstmann.violet.framework.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class XMLManager <T> {

	private static final String SERIALIZED_FILE_NAME = "statistics.xml";

	public void writeXML(List<T> list) {

		XMLEncoder encoder = null;
		try{
			encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(SERIALIZED_FILE_NAME)));
		}catch(FileNotFoundException fileNotFound){
			System.out.println("Cannot write to " + SERIALIZED_FILE_NAME);
			return;
		}
		encoder.writeObject(list);
		encoder.close();
	}

	@SuppressWarnings("unchecked")
	public List<T> readXML() {

		XMLDecoder decoder = null;
		try {
			decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(SERIALIZED_FILE_NAME)));
		} catch(FileNotFoundException fileNotFound) {
			System.out.println("Cannot read from " + SERIALIZED_FILE_NAME);
			return null;
		}

		Object obj = decoder.readObject();
		List<T> list = null;
		if (obj instanceof List<?>) {
			list = (List<T>) obj;
		}
		decoder.close();
		return list;
	}

}