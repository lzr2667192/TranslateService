Description
===========
TranslateService is based on [YouDao Translation API][1]. Showing you how to use an existing service to build your app.
Tutorial
--------
Simply you need to follow these steps:
Step 1
------
[Apply a Key for you app.][2]
Step 2
------
Remember the API Key & keyfrom, put it in any file you like as Constants.
```java
String KEY_FOR_TRANSLATE = "13******68";
String KEY_FROM = "TranslateHelper";
```
Step 3
------
Try to read the documentation and use it in you app.
```text
数据接口
http://fanyi.youdao.com/openapi.do?keyfrom=<keyfrom>&key=<key>&type=data&doctype=<doctype>&version=1.1&q=要翻译的文本
版本：1.1，请求方式：get，编码方式：utf-8
主要功能：中英互译，同时获得有道翻译结果和有道词典结果（可能没有）
参数说明：
　type - 返回结果的类型，固定为data
　doctype - 返回结果的数据格式，xml或json或jsonp
　version - 版本，当前最新版本为1.1
　q - 要翻译的文本，不能超过200个字符，需要使用utf-8编码
errorCode：
　0 - 正常
　20 - 要翻译的文本过长
　30 - 无法进行有效的翻译
　40 - 不支持的语言类型
　50 - 无效的key
xml数据格式举例
http://fanyi.youdao.com/openapi.do?keyfrom=<keyfrom>&key=<key>&type=data&doctype=xml&version=1.1&q=这里是有道翻译API
<?xml version="1.0" encoding="UTF-8"?>
<youdao-fanyi>
    <errorCode>0</errorCode>
    <!-- 有道翻译 -->
    <query><![CDATA[这里是有道翻译API]]></query>
    <translation>
        <paragraph><![CDATA[Here is the youdao translation API]]></paragraph>
    </translation>
</youdao-fanyi>
json数据格式举例
http://fanyi.youdao.com/openapi.do?keyfrom=<keyfrom>&key=<key>&type=data&doctype=json&version=1.1&q=翻译
{
    "errorCode":0
    "query":"翻译",
    "translation":["translation"], // 有道翻译
    "basic":{ // 有道词典-基本词典
        "phonetic":"fān yì",
        "explains":[
            "translate",
            "interpret"
        ]
    },
    "web":[ // 有道词典-网络释义
        {
            "key":"翻译",
            "value":["translator","translation","translate","Interpreter"]
        },
        {...}
    ]
}
jsonp数据格式举例
http://fanyi.youdao.com/openapi.do?keyfrom=<keyfrom>&key=<key>&type=data&doctype=jsonp&callback=show&version=1.1&q=API
show({
    "errorCode":0
    "query":"API",
    "translation":["API"], // 有道翻译
    "basic":{ // 有道词典-基本词典
        "explains":[
            "abbr. 应用程序界面（Application Program Interface）；..."
        ]
    },
    "web":[ // 有道词典-网络释义
        {
            "key":"API",
            "value":["应用程序接口(Application Programming Interface)","应用编程接口","应用程序编程接口","美国石油协会"]
        },
        {...}
    ]
});
```

[1]: http://fanyi.youdao.com/openapi
[2]: http://fanyi.youdao.com/openapi?path=data-mode