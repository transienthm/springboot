# 五、Docker

## 5.1 简介

**Docker**是一个开源的应用容器引擎；是一个轻量级容器技术；

Docker支持将软件编译成一个镜像；然后在镜像中各种软件做好配置，将镜像发布出去，其他使用者可以直接使用这个镜像；

运行中的这个镜像称为容器，容器启动是非常快速的。

![20180303145450](https://user-images.githubusercontent.com/16509581/40606778-12aab656-6299-11e8-9a09-cd998f29b326.png)
![20180303145531](https://user-images.githubusercontent.com/16509581/40606779-12e55432-6299-11e8-8de3-42fea47df118.png)

## 5.2 核心概念

docker主机(Host)：安装了Docker程序的机器（Docker直接安装在操作系统之上）；

docker客户端(Client)：连接docker主机进行操作；

docker仓库(Registry)：用来保存各种打包好的软件镜像；

docker镜像(Images)：软件打包好的镜像；放在docker仓库中；

docker容器(Container)：镜像启动后的实例称为一个容器；容器是独立运行的一个或一组应用

![20180303165113](https://user-images.githubusercontent.com/16509581/40606780-131d3c3a-6299-11e8-92fa-204b6fc07482.png)