package com.ikags.ikalib.util.loader;

import com.ikags.ikalib.util.bxml.BXmlDriver;
import com.ikags.ikalib.util.bxml.BXmlElement;
/**
 * 网络XML解析器
 * @author zhangxiasheng
 *
 */
public abstract class XMLBaseParser extends TextBaseParser {

	public static final String TAG = "XMLBaseParser";


	@Override
	public void parsetTextData(String url, String text, String tag, boolean isNetdata) {
		BXmlElement root=null;
		try {
		 root = BXmlDriver.loadXML(text.trim());
		} catch (Exception ex) {
			root = null;
			ex.printStackTrace();
		}
		
		//加载解析器
			parseXMLData(url, root, tag,isNetdata);
	}
	

	abstract public void parseXMLData(String url, BXmlElement root, String tag,boolean isNetdata);
}
