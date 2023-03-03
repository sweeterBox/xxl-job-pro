#
#启动脚本
#

# 检查配置文件是否存在，不存在则拷贝一份新的
cp -r -n ./init-conf/ ./conf/

sh -c java ${JAVA_OPTS} -jar  xxl-job-admin-cloud.jar ${0} ${@}
