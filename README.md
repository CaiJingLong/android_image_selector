# android_image_selector

一个仿微信的图片选择器,去除了大图放大/ 图片编辑/原图选项

[![Release](https://jitpack.io/v/caijinglong/android_image_selector.svg)](https://jitpack.io/#caijinglong/android_image_selector)

## install

可以使用[jitpack](https://jitpack.io/#caijinglong/android_image_selector)引用

or clone library

## sample 代码

增加以下代码到项目中

以下代码需要在 activity 内,因为需要访问权限

代码示例为 kotlin ,java 的话同理

初始化代码

```kotlin
val picker = GalleryPicker(this) //初始化库
```

加入 Activity 的回调,这部分代码是 android 6.0 权限请求相关,如果没有,则会直接打开失败

```kotlin
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        picker.permissionsUtils.dealResult(requestCode, permissions, grantResults)
    }
```

打开图片选择器

```kotlin
picker.openWithOption(GalleryOption().apply { //这里有一些选项,都有默认值,也可以自定义,可以点到类里去看源码
    themeColor = Color.parseColor("#3772E0")
    textColor = Color.WHITE
})

/// option是一些选项
/// requestCode是请求码,有默认值,也可以自定义,在onActivityResult时使用
/// 这里有一个危险选项,ignorePermission,不建议使用,这个选项会忽略权限检查,直接打开选择器,如果你确信你真的有权限,则可以设置为true,当无权限时,大于andriod 6.0的会直接崩溃
fun openWithOption(option: GalleryOption, requestCode: Int = REQUEST_CODE, ignorePermission: Boolean = false) {
    ///
}
```

```kotlin
class GalleryOption {

    var rowCount = 4  // 单行的图片数量

    val maxSelected = 9 //最大选择数量

    var itemRadio = 1f //item的宽高比

    var padding = 1 // item间的间距,单位是像素(px)

    @ColorInt
    var textColor = Color.WHITE  //图片的颜色

    @ColorInt
    var themeColor = Color.parseColor("#333333") // 主题色(头部,底部颜色)

    @ColorInt
    var dividerColor = Color.parseColor("#333333") //分割线的颜色

    @ColorInt
    var disableColor = Color.GRAY   // 当不可继续选择时,checkBox的颜色

    var maxTip: String = "已经选择了${maxSelected}张图片" // 当选择达到上限时的提示语

}
```

这部分代码是接收结果时的回调,这里的 requestCode 就是上一步传入的

```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GalleryPicker.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val arrayList = data?.getParcelableArrayListExtra<ImageEntity>(GalleryPicker.RESULT_LIST)
                logi(arrayList)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                logi("取消选择")
            }
        }
    }
```

demo 里的 jks name/alias 都是 dd, 密码都为 123456

## 库冲突

### 图片加载相关

使用的是 Glide 4.4 加载图片,如果你也有 Glide,请自行在依赖中 exclude glide, 或 fork library 自行修改依赖,不保证其他版本的 Glide api 一定可用

### support库
因为库中使用了support库(ViewPager/RecyclerView),版本为28.0.0
如果你使用的android编译版本不是28,或者supoort库有不同的版本引发冲突,考虑使用如下方法

```gradle
//解决V7包，引入冲突，强制使用某个库的统一版本，如统一引入V7某个版本
ext {
    // App dependencies
    supportLibraryVersion = '27.1.1'
 
}

subprojects {
    project.configurations.all {
        resolutionStrategy.eachDependency { details ->
            if (details.requested.group == 'com.android.support'
                    && !details.requested.name.contains('multidex')) {
                details.useVersion "$supportLibraryVersion"
            }
        }
    }
}

```
---------------------

本文来自 脚踏七星 的CSDN 博客 ，全文地址请点击：https://blog.csdn.net/qq998701/article/details/82593875?utm_source=copy 

## LICENSE

MIT
