## 1、thrift介绍

thrift官方文档：[https://thrift.apache.org](https://thrift.apache.org)

thrift官方使用demo：[https://github.com/apache/thrift](https://github.com/apache/thrift)

Thrift 是一个跨语言的服务部署框架，最初由 Facebook 于2007年开发，2008 年进入 Apache 开源项目。Thrift 通过 IDL（Interface Definition Language，接口定义语言）来定义RPC（Remote Procedure Call，远程过程调用）的接口和数据类型，然后通过thrift编译器生成不同语言的代码（目前支持C++,Java, Python, PHP, Ruby, Erlang, Perl, Haskell, C#, Cocoa, Smalltalk和OCaml），并由生成的代码负责RPC协议层和传输层的实现。

> protobuff 也是一种 IDL

### Thrift 架构

TProtocol（协议层），定义数据传输格式，例如：

```
TBinaryProtocol：二进制格式；
TCompactProtocol：压缩格式；
TJSONProtocol：JSON格式；
TSimpleJSONProtocol：提供JSON只写协议, 生成的文件很容易通过脚本语言解析；
TDebugProtocol：使用易懂的可读的文本格式，以便于debug
```

> tips:客户端和服务端的协议要一致

TTransport（传输层），定义数据传输方式，可以为TCP/IP传输，内存共享或者文件共享等）被用作运行时库。

```
TSocket：阻塞式socker；
TFramedTransport：以frame为单位进行传输，非阻塞式服务中使用；
TFileTransport：以文件形式进行传输；
TMemoryTransport：将内存用于I/O，java实现时内部实际使用了简单的ByteArrayOutputStream；
TZlibTransport：使用zlib进行压缩， 与其他传输方式联合使用，当前无java实现；
```

TServer（服务模型），定义服务提供者的服务模型：

```
TSimpleServer：简单的单线程服务模型，常用于测试；
TThreadPoolServer：多线程服务模型，使用标准的阻塞式IO；
TNonblockingServer：多线程服务模型，使用非阻塞式IO（需使用TFramedTransport数据传输方式）；
TThreadedSelectorServer：多线程非阻塞模式
```

Thrift实际上是实现了C/S模式，通过代码生成工具将thrift文生成服务器端和客户端代码（可以为不同语言），从而实现服务端和客户端跨语言的支持。用户在Thirft文件中声明自己的服务，这些服务经过编译后会生成相应语言的代码文件，然后客户端调用服务，服务器端提服务便可以了。

一般将服务放到一个.thrift文件中，服务的编写语法与C语言语法基本一致，在.thrift文件中有主要有以下几个内容：变量声明（variable）、数据声明（struct）和服务接口声明（service, 可以继承其他接口）。

 Thrif 提供网络模型：单线程、多线程、事件驱动。从另一个角度划分为：阻塞服务模型、非阻塞服务模型。

- 阻塞服务

​    TSimpleServer

​    TThreadPoolServer

- 非阻塞服务模型

   TNonblockingServer

   THsHaServer

   TThreadedSelectorServer

TNonblockingServer 只能一次处理一个请求。



## 2、使用

### 2.1、安装thrift环境

需要下载thrift工具包。



### 2.2、定义  .thrift IDL 接口文件

需要先定义一个.thrift 作为一个IDL，如：

```
namespace java com.thrift.api
service Hello{
    string helloString(1:string para)
    i32 helloInt(1:i32 para)
    bool helloBoolean(1:bool para)
    void helloVoid()
    string helloNull()
}
```

thrift 支持的类型和结构：

- 基本类型：

```
bool：布尔值，true 或 false，对应 Java 的 boolean
byte：8 位有符号整数，对应 Java 的 byte
i16：16 位有符号整数，对应 Java 的 short
i32：32 位有符号整数，对应 Java 的 int
i64：64 位有符号整数，对应 Java 的 long
double：64 位浮点数，对应 Java 的 double
string：utf-8编码的字符串，对应 Java 的 String
```

- 结构

如果要定义一个结构（相当于Java的Object）：

```
senum PhoneType {
  "HOME",
  "WORK",
  "MOBILE"
  "OTHER"
}

struct Phone {
  1: i32    id,
  2: string number,
  3: PhoneType type
}
struct Person {
  1: i32    id,
  2: string firstName,
  3: string lastName,
  4: string email,
  5: list<Phone> phones
}

struct Course {
  1: i32    id,
  2: string number,
  3: string name,
  4: Person instructor,
  5: string roomNumber,
  6: list<Person> students
}
```

生成Java文件：

```
thrift --gen java Hello.thrift
```



## 3、thrift的性能

| 数据传输格式    | 类型   | 优点                                                         | 缺点                                                         |
| --------------- | ------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| Xml             | 文本   | 1、良好的可读性。 2、序列化的数据包含完整的结构。3、调整不同属性的顺序对序列化/反序列化不影响 | 1、数据传输量大。2、不支持二进制数据类型                     |
| Json            | 文本   | 1、良好的可读性。2、调整不同属性的顺序对序列化/反序列化不影响 | 1、丢弃了类型信息, 比如”price”:100, 对price类型是int/double解析有二义性。2、不支持二进制数据类型 |
| Thrift          | 二进制 | 高效                                                         | 1、不宜读。2、向后兼容有一定的约定限制，采用id递增的方式标识并以optional修饰来添加 |
| Google Protobuf | 二进制 | 高效                                                         | 1、不宜读。2、向后兼容有一定的约定限制                       |



thrift和dubbo的对比：

|                | dubbo                  | thrift |
| -------------- | ---------------------- | ------ |
| 传输协议       | dubbo、hessian、http、 | thrift |
| 序列化         | hessian、json、java    | 二进制 |
| 是否支持跨语言 | 不支持                 | 支持   |
| 使用           |                        |        |
| 负载均衡       | 支持                   | 不支持 |
| 重试           | 支持                   | 不支持 |
| 超时           | 支持                   | 不支持 |
| 与spring集成   | 支持                   | 不支持 |
|                |                        |        |



---

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

shift的服务模型：

- http://www.micmiu.com/soa/rpc/thrift-sample/

shift的使用：

- http://jnb.ociweb.com/jnb/jnbJun2009.html

对Thrift的封装：

- https://vimsky.com/examples/detail/java-class-org.apache.thrift.TMultiplexedProcessor.html
- https://github.com/sofn/trpc

选择 Server的艺术：

- https://www.cnblogs.com/cyfonly/p/6059374.html

- https://www.cnblogs.com/exceptioneye/p/4945073.html