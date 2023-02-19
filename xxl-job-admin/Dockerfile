FROM eclipse-temurin:8-jdk-alpine

MAINTAINER sweeter

LABEL "version"="v1.0" \
      "author"="sweeter"

RUN echo "http://mirrors.aliyun.com/alpine/v3.9/main" > /etc/apk/repositories \
    && echo "http://mirrors.aliyun.com/alpine/v3.9/community" >> /etc/apk/repositories \
    && apk update upgrade \
    && apk add --no-cache curl bash tzdata ttf-dejavu \
    && rm -rf /var/cache/apk/* \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

#指令集的工作目录，可作用于RUN、CMD、ENTRYPOINT、COPY和ADD
WORKDIR /app

ADD ./target/xxl-job-admin-2.4.0-SNAPSHOT.jar ./app.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar  app.jar ${0} ${@}"]

#指定默认端口/协议
EXPOSE ${port:-8282}/tcp

#检查容器健康状况,每分钟检查一次，接口超时时间3秒，连续5次返回1则容器不健康
HEALTHCHECK --interval=1m --timeout=3s --retries=5 \
  CMD curl -o /dev/null -s -w %{http_code} http://localhost:${port:-8282} || exit 1
