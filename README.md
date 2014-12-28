#HttpHelper 

####对HttpClient进行封装，简化get/post操作，使用简单方便。   
  
在Java开发中，HttpClient的应用非常广泛。用的多了，自然会想去把他封装一下，写一个自己的小类库，方便自己日常学习工作使用。于是，就结合工作情况，利用业余时间简单的对HC进行了一下封装，最终的结果就是发起Http请求只用传递URL，[编码]，[参数]，一个函数就能返回响应数据。  

例如，如果你想以Post方式，name=xxx为参数访问domain.com,则只需构建出helper对象，将参数放到map中，只需String result=helper.post("domaon.com",map);就可以方便的获得响应内容。  

还有一个提取Cookie的方法，我对Cookie这边了解不是太多，可能一些网站上的Cookie会提取错（目前我还没碰到），这一点就要靠大家指教了，我会慢慢完善这个的。

主要有`get()`，`post()`两个方法,对应http的GET/POST请求  

- get()：GET方法，已添加缓存机制，可带参数和cookie  
- post()：POST方法，可带参数和cookie  
- getcookie()：获取cookie
- 更多信息可参见代码    


使用示例：
	HttpHelper helper=HttpHelper.getHelper();
	helper.getCookie("loginurl", map,null);
	System.out.println(helper.get("privateurl"));
	helper.closeClient();

由于技术和经验方面的原因，代码写的可能有很多没有考虑到的地方，望大家不吝指教。