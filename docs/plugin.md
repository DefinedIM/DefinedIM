# 插件

开发插件需要依赖本仓库的jar包.

[示范插件](https://github.com/DefinedIM/DefinedIM_ExamplePlugin)

需要您实现一个继承自`org.definedim.plugin.DefinedIMPlugin`的类, 其中有一些函数需要实现:

+ `void onLoad()`
+ `void onReload()`
+ `void onExit()`

以上三个函数均阻塞主线程

## 打包结构
正常把你的插件项目打包为`jar`文件,在里面加入一个`plugin.json`用于标识这是一个插件,格式如下
```json
{
  "name": "字符串,你的插件名",
  "version": "字符串,你的插件版本",
  "author": "字符串,作者",
  "classpath": "字符串,继承org.definedim.plugin.DefinedIMPlugin类的类的完整路径,即 包名.类名",
  "link": "字符串,相关链接(本项可选)"
}

```
