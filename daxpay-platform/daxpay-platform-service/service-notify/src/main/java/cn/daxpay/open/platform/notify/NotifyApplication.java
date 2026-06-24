package cn.daxpay.open.platform.notify;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/// 通知模块(公告 + 个人消息)
@AutoConfiguration
@ComponentScan
@EnableScheduling
@MapperScan(annotationClass = Mapper.class)
public class NotifyApplication {

}
