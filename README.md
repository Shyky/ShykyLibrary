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
### 初始化
由于shyky_library中某些类依赖BaseApplication这个类，所以需要你的工程下自定义一个Application类并继承com.shyky.library.BaseApplication这个类，否则使用了某些类会报空指针异常！！！
继承com.shyky.library.BaseApplication需要实现getAppVersion、getBuildType这两个方法，示例：
```java
public class MyApplication extends BaseApplication {
    @NonNull
    @Override
    protected String getAppVersion() {
        return BuildConfig.VERSION_NAME;
    }

    @NonNull
    @Override
    protected String getBuildType() {
        return BuildConfig.BUILD_TYPE;
    }
}
```
注意：上面的BuildConfig是你的工程中的BuildConfig类，而不是shyky_library中的BuildConfig类。
### BaseActivity
```java
import com.shyky.library.view.activity.base.BaseActivity;
public class MainActivity extends BaseActivity {
    @Override
    public int getContentViewLayoutResId() {
        return R.layout.activity_main;
    }
}
```
不用再写setContentView(R.layout.activity_main)，除了在protected void onCreate(Bundle savedInstanceState)方法中初始化view之外，BaseActivity还额外提供了下面三个方法：
```java
    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    public void findView(@NonNull Bundle savedInstanceState) {
        super.findView(savedInstanceState);
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
    }

    @Override
    public void initListener(@NonNull Bundle savedInstanceState) {
        super.initListener(savedInstanceState);
    }
```
