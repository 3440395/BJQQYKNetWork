http://www.cnblogs.com/xiaobaiyey/p/6442282.html?utm_source=itdadao&utm_medium=referral
# 超时
用的是race原理，N秒之后如果第一个任务还没完成，就会抛出超时异常

# 小朋友的APIUtil设计
- 进入app通过强制刷新（不考虑本地缓存），从服务端拿到动态入口json/xml，永久缓存到本地
- APIUtil的getApi方法：apiutil中维护了一个api集合，如果是空，则解析动态入口的本地数据和配置文件（包含重试次数，缓存时间，mock类等机制）中的数据，共同组成了这个集合(这块可以用rxjava的merge操作符，具体参考包建强网络请求封装2视频的46分钟)，如果不为空，则直接从集合中通过key拿api


# rxjava+okhttp

在小朋友网络框架中，可以在XXManager中写各种getXXX的方法，返回一个Observable，内部进行okhttp的网络访问，即可。。。



# RxJava就是为了解决异步回调

# 优朋网络框架
> 为什么我不用retrofit
- 公司的url不固定，但是retrofit必须提供baseurl，只能随便写个，明显不符合retrofit的设计者的心理
- 公司有xml/json，那么就需要用两个retrofit对象了
- 缓存机制不够灵活（对于缓存路径的指定，是否需要缓存等的指定）


- 配置xml或者json
- url通过动态入口获取，所以apiutil用了两个文件中读取，api.xml作为补充



---
1.启动app从动态入口（强制）请求数据，并且缓存到本地
2.当调用ApiConfigManager的时候，就可以结合动态入口数据的缓存和api.xml获取到api对象了
3.根据api的要求做网络请求



# 网络框架的几个要素
- 把所有的URL统一管理
- 数据缓存
- 重试
- 取消请求
- 本地时间校准
- 错误提示
- dialog

