Dubbo官方提供了一个 admin 的可视化后台，可能是整合进Apache，github的Dubbo原项目已经没有dubbo-admin这个项目已经没有了，独立出来做成了一个vue+springboot项目了。

地址：[https://github.com/apache/dubbo-admin](https://github.com/apache/dubbo-admin)

dubbo管理控制台开源部分主要包含： 提供者  路由规则  动态配置  访问控制  权重调节  负载均衡  负责人，等管理功能

## 1、启动后台项目

后台项目是dubbo-admin-server，是一个spring-boot项目，直接启动`DubboAdminApplication`方法即可。

需要修改zk的地址，配置文件是`application.properties`，我这里是选择zk作为注册中心：

```properties
admin.registry.address=zookeeper://127.0.0.1:2181
admin.config-center=zookeeper://127.0.0.1:2181
admin.metadata-report.address=zookeeper://127.0.0.1:2181
```

其他注册中心参考官方文档。

打包：

```shell
mvn clean package -Dmaven.test.skip=true
```

打包成功会在target目录生成一个jar，直接 java -jar 即可运行。

## 2、启动前端项目

前端项目是dubbo-admin-ui ，用vue写的，需要node环境，需要安装npm。

npm可以设置一下taobao镜像源：

```shell
npm config set registry https://registry.npm.taobao.org
```

打开 dubbo-admin-ui 目录的 `vue.config.js`，自行修改后台项目的端口和前端项目的端口：

```js
module.exports = {
  outputDir: "target/dist",
  lintOnSave: "warning",
  devServer: {
  //前端项目启动端口
    port: 8082,
    historyApiFallback: {
      rewrites: [
        {from: /.*/, to: path.posix.join('/', 'index.html')},
      ],
    },
    publicPath: '/',
    proxy: {
      '/': {
      //后台接口
        target: 'http://localhost:8077/',
        changeOrigin: true,
        pathRewrite: {
          '^/': '/'
        }
      }
    }
  },
```

上诉设置完毕，执行以下

步骤一：

```shell
npm install
```

步骤二：

```shell
npm run dev
```

如下即启动成功：

```shell
You may use special comments to disable some warnings.
Use // eslint-disable-next-line to ignore the next line.
Use /* eslint-disable */ to ignore all warnings in a file.

  App running at:
  - Local:   http://localhost:8082/
  - Network: http://172.16.44.140:8082/

```



访问[http://localhost:8082](http://localhost:8082) 即可。

账户/密码：root / root



期间遇到了两个错误：

### 1、错误一

```shell
error dubbo-admin-ui@0.3.0-SNAPSHOT serve: `vue-cli-service serve`
```

dubbo-admin-ui 项目是vue3.0，需要安装 vue-cli-service 



### 2、错误二

```shell
Error: Cannot find module 'webpack-merge'
```



以上两个错误猜测是因为 `npm install` 的时候没有安装成功。

删除 dubbo-admin-ui 项目的 node_modules 目录，然后重新执行 `npm install` 即可。