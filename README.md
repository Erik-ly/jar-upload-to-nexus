# jar-upload-to-nexus

jar-upload-to-nexus为一种批量上传jar包到nexus 服务器的方法

## 使用方法

### 设置 `settings.xml` 文件

在 `settings.xml` 文件中的 `servers`中添加需要将 jar 包上传到 repository 库的名字，Nexus 的用户名及密码，如：
```
  <servers>
  
	<server>
		<id>jarUploadTest</id>
		<username>admin</username>
		<password>password</password>
       </server>
  
  </servers>
```

### 上传 jar 包

可以直接使用 ` target` 中的 jar 包，执行如下命令：

`java -jar jar-upload-to-nexus-1.0-SNAPSHOT.jar folderPath repositoryId url`，

- `folderPath`为将要上传的 jar 包的绝对路径，但不能为 `.m2` 中的文件夹，如果上传本地 `.m2` 中的 jar 包，需要将该文件夹拷贝到普通文件夹中
- `repositoryId`为  repository 库的名字
- `url`为  repository 的 URL

例如，需要将目录`C:\Users\Erik\Desktop\testJar`中的所有 jar 包上传到 `settings.xml` 文件中所设置的 `jarUploadTest` 中，则完整的命令为：

`java -jar jar-upload-to-nexus-1.0-SNAPSHOT.jar C:\Users\Erik\Desktop\testJar jarUploadTest http://10.180.210.148:8088/repository/jarUploadTest/`
