package com.ikags.ikalib.gameutil.anime.info;
/**
 * 物块信息
 * @author zhangxiasheng
 *
 */
public class ModInfo{
	public float mX=0;
	public float mY=0;
	public int mRotateType=0; // J2ME的8个设置
	public float mRotate=0; // android/ios/pc设置
	public float mScale=1; // android/ios/pc设置
	public String mInfo=null;//说明
	public int mImageModID=0;//用了哪张小图



public String getName(){
	StringBuffer sb=new StringBuffer();
	sb.append("mod=");
	sb.append(mX);
	sb.append("x");
	sb.append(mY);
	sb.append("(");
	sb.append(mRotate);
	sb.append(",");
	sb.append(mScale);
	sb.append(")");
	return sb.toString();
}

}
