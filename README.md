# XXL-JOB Pro
>基于XXL-JOB项目二次开发,由于变动较大不准备申请合并到XXL-JOB仓库，后续将独立维护
## 简介
  基于学习[XXL-JOB](https://github.com/xuxueli/xxl-job)的目的,作者尝试编码了XXL-JOB Pro，主要目的是让XXL-JOB更好的和Spring Cloud架构融合。
## 原XXL-JOB文档
- [XXL-JOB](https://github.com/xuxueli/xxl-job/blob/master/README.md)  
- [XXL-JOB官方文档](https://www.xuxueli.com/xxl-job/#%E3%80%8A%E5%88%86%E5%B8%83%E5%BC%8F%E4%BB%BB%E5%8A%A1%E8%B0%83%E5%BA%A6%E5%B9%B3%E5%8F%B0XXL-JOB%E3%80%8B)
## 已修改的功能点
- 后端mybatis切换为spring-data-jpa
- 客户端实例服务（执行器）自动发现,支持spring cloud自动发现client执行服务
- 客户端端口与Web端口融合,支持只需一个端口对外开放
- 任务自动发现
- UI界面美化
- 前端与后端代码分离，便于重写前端UI
- 添加swagger接口文档
## 待修改功能点
- 增加任务监控回调api hook，在配置文件中增加回调通知配置地址（支持数组）参照spring-boot-admin 实现邮件、钉钉及飞书的通知消息
- 增加监控参数包括内存、网络、磁盘等
- 用户登录支持oauth2
- tomcat->netty springMvc ->webflux
- 优化执行器的执行日志，最好能在admin中实时显示  
- 编写客户端执行器starter，便于springboot项目集成
- 前端使用vue重新构建编写

## 原则及注意事项
- 非必要不引入新的中间件，越简单越好

## 接口文档
- URL：http://ip:port/xxl-job-admin/doc.html

## Supported database
- mysql v5.7
- mariadb v10.6.x
- h2
- TiDB
- oceanBase
- oracle
- Postgresql



## Docker 

```shell script
docker pull sweeter/xxl-job-pro-admin:v2.4
```

## 使用
### spring-cloud 微服务架构项目
- spring-cloud-alibaba项目中引入如下maven依赖
```xml
<dependency>
    <groupId>com.xuxueli</groupId>
    <artifactId>spring-boot-xxl-job-starter-nacos-client</artifactId>
    <version>2.4.0-SNAPSHOT</version>
</dependency>
```
- 支持的nacos版本 v2.0.3
