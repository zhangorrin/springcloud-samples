# 使用方法
- - -
## 1. 配置
在application.yml当中配置Fdfs相关参数
```
fdfs:
  connect_timeout: 30
  network_timeout: 30
  charset: UTF-8
  http:
    tracker_http_port: 8080
    anti_steal_token: true
    secret_key: FastDFS1234567890
  tracker_server:
    - 192.168.1.187:22122
```

## 2. 接口说明

### 2.1 使用HttpClient进行二进制数据传输
2.1.1 上传upload

*url : http://host:port/fdfs/byte/upload
 
method : post
 
Content-type : application/octet-stream; charset=UTF-8
 
Params : byte[]
 
*Result : String 上传成功返回fileId

2.1.2 下载download
*url : http://host:port/fdfs/byte/download
 
method : get
 
Content-type : application/json; charset=UTF-8
 
Params : fileId:fileId  上传成功时返回的fileId
*Result : byte[]   getMethod.getResponseBody();

2.1.3 删除delete
*url : http://host:port/fdfs/byte/delete
 
method : get
 
Content-type : application/json; charset=UTF-8
 
Params : fileId:fileId  上传成功时返回的fileId
*Result : String -1 文件不存在,0 删除成功,2 删除失败

2.1.4 查询queryFileInfo
*url : http://host:port/fdfs/byte/queryFileInfo
 
method : get
 
Content-type : application/json; charset=UTF-8
 
Params : fileId:fileId 上传成功时返回的fileId
*Result : json
```json
{"crc32":-753150525,
"fileSize":8611737,
"sourceIpAddr":"192.168.1.185",
"createTimestamp":1476696678000}
```

### 2.2 Http客户端请求
释: 客户端直接调用（不推荐使用）

2.2.1 上传upload
*url : http://host:port/fdfs/file/upload
 
method : post
 
Content-type

Params : file
```html
<form action="/fileDFS/restful/fdfs/upload" enctype="multipart/form-data" method="post">
    上传文件1：<input type="file" name="fileName"><br/>
    <input type="submit" value="提交">
  </form>
```
*Result : String 上传成功返回fileId

2.2.2 下载download
*url : 
http://host:port/fdfs/file/download
 
method : get
 
Content-type
 
 
Params : fileId:fileId 上传成功时返回的fileId
*Result : void getMethod.getResponseBody();

2.2.3 删除delete
*url : http://host:port/fdfs/file/delete
 
method : get
 
Content-type
 
Params : fileId:fileId  上传成功时返回的fileId
*Result : String -1 文件不存在,0 删除成功,2 删除失败