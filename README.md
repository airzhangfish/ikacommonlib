# ikacommonlib
ikacommonlib is a open source tool package. include a full 2D/3D game framework and a lot of tools base on android. it let you build game and app quickly。
it also can use ika_map_editor ，ika_anime_editor_V2，IKA_Map_collision_editor to create sprite and map.
===
ikacommonlib 是一个工具集，包含了一整套基于andriod 的2D/3D游戏框架以及大量工具，无需其他依赖的情况下，可以快速构建app以及游戏原型。
结构并不复杂，可快速阅读源码和demo快速上手。
并且可配合ika_map_editor地图编辑器 ，ika_anime_editor_V2动画编辑器，IKA_Map_collision_editor碰撞编辑器等工具使用。是一套简单开源的2D/3D游戏开发解决方案。

项目包含两部分，demo和lib。可直接下载demo和lib导入查看内容。

===
使用说明：
1.在build.gradle添加jitpack数据源

allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

2.在依赖中添加
dependencies {
	        compile 'com.github.airzhangfish:ikacommonlib:1.0'
	}
  
3.刷新即可使用。
