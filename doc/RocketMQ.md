# RocketMQ

## 一、介绍

> [RocketMQ](https://rocketmq.apache.org/) 是一款纯 Java、分布式、队列模型的开源消息中间件，是一款低延迟、高可靠、可伸缩、易于使用的消息中间件。

具有以下特性：
1.支持发布/订阅（Pub/Sub）和点对点（P2P）消息模型
2.在一个队列中可靠的先进先出（[FIFO](https://so.csdn.net/so/search?q=FIFO&spm=1001.2101.3001.7020)）和严格的顺序传递
3.支持拉（pull）和推（push）两种消息模式
4.单一队列百万消息的堆积能力
5.支持多种消息协议，如 JMS、MQTT 等
6.分布式高可用的部署架构,满足至少一次消息传递语义
7.提供 docker 镜像用于隔离测试和云集群部署
8.提供配置、指标和监控等功能丰富的 Dashboard



## 二、准备工作

在搭建之前，需要做一些准备工作，这里我们需要使用 docker 搭建服务，所以需要提前安装 docker。

此外，由于 rocketmq 需要部署 broker 与 nameserver ，考虑到分开部署比较麻烦，这里将会使用 docker-compose。

*rocketmq 架构图如下:*

[![rocketmq](https://tva1.sinaimg.cn/large/006y8mN6gy1h6uz0bfsncj30r30c2aat.jpg)](https://img2018.cnblogs.com/blog/1419561/201909/1419561-20190904094823374-183044215.png)

另外，还需要搭建一个 web 可视化控制台，可以监控 mq 服务状态，以及消息消费情况，这里使用 rocketmq-console，同样该程序也将使用 docker 安装。



## 三、部署过程

首先我们需要 rocketmq docker 镜像，可以选择自己制作，直接拉取 [git@github.com:apache/rocketmq-docker.git](https://www.cnblogs.com/goodAndyxublog/p/git@github.com:apache/rocketmq-docker.git) ，然后再制作镜像。

也可以直接使用 docker hub 上官方制作的镜像，镜像名： `rocketmqinc/rocketmq`

接着创建三个目录，分别用来放配置、日志、存储

```bash
mkdir -p /opt/rocketmq/{conf,logs,store}
```

在 conf 目录 创建 mq 配置文件 `broker.conf`，内容如下

```bash
brokerClusterName = DefaultCluster  
brokerName = broker-a  
brokerId = 0  
deleteWhen = 04  
fileReservedTime = 48  
brokerRole = ASYNC_MASTER  
flushDiskType = ASYNC_FLUSH  
# 如果是本地程序调用云主机 mq，这个需要设置成 云主机 IP
brokerIP1=10.10.101.80 
```

最后在 rocketmq 目录创建 docker-compose.yml 文件，如下

```yaml
version: '2'
services:
  namesrv:
    image: rocketmqinc/rocketmq
    container_name: rmqnamesrv
    ports:
      - 9876:9876
    volumes:
      - /opt/rocketmq/logs:/home/rocketmq/logs
      - /opt/rocketmq/store:/home/rocketmq/store
    command: sh mqnamesrv
  broker:
    image: rocketmqinc/rocketmq
    container_name: rmqbroker
    ports:
      - 10909:10909
      - 10911:10911
      - 10912:10912
    volumes:
      - /opt/rocketmq/logs:/home/rocketmq/logs
      - /opt/rocketmq/store:/home/rocketmq/store
      - /opt/rocketmq/conf/broker.conf:/opt/rocketmq-4.4.0/conf/broker.conf
    command: sh mqbroker -n namesrv:9876 -c ../conf/broker.conf
    depends_on:
      - namesrv
    environment:
      - JAVA_HOME=/usr/lib/jvm/jre
  console:
    image: styletang/rocketmq-console-ng
    container_name: rocketmq-console-ng
    ports:
      - 8087:8080
    depends_on:
      - namesrv
    environment:
      - JAVA_OPTS= -Dlogging.level.root=info   -Drocketmq.namesrv.addr=rmqnamesrv:9876 
      - Dcom.rocketmq.sendMessageWithVIPChannel=false
```

**注意点**

> 这里需要注意 rocketmq broker 与 rokcetmq-console 都需要与 rokcetmq nameserver 连接，需要知道 nameserver ip。使用 docker-compose 之后，上面三个 docker 容器将会一起编排，可以直接使用容器名代替容器 ip，如这里 nameserver 容器名 rmqnamesrv。

配置完成之后，运行 `docker-compose up` 启动三个容器，启动成功后，访问 你的服务IP地址:8087，查看 mq 外部控制台，如果可以看到以下信息，rocketmq 服务启动成功。

![image-20221006023418706](https://tva1.sinaimg.cn/large/006y8mN6gy1h6uza664nuj31ft0u0770.jpg)



## 附：安装 docker-compose

快速安装

```bash
curl -L https://get.daocloud.io/docker/compose/releases/download/v2.4.1/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
```

将可执行权限应用于二进制文件

```bash
sudo chmod +x /usr/local/bin/docker-compose
```

创建软链

```bash
sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
```

测试是否安装成功

```bash
docker-compose version
```

