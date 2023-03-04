FROM openjdk:8-jdk-alpine

MAINTAINER sweeter

LABEL "version"="v1.0" \
      "author"="sweeter"

RUN echo "http://mirrors.aliyun.com/alpine/v3.9/main" > /etc/apk/repositories \
    && echo "http://mirrors.aliyun.com/alpine/v3.9/community" >> /etc/apk/repositories \
    && apk update upgrade \
    && apk add --no-cache curl bash tzdata \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

WORKDIR /app

ADD ./target/xxl-job-admin-cloud/ ./
# 拷贝一份初始配置
COPY ./target/xxl-job-admin-cloud/conf/ ./init-conf/

COPY ./docker-entrypoint.sh /

ENTRYPOINT ["sh","-c","/docker-entrypoint.sh"]
#ENTRYPOINT ["sh","-c","java ${JAVA_OPTS} -jar  xxl-job-admin-cloud.jar ${0} ${@}"]

EXPOSE ${port:-8282}/tcp

HEALTHCHECK --interval=1m --timeout=3s --retries=5 \
  CMD curl -o /dev/null -s -w %{http_code} http://localhost:${port:-8282} || exit 1
