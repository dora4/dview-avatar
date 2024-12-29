dview-avatar
![Release](https://jitpack.io/v/dora4/dview-avatar.svg)
--------------------------------

##### 卡名：Dora视图 Avatar 
###### 卡片类型：效果怪兽
###### 属性：暗
###### 星级：3
###### 种族：魔法师族
###### 攻击力/防御力：1000/1200
###### 效果：此卡不会因为对方卡的效果而破坏，并可使其无效化。此卡攻击里侧守备表示的怪兽时，若攻击力高于其守备力，则给予对方此卡原攻击力的伤害，并抽一张卡。此卡攻击成功后，可以直接从卡组特殊召唤一只等级不高于4的名字带有「Dora视图」的怪兽。

#### Gradle依赖配置

```groovy
// 添加以下代码到项目根目录下的build.gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
// 添加以下代码到app模块的build.gradle
dependencies {
    implementation 'com.github.dora4:dview-avatar:1.4'
}
```
