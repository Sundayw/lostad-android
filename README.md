# lostad-android
一、框架简介： 本框架是作者在长期的项目开发中积累的劳动成果，是最优秀android开发框架的整合. 
本项目的目标是为您提供进行二次发的框架，提高您的开发效率！



二、为什么选择使用该框架？

1、集成了大部分所有app开发中需要搭建的必要组件，
并且是对多种组件对比后选择的最优的组件，作者在长期的使用过程中不断的 完善，让您少走很多弯路。 
如：

1)本框架原来使用的是afinal过程中发现其存在很多局限；
比如orm的bean主键为整形时无法保存从服务器加载下来的整形主键等 
（有两种处理方式，一种是修改afinal框架、别一种是直接在客户端直接把int解析到String类型的主键）


2)entity必须要有getter、setter方法才可以进行orm持久化。 个人觉得没必要，贫血模型里的entity一般是不包含业务逻辑的，
没必要使用getter\setter 增加代码量。 本人的作用通常是直接把entity里的属性定义为public . 


3)其它的就不多说了。后来改用xutils了。 :) 2、修复了一些第三方组件的bug,并进行了扩展。
（比如 pullToRefresh-ListView 类似微博的效果，进入列表自动展现刷新提示， 滚动到地步自动出现loadding）. 


3、现成的工具类，提示窗口工具类，主界面、登陆、注册界面和交互流程。


4、良好的架构，您直接继承listad 这个lib工程中的BaseApplication和BaseAppActivity就可以。
当然您必须实现这两个抽象类的抽象方法。


三、本框架具体整合了哪些东东？


xutils （最著名的android开发框架之一）、
PullToRefresh-ListView （最流行的listView）、
gson(最好用的Json解析工具)等。

四、如何使用？ 直接下载本项目，在app项目下的gradle配置文件中的 applicationId 改成您自己的applicationId 
就可以直接在上面进行二次开发了。


五、最后，期待您在使用过本项目后前来发表有任何吐槽或建议