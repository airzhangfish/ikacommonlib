package com.ikags.ikalib.util.bxml;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX解析模块
 * 
 * @author zhangxiasheng
 * 
 */
public class BXmlSaxHandler extends DefaultHandler {
	private BXmlElement rootElement = null;
	private BXmlElement thisElement = null;
	Vector<Object> elementStack = null;
	@Override
	public void startDocument() throws SAXException {
		rootElement = new BXmlElement();
		elementStack = new Vector<Object>();
	}

	public BXmlElement getRootElement() {
		return rootElement;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		// System.out.println("startElement="+uri+","+localName+","+qName);

		thisElement = new BXmlElement();
		thisElement.setTagName(qName);
		int attrisize = attributes.getLength();
		if (attrisize > 0) {
			thisElement.setNewAttribute(attrisize);
			for (int i = 0; i < attrisize; i++) {
				thisElement.setAttributeName(i, attributes.getQName(i));
				thisElement.setAttributeValue(i, attributes.getValue(i));
			}
		}
		int size = elementStack.size();
		if (size > 0) {
			BXmlElement lastElement = (BXmlElement) elementStack.elementAt(size - 1);
			lastElement.addChildrenElement(thisElement);
		} else {
			rootElement = thisElement;
		}
		elementStack.add(thisElement);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// System.out.println("endElement="+uri+","+localName+","+qName);

		int size = elementStack.size();
		if (size > 0) {
			elementStack.removeElementAt(size - 1);
		}
		thisElement = null;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String content = new String(ch, start, length);
		String trimcontent = content.trim();
		if (thisElement != null && trimcontent != null && trimcontent.length() > 0) {
			if (thisElement.getChildren().size() == 0) {
				BXmlElement textElement = new BXmlElement();
				textElement.setTagName("");
				textElement.setContents(trimcontent);
				thisElement.addChildrenElement(textElement);
			} else {
				BXmlElement textElement = thisElement.getChildrenElement(0);
				String newstr = textElement.getContents() + content;
				textElement.setContents(newstr);
			}
		}
	}

}
