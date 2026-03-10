# 坦克大战APK - GitHub构建指南

## 步骤1：创建GitHub仓库

1. 登录GitHub账号
2. 点击右上角"+" → "New repository"
3. 输入仓库名称：`tank-battle-android`
4. 选择"Public"（公开）
5. 勾选"Add a README file"
6. 点击"Create repository"

## 步骤2：上传项目文件

### 方法A：使用Git命令行
```bash
# 克隆您的仓库
git clone https://github.com/您的用户名/tank-battle-android.git
cd tank-battle-android

# 复制所有项目文件到此文件夹
# 然后提交
git add .
git commit -m "添加坦克大战Android游戏"
git push origin main
```

### 方法B：使用GitHub网页界面
1. 进入仓库页面
2. 点击"Add file" → "Upload files"
3. 拖拽或选择所有项目文件
4. 点击"Commit changes"

## 步骤3：查看构建结果

1. 上传完成后，GitHub Actions会自动开始构建
2. 点击仓库顶部的"Actions"标签
3. 查看构建状态（绿色√表示成功）
4. 构建完成后，在"Artifacts"部分下载APK

## 文件说明

必须上传的文件：
- ✅ `.github/workflows/build.yml` - 构建配置
- ✅ `app/` - 应用代码和资源
- ✅ `gradle/` - Gradle构建工具
- ✅ `build.gradle`
- ✅ `settings.gradle`
- ✅ `gradlew` 和 `gradlew.bat`
- ✅ `.gitignore`
- ✅ `README.md`

不需要上传的文件：
- ❌ `local.properties`（包含本地SDK路径）
- ❌ `build/`文件夹（自动生成）

## 构建成功后的下载

构建完成后，您会看到：
- 在"Actions"页面找到最新的构建
- 点击进入构建详情
- 在"Artifacts"部分下载`tank-battle-apk.zip`
- 解压后得到`app-debug.apk`

## 注意事项

- 首次构建可能需要5-10分钟
- 确保所有文件都正确上传
- 如果构建失败，检查错误日志进行调试

现在您就可以通过GitHub免费构建APK，无需下载Android Studio！