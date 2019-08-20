# jar-upload-to-nexus

jar-upload-to-nexus为一种批量上传jar包到nexus 服务器的方法

## 使用方法

### 设置 `setting` 文件

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
`java -jar jar-upload-to-nexus-1.0-SNAPSHOT.jar folderPath repositoryId url`，

其中，`folderPath`为将要上传的 jar 包的绝对路径，`repositoryId`为  repository 库的名字，`url`为  repository 的 URL。

例如，需要将目录`C:\Users\Erik\Desktop\testJar`中的所有 jar 包上传到 `jarUploadTest` 中，则完整的命令为：

`java -jar jar-upload-to-nexus-1.0-SNAPSHOT.jar C:\Users\Erik\Desktop\testJar jarUploadTest http://10.180.210.148:8088/repository/jarUploadTest/`
