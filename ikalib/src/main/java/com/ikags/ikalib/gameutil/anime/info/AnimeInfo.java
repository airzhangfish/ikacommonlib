package com.ikags.ikalib.gameutil.anime.info;

import com.ikags.ikalib.util.IKALog;

/**
 * 动画信息
 * @author zhangxiasheng
 *
 */
public class AnimeInfo{
	public int[] mFrameIDlist = null;
	
	public void printList(){
		if(mFrameIDlist!=null){
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<mFrameIDlist.length;i++){
				sb.append(mFrameIDlist[i]);
				sb.append(",");
			}
			String data=sb.toString();
			IKALog.v("AnimeInfo", "frameidlist="+data);
		}
	}

}
