FROM  eclipse-temurin:8-jre-focal

MAINTAINER sweeter

LABEL "version"="v1.0" \
      "author"="sweeter"

RUN apt-get -y update && DEBIAN_FRONTEND="noninteractive" apt -y install tzdata
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

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
