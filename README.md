# PlayJMAndroid
:fire::fire::fire:玩Android客户端，基于Jetpack组件库+Coroutines(协程)，Kotlin编写的mvvm架构项目，主要是学习JetPack组件库、协程的练手项目
## 项目截图
 ![image](https://raw.githubusercontent.com/DwyaneQ/PlayJMAndroid/master/screenshots/screenshots_1.png)
  ![image](https://raw.githubusercontent.com/DwyaneQ/PlayJMAndroid/master/screenshots/screenshots_2.png)
  ![image](https://raw.githubusercontent.com/DwyaneQ/PlayJMAndroid/master/screenshots/screenshots_3.png)
  ![image](https://raw.githubusercontent.com/DwyaneQ/PlayJMAndroid/master/screenshots/screenshots_4.png)
  ![image](https://raw.githubusercontent.com/DwyaneQ/PlayJMAndroid/master/screenshots/screenshots_5.png)
  ![image](https://raw.githubusercontent.com/DwyaneQ/PlayJMAndroid/master/screenshots/screenshots_6.png)
  ![image](https://raw.githubusercontent.com/DwyaneQ/PlayJMAndroid/master/screenshots/screenshots_7.png)
  ![image](https://raw.githubusercontent.com/DwyaneQ/PlayJMAndroid/master/screenshots/screenshots_8.png)
  ![image](https://raw.githubusercontent.com/DwyaneQ/PlayJMAndroid/master/screenshots/screenshots_9.png)
  ![image](https://raw.githubusercontent.com/DwyaneQ/PlayJMAndroid/master/screenshots/screenshots_10.png)
  ![image](https://raw.githubusercontent.com/DwyaneQ/PlayJMAndroid/master/screenshots/screenshots_11.png)
## 核心框架组件
### Kotlin
  代码量减少的不是一星半点，各种语法糖，拓展函数，好处不必多说，懂的自然懂
### MVVM架构
  ![image](https://raw.githubusercontent.com/DwyaneQ/PlayJMAndroid/master/screenshots/mvvm.png)
  
  MVVM 要解决的问题和 MVC，MVP 大同小异：控制逻辑，数据处理逻辑和界面交互耦合，并且同时能将 MVC 中的 View 和 Model 解耦，还可以把 MVP 中 Presenter 和 View 也解耦。
  在 MVVM 中，数据的流向是这样的：
View 产生事件，自动通知给 ViewMode，ViewModel 中进行逻辑处理后，通知 Model 更新数据，Model 更新数据后，通知数据结构给 ViewModel，ViewModel 自动通知 View 更新界面。
### Retrofit+Coroutines(协程)网络请求
  Retrofit框架安卓靓仔们都不陌生，主要说一下Coroutines协程,Kotlin v1.3引入的概念，采用"同步"的写法来处理异步的操作，大大减少了处理复杂繁多的回调事件，以少量的代码来处理线程切换、异步操作
### 核心组件
  * LiveData
  * DataBinding
  * Navigation
  * ViewModel
  
### 黑夜模式
  * 项目主题设置为继承自DayNight主题,<style name="AppTheme" parent="Theme.AppCompat.DayNight">
 
 * AppCompatDelegate.setDefaultNightMode()方法设置是否是黑夜模式，或跟随系统等
  //  开启黑夜模式
  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
  //  关闭黑夜模式
  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
  
  * 同时需要资源目录适配黑夜模式的color、drawable等
  创建color-night、drawable-night资源目录，添加与常规资源相同的文件名，不同颜色的资源文件以设置黑夜模式下的资源、颜色展示

## 感谢
   * @DWQ
   * @CarGuo
