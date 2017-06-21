package com.ikags.ikalib.util.bxml;

import java.util.Vector;
/**
 * xml的node节点,转化成的数据结构
 * 
 * @author zhangxiasheng
 * 
 */
public class BXmlElement {

	public BXmlElement() {
		children = new Vector<BXmlElement>();
	}

	public BXmlElement(String TagName) {
		if (TagName != null) {
			tagName = TagName;
		}
		children = new Vector<BXmlElement>();
	}

	private Vector<BXmlElement> children;
	private String[] attributesName;
	private String[] attributesValue;
	private String tagName;
	private String contents;
	public int id;
	/**
	 * 此节点的父节点,如果是root节点则为null
	 */
	public BXmlElement parent = null;
	public int parentID = 0;
	/**
	 * 此节点所在的父节点的上一个节点,如果是第一个节点则为null
	 */
	public BXmlElement previous = null;
	public int previousID = 0;
	/**
	 * 此节点所在的父节点的下一个节点,如果是最后一个节点则为null
	 */
	public BXmlElement next = null;
	public int nextID = 0;

	/**
	 * 初始化attributes的大小(生成此节点时调用,一般不需要使用此方法)
	 * 
	 * @param size
	 */
	public void setNewAttribute(int size) {
		attributesName = new String[size];
		attributesValue = new String[size];
	}

	/**
	 * 设置attributesName[index]的Name(生成此节点时调用,一般不需要使用此方法)
	 * 
	 * @param index
	 * @param Name
	 */
	public void setAttributeName(int index, String Name) {
		if (index < 0 || index > attributesName.length - 1) {
			return;
		}
		attributesName[index] = Name;
	}

	/**
	 * 设置attributesValue[index]的name(生成此节点时调用,一般不需要使用此方法)
	 * 
	 * @param index
	 * @param Value
	 */
	public void setAttributeValue(int index, String Value) {
		if (index < 0 || index >= attributesValue.length) {
			return;
		}
		attributesValue[index] = Value;
	}

	/**
	 * 获取attributes指定index的name
	 * 
	 * @param index
	 * @return String
	 */
	public String getAttributeName(int index) {
		if (attributesName == null) {
			return null;
		}
		if (index < 0 || index >= attributesName.length) {
			return null;
		}
		return attributesName[index];
	}

	/**
	 * 获取attributes指定index的Value
	 * 
	 * @param index
	 * @return String
	 */
	public String getAttributeValue(int index) {
		if (attributesValue == null) {
			return null;
		}
		if (index < 0 || index >= attributesValue.length) {
			return null;
		}
		return attributesValue[index];
	}

	/**
	 * 获取attributes的数目
	 * 
	 * @return int
	 */
	public int getAttributeCounts() {
		if (attributesValue == null) {
			return 0;
		}
		return attributesValue.length;
	}

	/**
	 * 获取attributes指定name的Value
	 * 
	 * @param abName
	 * @return String
	 */
	public String getAttributeValue(String abName) {
		if (abName == null) {
			return null;
		}
		if (attributesName == null) {
			return null;
		}
		if (attributesValue == null) {
			return null;
		}
		for (int i = 0; i < attributesName.length; i++) {
			if (attributesName[i].equalsIgnoreCase(abName)) {
				return attributesValue[i];
			}
		}
		return null;
	}

