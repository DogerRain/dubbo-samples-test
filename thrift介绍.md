## 1、thrift

thrift是一个代码生成引擎，在不同的语言如Java、C++、Python、go 等等 之间进行无缝地构建服务。

thrift官方文档：[https://thrift.apache.org](https://thrift.apache.org)

thrift使用demo：[https://github.com/apache/thrift](https://github.com/apache/thrift)



## 2、使用

### 2.1、thrift环境

需要先安装thrift环境。

### 2.2、生成目标文件







参考资料：

- https://blog.csdn.net/houjixin/article/details/42778335
- https://www.cnblogs.com/zhaoxd07/p/5387215.html
- https://blog.csdn.net/zw19910924/article/details/78178539

thrift分析：

- https://blog.csdn.net/liubenlong007/article/details/54692467



java调用Python 例子：

- https://www.cnblogs.com/tonglin0325/p/13785407.html



java调用C++例子：

- https://blog.csdn.net/j1231230/article/details/77899160

| 数据传输格式    | 类型   | 优点                                                         | 缺点                                                         |
| --------------- | ------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| Xml             | 文本   | 1、良好的可读性。 2、序列化的数据包含完整的结构。3、调整不同属性的顺序对序列化/反序列化不影响 | 1、数据传输量大。2、不支持二进制数据类型                     |
| Json            | 文本   | 1、良好的可读性。2、调整不同属性的顺序对序列化/反序列化不影响 | 1、丢弃了类型信息, 比如”price”:100, 对price类型是int/double解析有二义性。2、不支持二进制数据类型 |
| Thrift          | 二进制 | 高效                                                         | 1、不宜读。2、向后兼容有一定的约定限制，采用id递增的方式标识并以optional修饰来添加 |
| Google Protobuf | 二进制 | 高效                                                         | 1、不宜读。2、向后兼容有一定的约定限制                       |



![](picture/image-20211124154002605.png)