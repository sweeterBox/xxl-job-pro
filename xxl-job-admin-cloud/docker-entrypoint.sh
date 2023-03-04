#!/bin/sh
####
# 启动脚本
#  @author sweeter
####

# 检查配置文件是否存在，不存在则拷贝一份新的
cp -ru ./init-conf/* ./conf/

java ${JAVA_OPTS} -jar  /app/xxl-job-admin-cloud.jar ${0} ${@}
