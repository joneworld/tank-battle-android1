# 坦克大战 Android 游戏

这是一个简单的坦克大战Android游戏，使用纯Java和Android Canvas API开发。

## 游戏玩法

- **控制方式**: 点击并触摸屏幕来控制蓝色坦克移动
- **攻击**: 按住触摸时，坦克会自动发射子弹
- **目标**: 消灭所有红色敌方坦克
- **失败**: 被敌方子弹击中会重新开始游戏

## 构建APK

### 环境要求

1. **Android SDK**: 需要安装Android SDK (API 24及以上)
2. **Java JDK**: 需要JDK 8或更高版本
3. **Gradle**: 项目包含Gradle Wrapper，无需单独安装

### 配置Android SDK路径

1. 打开 `local.properties` 文件
2. 取消注释并设置正确的Android SDK路径，例如：
   ```
   sdk.dir=C\:\\Users\\YourUsername\\AppData\\Local\\Android\\Sdk
   ```

### 构建步骤

在项目根目录下执行以下命令：

**Windows系统:**
```bash
gradlew assembleDebug
```

**Linux/Mac系统:**
```bash
chmod +x gradlew
./gradlew assembleDebug
```

### 获取APK

构建成功后，APK文件位于：
```
app/build/outputs/apk/debug/app-debug.apk
```

## 项目结构

```
TankBattle/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/tankbattle/
│   │       │   ├── MainActivity.java      # 主Activity
│   │       │   ├── GameView.java          # 游戏视图
│   │       │   └── GameThread.java        # 游戏线程
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   ├── values/
│   │       │   └── drawable/
│   │       └── AndroidManifest.xml
│   └── build.gradle
├── gradle/
├── build.gradle
├── settings.gradle
└── gradlew / gradlew.bat
```

## 游戏特性

- 🎮 简单的触摸控制
- 🔵 蓝色玩家坦克
- 🔴 红色敌方坦克
- 🧱 随机生成的障碍墙
- 💡 自动射击系统
- 🔄 自动波次生成

## 技术栈

- Android SDK
- Java
- Canvas API
- Custom View
- Surface Rendering

## 许可证

MIT License
