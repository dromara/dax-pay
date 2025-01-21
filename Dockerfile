# java21基础镜像
FROM m.daocloud.io/docker.io/eclipse-temurin:21.0.4_7-jdk-alpine

# 作者信息
MAINTAINER daxpay@daxpay.cn

# 时区设置
ENV TZ=Asia/Shanghai
RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 启动命令
ENV JAVA_COMM="-Djava.security.egd=file:/dev/./urandom -Dfile.encoding=UTF-8"
# 启动选项配置
ENV JAVA_OPTS=""
# Agent配置
ENV JAVA_AGENT=""
# 参数配置
ENV JAVA_ARGS=""
# 工作目录
WORKDIR /

# 端口暴露
EXPOSE 9999

# 添加执行jar包
ADD daxpay-single-server/target/daxpay-server.jar daxpay-server.jar

# 执行启动命令
CMD java $JAVA_OPTS -jar daxpay-server.jar
