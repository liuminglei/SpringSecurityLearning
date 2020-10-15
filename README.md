> 转载请注明作者及出处：
> https://blog.csdn.net/liuminglei1987/article/details/107538666
> 本文出自[银河架构师](https://blog.csdn.net/liuminglei1987)的博客。


说起来Web应用安全，通用的方案无非 Spring Security 和 Apache Shiro。这两者我们在此不做比较，用Spring Security多，无非是因为 Spring 框架的“裙带关系”，当然了，也有一些其它原因，如CAS集成、OAuth2集成等等，都有比较成熟的集成框架方案。

Spring Security框架，说实话，比较复杂，好多人一开始不太理解，只会照搬网上的答案，遇到问题解决不了。这是非常不好的习惯，技术方案一定是自己能解决的，或者说比较容易找到解决方案的，不然生产环境出问题、亦或是后续业务发展不满足等等，都会出现比较大的问题。

本系列正是基于此，一方面巩固一下对于Spring Security框架的理解，另一方面，将一些常见场景的解决方案做一些分享，同时，对自己也是又一次深层次的学习。

另外，本系列计划分别以SpringBoot、SpringMVC两种不同的版本进行讲解，目前讲解的是SpringBoot版本，更适合于目前技术开发环境，毕竟新产品还在用SpringMVC的已经很少了。但是，后续也会陆续出一些对应的SpringMVC版本的文章，敬请期待吧！！！

本系列源码：gitee：https://gitee.com/xbd521/SpringSecurityLearning， github：https://github.com/liuminglei/SpringSecurityLearning， 不要忘了点赞支持哦，感谢感谢！！！


汇总文章：[史上最简单的Spring Security教程：终极篇，注意，这不是重点，不是最后一篇！！！](https://blog.csdn.net/liuminglei1987/article/details/107538666)



# SpringBoot版本：

* [未完待续......]
* [史上最简单的Spring Security教程（三十五）：RememberMe记住我之更安全的实现方式-持久化token存储方式](https://blog.csdn.net/liuminglei1987/article/details/109098937)
* [史上最简单的Spring Security教程（三十四）：用户名密码验证码（记住我）登录与CA登录融合方案](https://blog.csdn.net/liuminglei1987/article/details/109098859)
* [史上最简单的Spring Security教程（三十三）：用户名密码登录新增验证码和RememberMe（记住我）选项](https://blog.csdn.net/liuminglei1987/article/details/109005753)
* [史上最简单的Spring Security教程（三十二）：默认用户名密码记住我登录与CA登录融合方案](https://blog.csdn.net/liuminglei1987/article/details/109005723)
* [史上最简单的Spring Security教程（三十一）：默认用户名密码登录新增RememberMe（记住我）选项](https://blog.csdn.net/liuminglei1987/article/details/108830717)
* [史上最简单的Spring Security教程（三十）：自定义用户名密码及验证码登录与CA登录方式共存详细实现及配置](https://blog.csdn.net/liuminglei1987/article/details/108830683)
* [史上最简单的Spring Security教程（二十九）：用户名密码登录添加验证码选项](https://blog.csdn.net/liuminglei1987/article/details/108706016)
* [史上最简单的Spring Security教程（二十八）：CA登录与默认用户名密码登录共存详细实现及配置](https://blog.csdn.net/liuminglei1987/article/details/108693182)
* [史上最简单的Spring Security教程（二十七）：AuthenticationManager默认实现之ProviderManager详解](https://blog.csdn.net/liuminglei1987/article/details/108644881)
* [史上最简单的Spring Security教程（二十六）：DaoAuthenticationProvider详解](https://blog.csdn.net/liuminglei1987/article/details/108279836)
* [史上最简单的Spring Security教程（二十五）：UsernamePasswordAuthenFilter详解](https://blog.csdn.net/liuminglei1987/article/details/108280067)
* [史上最简单的Spring Security教程（二十四）：自定义用户名密码参数名及用户名密码验证路径](https://blog.csdn.net/liuminglei1987/article/details/108194357)
* [史上最简单的Spring Security教程（二十三）：自定义AccessDecisionManager实现类选举法的访问决策](https://blog.csdn.net/liuminglei1987/article/details/108194162)
* [史上最简单的Spring Security教程（二十二）：自定义AccessDecisionManager实现简单的访问决策](https://blog.csdn.net/liuminglei1987/article/details/108099022)
* [史上最简单的Spring Security教程（二十一）：AccessDecisionManager简介及自定义访问决策管理器](https://blog.csdn.net/liuminglei1987/article/details/107904526)
* [史上最简单的Spring Security教程（二十）：自定义AccessDecisionVoter实现必须全部拥有请求所需权限才可访问的需求](https://blog.csdn.net/liuminglei1987/article/details/107861483)
* [史上最简单的Spring Security教程（十九）：AccessDecisionVoter简介及自定义访问权限投票器](https://blog.csdn.net/liuminglei1987/article/details/107861391)
* [史上最简单的Spring Security教程（十八）：FilterSecurityInterceptor实现每个请求只处理一次](https://blog.csdn.net/liuminglei1987/article/details/107806965)
* [史上最简单的Spring Security教程（十七）：FilterSecurityInterceptor默认初始化逻辑剖析](https://blog.csdn.net/liuminglei1987/article/details/107806834)
* [史上最简单的Spring Security教程（十六）：FilterSecurityInterceptor详解](https://blog.csdn.net/liuminglei1987/article/details/107662200)
* [史上最简单的Spring Security教程（十五）：资源权限动态控制（FilterSecurityInterceptor）](https://blog.csdn.net/liuminglei1987/article/details/107606012)
* [史上最简单的Spring Security教程（十四）：动态权限（自定义UserDetailsService）](https://blog.csdn.net/liuminglei1987/article/details/107605953)
* [史上最简单的Spring Security教程（十三）：动态用户（自定义UserDetailsService）](https://blog.csdn.net/liuminglei1987/article/details/107537338)
* [史上最简单的Spring Security教程（十二）：@PreAuthorize注解实现权限控制](https://blog.csdn.net/liuminglei1987/article/details/107413061)
* [史上最简单的Spring Security教程（十一）：url区分不同的登录失败场景](https://blog.csdn.net/liuminglei1987/article/details/107363408)
* [史上最简单的Spring Security教程（十）：AuthenticationFailureHandler高级用法](https://blog.csdn.net/liuminglei1987/article/details/107181973)
* [史上最简单的Spring Security教程（九）：自定义用户登录失败页面](https://blog.csdn.net/liuminglei1987/article/details/107107782)
* [史上最简单的Spring Security教程（八）：用户登出成功LogoutSuccessHandler高级用法](https://blog.csdn.net/liuminglei1987/article/details/107059399)
* [史上最简单的Spring Security教程（七）：用户登出成功url配置](https://blog.csdn.net/liuminglei1987/article/details/107059346)
* [史上最简单的Spring Security教程（六）：用户登出](https://blog.csdn.net/liuminglei1987/article/details/107059286)
* [史上最简单的Spring Security教程（五）：成功登录SuccessHandler高级用法](https://blog.csdn.net/liuminglei1987/article/details/106961020)
* [史上最简单的Spring Security教程（四）：成功登录页面](https://blog.csdn.net/liuminglei1987/article/details/106936595)
* [史上最简单的Spring Security教程（三）：不拦截系统资源](https://blog.csdn.net/liuminglei1987/article/details/106896276)
* [史上最简单的Spring Security教程（二）：自定义登录页](https://blog.csdn.net/liuminglei1987/article/details/106896220)
* [史上最简单的Spring Security教程（一）：一分钟搭建SpringSecurity](https://blog.csdn.net/liuminglei1987/article/details/106873497)


# 相关文章：

* [因为一句代码，老大差点拿我祭旗！Spring Security authorizeRequests 顺序问题不容忽视](https://blog.csdn.net/liuminglei1987/article/details/106961040)
* [避坑指南（三）：Spring Security Oauth2框架如何初始化AuthenticationManager](https://blog.csdn.net/liuminglei1987/article/details/103963070)


# 后续


* 本系列会持续更新，敬请期待




# 支持我

笔者开通了个人微信公众号【银河架构师】，分享工作、生活过程中的心得体会，填坑指南，技术感悟等内容，会比博客提前更新，欢迎订阅。

技术资料领取方法：关注公众号，回复微服务，领取微服务相关电子书；回复MK精讲，领取MK精讲系列电子书；回复JAVA 进阶，领取JAVA进阶知识相关电子书；回复JAVA面试，领取JAVA面试相关电子书，回复JAVA WEB领取JAVA WEB相关电子书。

![@银河架构师](https://img-blog.csdnimg.cn/20200120104422781.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xpdW1pbmdsZWkxOTg3,size_16,color_FFFFFF,t_70)