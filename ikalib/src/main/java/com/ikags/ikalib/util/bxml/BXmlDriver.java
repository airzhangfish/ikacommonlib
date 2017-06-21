package com.ikags.ikalib.util.bxml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
/**
 * XML处理方法封装
 * 
 * @author zhangxiasheng
 * 
 */
public class BXmlDriver {

	/**
	 * 读取文本方式的xml
	 * 
	 * @param text
	 * @return
	 */
	public static BXmlElement loadXML(String text) {
		InputStream inputstream = null;
		try {
			inputstream = new ByteArrayInputStream(text.getBytes("UTF-8"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		BXmlElement rootelement = loadXML(inputstream);
		return rootelement;
	}

	/**
	 * 读取数据流方式的xml
	 */
	public static BXmlElement loadXML(InputStream input) {
		BXmlSaxHandler saxhandler = new BXmlSaxHandler();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(input, saxhandler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		BXmlElement rootelement = saxhandler.getRootElement();
		resetElement(rootelement);
		return rootelement;
	}

	/**
	 * 处理next()等方法
	 * 
	 * @param root
	 */
	private static void resetElement(BXmlElement root) {
		int size = root.getChildren().size();
		for (int i = 0; i < size; i++) {
			BXmlElement element = (BXmlElement) root.getChildren().elementAt(i);
			element.parent = root;
			if (i < size - 1) {
				element.next = (BXmlElement) root.getChildren().elementAt(i + 1);
			}
			if (i > 0) {
				element.previous = (BXmlElement) root.getChildren().elementAt(i - 1);
			}
			resetElement(element);
		}
	}
}
