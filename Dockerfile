# java8基础镜像
FROM registry.cn-beijing.aliyuncs.com/xxm1995/java8

# 作者信息
MAINTAINER xxm1995@outlook.com

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
EXPOSE 9000

# 添加执行jar包
ADD daxpay-single-start/target/daxpay-start.jar dax-start.jar

# 执行启动命令
CMD java $JAVA_OPTS -jar dax-start.jar
