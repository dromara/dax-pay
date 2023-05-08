# java8基础镜像
FROM registry.cn-beijing.aliyuncs.com/xxm1995/java8

# 作者信息
MAINTAINER xxm1995@outlook.com

# 时区设置
ENV TZ=Asia/Shanghai
RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# jvm启动参数, 启动内存256M,最大内存1G,使用G1垃圾回收器
ENV JAVA_OPTS="-Xms256m -Xmx1G -XX:+UseG1GC -Djava.security.egd=file:/dev/./urandom"

# 工作目录
WORKDIR /

# 端口暴露
EXPOSE 9999

# 添加执行jar包
ADD bootx-start/target/bootx-start.jar bootx-start.jar

# 执行启动命令
CMD java $JAVA_OPTS -jar bootx-start.jar
