# Pukedex2
一款神奇宝贝信息查看app
#### 帮助自己更好的了解架构组件和协程的使用：

apk下载地址：(https://github.com/chenqi5256969/Pukedex2/blob/master/apk/app-debug.apk)
协程这一块主要是用到flow来更好进行流式调用，后续会改成谷歌在文档中推荐的MutableStateFlow

#### 采坑日志：
1.对于room来说，不能直接存储list对象，需要对list进行转换。
具体代码可以参考：TypeResponseConverter这个类
因为本项目是用的moshi进行json格式解析，如果你用到是gson，参考一下代码：

```kotlin
  val gson = Gson()

    @TypeConverter
    fun toHomeArticleList(json: String): MutableList<HomeArticleResp.Detail> {
        val type = object : TypeToken<MutableList<HomeArticleResp.Detail>>() {}.type
        return gson.fromJson<MutableList<HomeArticleResp.Detail>>(json, type)
    }

    @TypeConverter
    fun homeArticleListToJson(data: MutableList<HomeArticleResp.Detail>): String {
        return gson.toJson(data)
    }
```

2.对于ViewModel来说，如果你用到了hilt注解框架，请使用@ViewModelInject 标注在viewmodel的构造器上。

3.当你想在Fragment中使用注解时，别忘了在对应的Activity中也要进行标注



![](https://github.com/chenqi5256969/Pukedex2/blob/master/preview/Screenshot_2021-03-26-15-25-47-716_com.revenco.pukedex2.png)





