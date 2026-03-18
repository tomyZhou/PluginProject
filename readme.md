插桩（代理）方式实现插件化启动插件里的Activity，插件里的Activity不用在宿主Manifest.xml中声明。


方案说明

1、创建一个插桩，占位Activity，为了实现Activity进出栈

2、通过DexclassLoader获取到插件包里的类,通过Resources获取插件包里的资源，并创建出插件里的Activity对象 

3、这时的插件Activity对象没有运行环境context,我们需要把宿主里占位Activity环境传给插件里的对象 

4、当要执行插件Activity里的生命周期方法时，使用宿主占位Activity里的环境来执行相应操作。


注意事项： 

遇到Attempt to load writeable dex file的问题，处理方式

做插件化的时候，把apk文件从assets里面拷贝到存储空间里面，开始用的存放目录是

context.getExternalFilesDir("plugin"), 

对应的存储目录是：storage/emulated/0/Android/data/com.plugin.plugindemo/plugin.apk 

然后使用DexClassLoader加载插件包，一直碰到这个报错：

 Attempt to load writeable dex file 

设置了 file.setReadOnly()后还是报这个错。 

后来将存放目录改成 context.getFilesDir() 对应的存储目录是：

/data/user/0/com.plugin.plguindemo/files/plugin.apk

再调用file.setReadOnly()后就不报错了。

下面是运行截图：
![示例图片1](/screenshoot/1.jpg)
![示例图片2](/screenshoot/2.jpg)
![示例图片3](/screenshoot/3.jpg)
![示例图片4](/screenshoot/4.jpg)
