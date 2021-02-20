# RedRockWinterWork# RedRockWinterWork
app选择的是网易云接口文档进行实现

# 1 思路：

  	由于底部导航栏和音乐播放栏目是两个界面共有的上部分的内容存在碎片化的变动 所有上部分使用了Fragment实现切换效果

​	不同的界面通过不同的Fragment实现   

## 		1.1用户界面 ：

[image](https://github.com/False-Mask/RedRockWinterWork/tree/master/Pics/user.jpg)

通过ScrollVIew嵌套RecyclerVIew（显示收藏夹）和其他一些LinearLayout和TextView实现（显示用户的个人信息）	（这样实现貌似在滑动的过程中存在滑动不流畅的问题）

## 		1.2主界面 ：

[image](https://github.com/False-Mask/RedRockWinterWork/tree/master/Pics/main.jpg)

​					1 最先想使用ScrollView实现但是内部嵌套了TextView和RecyclerVIew存在 滑动冲突的问题 

​					2 当时也想过利用NestedScrollView实现主界面的布局   但是达不到我预期想要的结果  NestedScrollView解决了滑动						冲突但是内部RecyclerView是可以滑动的  我又尝试着禁止RecyclerVIew滑动 结果是RecyclerVIew不动了 整个布局也滑动						不了了 （我不清楚是否能用NestedScrollVIew实现主界面但是由于当时比较急就放弃了）   

​					3 最后我尝试使用了RecyclerVIew重新getType方法内部定义一个枚举类实现一个多布局操作   起初是比较困难的 （也就是布					局复杂了之后全部代码全部集中在Adapter内部几乎无法实现） 考虑到这样写主界面不好维护 而且 越写越复杂 我就在网上找					到了https://www.cnblogs.com/cloudfloating/p/9817251.html利用工场设计模式实现多布局 （他将VIew和Data通过两个接					口分离开  然后布局的加载操作通过一个adapter实现 不同布局通过不同的Holder类实现内容加载）  当时我就利用他的模式 					用ViewPager做了一个Banner图嵌套进 了RecyclerView 然后还加入了一个标题栏（1个TextVIew）和几个歌单信息（1个					ImageVIew还有1个TextView）通过一个GridManager布局管理器实现了主界面 （RecyclerView嵌套貌似也存在滑动阻塞的					问题 但是 是我现在能想到的最好的实现布局的方法了）

## 		1.3 歌曲的播放界面 :

[image]( https://github.com/False-Mask/RedRockWinterWork/tree/master/Pics/song)

​		通过点击MainActivity下面的歌曲栏目进入底部的3个图标是我利用自定义view写的 最先是使用ImageVIew最后发现图片的显示效果		不是很好 而且颜色不知道是什么问题比较淡 而且大小还不好确定



## 		1.4搜索界面:

​	[image](https://github.com/False-Mask/RedRockWinterWork/tree/master/Pics/search.jpg)

​			输入搜索内容点击搜索按钮 或者 软键盘的回车键就搜索到了内容 底部通过一个recyclerview呈现搜索内容

## 		1.5登陆界面:

​	[image](https://github.com/False-Mask/RedRockWinterWork/tree/master/Pics/login.jpg)

​			登陆界面比较简单就几个简单的按钮和EditText点击触发一些内容





# 2功能实现

## 	2.1 音乐播放

​			通过Service和MediaPlayer结合 同时我想到每个activity都可能会用到这个播放功能 所以我 写了两个接口 分别绑定activity和他的			presenter这样每个activity需要播放音乐只需要绑定一下service然后调用presenter在调用service内部封装的方法就够了（当时又			考虑到每个界面需要对播放进度进行调整 所以我还在service里面写了一个循环刷新）



## 	2.2自定义view

​	[gif](https://github.com/False-Mask/RedRockWinterWork/tree/master/Pics/view.jpg)

​			我当时写了一个显示加载动画和播放进度 播放状态（暂停/播放）的  播放/暂停按钮  通过重写view的ondraw方法 由于view是动态			的  所以我先把静态的画了   然后再进行动态的绘制

## 	2.3网络请求等一系列 

​		内部通过协程封装了一个Get和Post请求 再通过MVP架构实现View和Model层的分离（实现的思路是很清晰但是就是代码有亿点长）



# 3 功能展示

## 	  3.1Banner：

[gif](https://github.com/False-Mask/RedRockWinterWork/tree/master/GIFs/banner_show.gif)



## 	3.2登陆：

[gif](https://github.com/False-Mask/RedRockWinterWork/tree/master/GIFs/login.gif)



## 	3.3主界面的音乐歌单

[gif](https://github.com/False-Mask/RedRockWinterWork/blob/master/GIFs/recommond_and_top_favorites.gif)

## 	3.4下拉刷新

[gif](https://github.com/False-Mask/RedRockWinterWork/blob/master/GIFs/refresh.gif)

## 	3.5个人收藏夹的歌单

[gif](https://github.com/False-Mask/RedRockWinterWork/blob/master/GIFs/my_favorites.gif)

## 	3.6点击播放音乐

[gif](https://github.com/False-Mask/RedRockWinterWork/blob/master/GIFs/play_music.gif)

## 	3.7音乐播放界面

[gif](https://github.com/False-Mask/RedRockWinterWork/blob/master/GIFs/music_ui.gif)

## 	3.8搜索界面

[gif](https://github.com/False-Mask/RedRockWinterWork/blob/master/GIFs/search.gif)



# 4 心得体会

这次作业是我到现在为止萌新生涯中花费最多时间完成的app 为了准备他我重头学了 网络编程 （在之前我连HttpUrlConnect 都忘了） ,  Kotlin , IO(本来是打算拿去做缓存的 由于我封装的是对象流所以频繁爆EOF异常 最后还是没用上），MVP ，Service，MediaPlayer ...

之后便是花费20多天疯狂**敲代码** **改Bug**  ~~猜接口文档的变量~~ 很难相信我竟然打出来了一个app 虽然还是很差     

在其中过程中 我用到了很多之前学过的东西  RecyclerVIew Fragment Service ....  回想起前一个多月还是个只会~~抄代码~~的小白 很多系统组件根本就不会用     感触 

## 			4.1  学习的过程中必须打项目

​		随着时间的推进Android或是其他的技术必须和实际操作相互结合  因为每一项技术都存在某个应用的场景 如果你在实际开发			过程中没有遇上相应的场景  你基本上无法了解    好比如最先学java的时候根本就不知道接口有什么用 学了Android 之后有了接口			的运用 比如什么 网络编程接口回调 监听事件接口回调  慢慢的就开始明白了    这一点也暗示我如果有什么东西是不明白的那主要			是因为是我没有遇见使用它的场景  要想理解更加深刻只能做app遇见了便学    

最先开始的时候我是打算用java做app的但是我之前抽过几天看过Kotlin的语法 我发现kotlin和java语法上其实有很多的相似 我就换了java使用kotlin   起初只会一些基础的语法 var val fun object companion object interface...真遇上了Android 完全不知道怎么写 然后我就在网上慢慢查 最后我渐渐的会打代码了（虽然还是一手java式的kotlin） 

## 			4.2 学会看官方文档 学会 ~~嘤~~ 英语  关注Android的走势

在学习的过程中我也发现了Android 的发展比较迅猛（之前完全没有过关注）  在翻阅一些教程的时候有很多的方法已经被遗弃了 比如我记得在倒计时的时候利用Handler就会是这样的~~Handler~~而且当时我在学的时候发现很多的教程都来自官方文档 （英文的我怎么看得懂）   所以成为一个大佬 ~~嘤~~英语真的很重要

## 			4.3 android的学习不仅仅是系统组件的学习还有很多的自定义和内部					实现

最初期android的时候我以为就只是玩玩拼图把系统组件拼出来 现在我不这么认为了 玩系统组件只能实现一些简单的操作  真					实的app开发过程中 难免会遇上一些复杂的操作 什么自定义view 自定义ViewGroup 自定义框架  学习系统组件只能为之后的					自定义XXX的学习打基础     在学到一定的程度的时候还得翻阅源码 看懂实现原理   （这样系统组件出了问题就可以自己解决或者出现性能问题就能自己实现优化）

## 			4.4 ~~我是垃圾~~ 革命尚未完成 同志仍需努力

越学越发现还有很多没学什么Matrix Camera OPenGl Jetpack LiveData 热修复 组件化 插件化 （名词都记不住） 路还有很长要走啊



# 5 待优化

## 1 Bug

​		由于我是通过绑定service依靠service提供给activity播放的效果   我在activity的onStart方法进行service的绑定 在onPause（当时是想的一个activity都到后台了就暂时用不上Service了）方法上进行解绑  但是不知道为什么可能会重复解绑 由于时间原因我只是try catch了异常

​		网络请求我封装的HttpUrlConnection但是有的时候会爆一些奇奇怪怪的异常 网上搜索了一些 基本上都是服务器拒绝连接 或者 连接		超时 或者没有权限 HttpUrlConnection的了解程度还不是很好 

## 2 滑动

​       不太懂自定义view事件分发也不太懂 在recyclerView和ScrollView内部嵌套一些ViewPager或者是RecyclerView在滑动的时候会有阻塞的感觉  这个可能会和他们的底层实现 有关 暂时还没有解决的办法

## 3 自定义view引起卡顿的问题

​		可能是由于自定义view在绘制过程中刷新率太高了 导致的主界面有亿点卡  我的解决方案就是把那玩意  在歌曲刷新或者用户交互的		时候调用invalidate()现在是不卡顿了不知道以后会不会    （不知道放在surfaceView上绘制会不会优化一些性能）

## 4 歌曲相关界面的刷新问题

​		播放歌曲等一系列问题我是依靠的Service来实现的   activity需要的****当前播放进度 歌曲的作者或者 歌曲的图片 歌曲的名称** 我在内部是利用一个循环（1秒钟刷新一次）这样用户点击之后并不能迅速得到内容的信息

## 5 使用框架的问题

​      现在我所知道的有3个 一个MVC MVP MVVM  MVC的耦合度太高了已经被淘汰  MVP虽然通过了接口解耦合但是存在着接口漫天飘的问题	而且实现一个功能模块以后就到处是Presenter Contract Model  。MVVM是Google当前推荐使用的框架具体不是很了解   据说是通过一个ViewModel来管理数据的 （和mvp有点点类似）

但是由于太多时间都拿去填坑了	知道MVVP的时候已经使用了一大片MVP了（MVVM就没有花时间去深入了解）
