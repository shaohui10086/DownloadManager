# DownloadManager
一个"善变"的下载库
# 介绍
DownloadManager是一个支持切换多种下载库的文件下载框架，支持多任务下载
* 随心切换下载库，目前只支持OkHttp，后续会支持Async Http和HttpUrlConnection
* 自定义下载库：实现Downloader接口实现自己的下载库
* 多个下载监听器选择：AlertDownloadListener,后续会增加更多
* 自定义下载监听器：实现DowaloadListtener接口实现自己的下载监听器
# 使用
首先，在AndroidManifest.xml增加两个权限：网络权限和存储读写权限
	<uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
然后引入
Maven：
	<dependency>
	<groupId>xyz.shaohui.downloadmanager</groupId>
	<artifactId>downloadmanager</artifactId>
	<version>0.5.0</version>
	<type>pom</type>
	</dependency>
Gradle：
	compile 'xyz.shaohui.downloadmanager:downloadmanager:0.5.0'

# Demo
		DownloadManager manager = new DownloadManager();  //初始化DownloadManager
        AlertDownloadListener listener = new AlertDownloadListener.Builder(this)  // 新建一个DownloadListener
                .showFileSize(true)
                .build();
        manager.download(url, fileUri, listener);  // 开始一个下载任务
![Demo](https://github.com/shaohui10086/DownloadManager/blob/master/demo/Gif_20160503_173058.gif)
# TODO
* 暂停任务
