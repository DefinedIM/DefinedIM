# DefinedIM

不受限制的即时通讯

*本仓库为服务端仓库*

## Features
+ 基于 `SM2` 非对称加密算法的端到端加密通讯
+ 多平台客户端（计划中）
+ 去中心化的设计理念，适于中小型组织内部使用
+ 高度可配置的服务端
+ 支持插件自由扩展

### 扩展
[插件说明](docs/plugin.md)

## 构建

### 依赖

JDK 17  
Rust ~~nightly~~ 任意通道最新工具链  
Gradle

### 打包
```
gradlew shadowJar
```
生成的`jar`文件位于`build/libs/`,可以直接运行

## 使用的其他开源仓库
- [libsm](https://github.com/citahub/libsm/)
- [blake3](https://github.com/blake3-team/blake3)
- [fastjson2](https://github.com/alibaba/fastjson2)
- [log4j](https://github.com/apache/logging-log4j2)

## License
代码以`MPL 2.0`协议发布