	public boolean removeAttributeValue(String abName) {
		if (abName == null) {
			return false;
		}
		if (attributesName == null) {
			return false;
		}
		if (attributesValue == null) {
			return false;
		}
		for (int i = 0; i < attributesName.length; i++) {
			if (attributesName[i].equalsIgnoreCase(abName)) {
				if (attributesName.length == 1) {
					attributesName = null;
					attributesValue = null;
					return true;
				}
				int size = attributesName.length - 1;
				String[] tmpName = new String[size];
				String[] tmpValue = new String[size];
				if (size == 0) {
					attributesName = null;
					attributesValue = null;
				} else {
					int count = 0;
					for (int j = 0; j < tmpName.length; j++) {
						if (j == i) {
							count++;
						}
						tmpName[j] = attributesName[count];
						tmpValue[j] = attributesValue[count];
						count++;
					}
					attributesName = tmpName;
					attributesValue = tmpValue;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取此节点的Vector
	 * 
	 * @return Vector
	 */
	public Vector<BXmlElement> getChildren() {
		return this.children;
	}

	/**
	 * 获取此节点下指定的子节点
	 * 
	 * @param index
	 * @return BXmlElement
	 */
	public BXmlElement getChildrenElement(int index) {
		if (index < 0 || index >= children.size()) {
			return null;
		}
		return (BXmlElement) children.elementAt(index);
	}

	/**
	 * 在此节点下增加一个子节点
	 * 
	 * @param el
	 * @return boolean
	 */
	public boolean addChildrenElement(BXmlElement el) {
		children.addElement(el);
		return true;
	}

	/**
	 * 在此节点下增加一个子节点
	 * 
	 * @param el
	 * @return boolean
	 */
	public boolean addChildrenElement(BXmlElement el, int index) {
		children.add(index, el);
		return true;
	}

	/**
	 * 删除此节点下的指定子节点
	 * 
	 * @param index
	 * @return boolean
	 */
	public boolean removeChildrenElement(int index) {
		if (index < 0 || index >= children.size()) {
			return false;
		}
		children.removeElementAt(index);
		return true;
	}

	/**
	 * 删除此节点下的所有子节点
	 * 
	 * @return boolean
	 */
	public boolean removeAllChildrenElements() {
		children.removeAllElements();
		return true;
	}

	/**
	 * 获取Contents
	 * 
	 * @return String
	 */
	public String getContents() {
		return contents;
	}

	/**
	 * 对Contents赋值
	 * 
	 * @param contents
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}

	/**
	 * 获取TagName
	 * 
	 * @return String
	 */
	public String getTagName() {
		return tagName;
	}

	/**
	 * 对TagName赋值
	 * 
	 * @param tagName
	 */
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public void addAttri(Vector<String> name, Vector<String> value) {
		if (attributesName == null) {
			this.setNewAttribute(name.size());
			for (int i = 0; i < name.size(); i++) {
				this.setAttributeName(i, name.elementAt(i));
				this.setAttributeValue(i, value.elementAt(i));
			}
		} else {
			// 已有属性,添加新属性.先把原来的复制到Vector里面,然后重新生成
			for (int i = 0; i < attributesName.length; i++) {
				name.add(attributesName[i]);
				value.add(attributesValue[i]);
			}
			this.setNewAttribute(name.size());
			for (int i = 0; i < name.size(); i++) {
				this.setAttributeName(i, name.elementAt(i));
				this.setAttributeValue(i, value.elementAt(i));
			}
		}
	}
	/**
	 * 打印以此element为root的整颗树
	 */
	public void printNode(int indentNum) {
		for (int i = 0; i < indentNum; i++) {
			System.out.print("    ");
		}
		if ("".equals(tagName) && contents != null) {
			System.out.println(contents);
		} else {
			System.out.print("└──<" + tagName + "> ");
			if (attributesName != null) {
				for (int i = 0; i < attributesName.length; i++) {
					System.out.print(attributesName[i] + "=\"" + attributesValue[i] + "\", ");
				}
			}
			System.out.println("");
			if (children.size() > 0) {
				for (int i = 0; i < children.size(); i++) {
					BXmlElement element = (BXmlElement) children.elementAt(i);
					element.printNode(indentNum + 1);
				}
			}
		}
	}

	/**
	 * 序列化整颗树
	 */
	public String elementToString() {
		StringBuffer sbu = new StringBuffer();
		if ("".equals(tagName)) {
			if (contents != null) {
				sbu.append(contents);
			}
		} else {
			sbu.append("<" + tagName + " ");
			if (attributesName != null) {
				for (int i = 0; i < attributesName.length; i++) {
					sbu.append(attributesName[i] + "=\"" + attributesValue[i] + "\" ");
				}
			}
			if (children.size() == 0) {
				sbu.append("/>");
			} else {
				sbu.append(">");
				for (int i = 0; i < children.size(); i++) {
					BXmlElement elt = (BXmlElement) children.elementAt(i);
					sbu.append(elt.elementToString());
				}
				sbu.append("</" + tagName + ">");
			}
		}
		return sbu.toString();
	}

	/**
	 * 序列化 BXmlElement
	 * 
	 * @return byte[]
	 */
	public byte[] serialize() {
		String str = new String(this.elementToString());
		return str.getBytes();
	}

	/**
	 * 反序列化
	 * 
	 * @param byteArray
	 * @return BXmlElement
	 */
	public static BXmlElement deserialize(byte[] byteArray) {
		BXmlElement bxmle = new BXmlElement();
		String str = new String(byteArray);
		bxmle = BXmlDriver.loadXML(str);
		return bxmle;
	}

	/**
	 * 判断以自己为root的树,是否有此节点
	 * 
	 * @param Element
	 * @return boolean
	 */
	public boolean isMyChildren(BXmlElement Element) {
		int size = this.children.size();
		for (int i = 0; i < size; i++) {
			BXmlElement ele = (BXmlElement) this.children.elementAt(i);
			if (ele.equals(Element)) {
				return true;
			}
			int childrensize = ele.children.size();
			if (childrensize > 0) {
				boolean istrue = ele.isMyChildren(Element);
				if (istrue == true) {
					return true;
				} else {
					// 继续运行
				}
			}
		}
		return false;
	}

	public int isMyTag(String[] Tags) {
		int size = this.children.size();
		for (int i = 0; i < size; i++) {
			BXmlElement ele = (BXmlElement) this.children.elementAt(i);
			for (int j = 0; j < Tags.length; j++) {
				if (ele.getTagName().equalsIgnoreCase(Tags[j])) {
					return j;
				}
			}
			int childrensize = ele.children.size();
			if (childrensize > 0) {
				int isTag = ele.isMyTag(Tags);
				if (isTag != -1) {
					return isTag;
				} else {
					// 继续运行
				}
			}
		}
		return -1;
	}

	/**
	 * 找到本节点下的包含有此TAG的bxml节点
	 * @param Tag
	 * @return
	 */
	public BXmlElement getElement(String Tag) {
		if (this.getTagName().equalsIgnoreCase(Tag)) {
			return this;
		}
		int size = this.children.size();
		for (int i = 0; i < size; i++) {
			BXmlElement ele = (BXmlElement) this.children.elementAt(i);
			if (ele.getTagName().equalsIgnoreCase(Tag)) {
				return ele;
			}
			int childrensize = ele.children.size();
			if (childrensize > 0) {
				BXmlElement isTag = ele.getElement(Tag);
				if (isTag != null) {
					return isTag;
				} else {
					// 继续运行
				}
			}
		}
		return null;
	}

	/**
	 * 获得本节点下的子节点contents
	 * @return
	 */
	public String getChildContents() {
		try {
			BXmlElement bxml = this.getChildrenElement(0);
			String contents = null;
			if (bxml != null) {
				contents = bxml.getContents();
			}
			return contents;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得本节点下第一个contents
	 * @param Tag
	 * @return
	 */
	public String getElementChildContents(String Tag) {
		BXmlElement bxml = getElement(Tag);
		if (bxml != null) {
			String data = bxml.getChildContents();
			return data;
		}
		return null;
	}
	
	/**
	 * 获得本节点下第一个contents(必须为非空)
	 * @param Tag
	 * @return
	 */
	public String getElementChildContentsClearNull(String Tag) {
		BXmlElement bxml = getElement(Tag);
		if (bxml != null) {
			String data = bxml.getChildContents();
			String newdata=BxmlUtil.getStringClearNull(data);
			return newdata;
		}
		return "";
	}
	
	
	/**
	 * 获得本节点下第一个contents(int 形)
	 * @param Tag
	 * @return
	 */
	public int getElementChildContentsInt(String Tag) {
		BXmlElement bxml = getElement(Tag);
		if (bxml != null) {
			String data = bxml.getChildContents();
			int newdata=BxmlUtil.getInt(data);
			return newdata;
		}
		return 0;
	}

}
