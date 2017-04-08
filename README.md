# ShykyLibrary
A powerful library for Android.

### 功能及亮点
* 快速开发Android项目
* 封装了很多常用的工具类和基础类，如TextUtil、ToastUtil、BaseActivity、BaseFragment等
* 源码注释清楚，代码整齐，命名规范
* 更多

#### 使用Gradle构建时添加一下依赖即可:
```javascript
compile 'com.shyky.library:shyky_library:1.3'
```


#### 使用前配置
##### 需要的权限
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```
##### 初始化
```java
// 在application的onCreate中初始化
@Override
public void onCreate() {
    super.onCreate();
    x.Ext.init(this);
    x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    ...
}
```

### Application入口
由于shyky_library中某些类依赖BaseApplication这个类，所以需要你的工程下自定义一个Application类并继承com.shyky.library.BaseApplication这个类。
