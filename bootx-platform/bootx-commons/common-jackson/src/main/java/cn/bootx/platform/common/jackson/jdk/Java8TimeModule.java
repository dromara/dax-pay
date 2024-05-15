package cn.bootx.platform.common.jackson.jdk;

import cn.bootx.platform.common.jackson.deserializer.BootxLocalDateTimeDeserializer;
import cn.bootx.platform.common.jackson.deserializer.BootxLocalTimeDeserializer;
import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * java8 时间序列化 受MySQL限制, 无法存储毫秒值. 只能处理到秒级别
 * 写入时间支持毫秒格式的时间
 * 读取时间时可以处理到秒或毫秒
 * @author xxm
 * @since 2020/4/14 13:33
 */
public class Java8TimeModule extends SimpleModule {

    public Java8TimeModule() {
        // 序列化
        this.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        this.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
        this.addSerializer(LocalTime.class,
                new LocalTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)));
        // 反序列化
        this.addDeserializer(LocalDateTime.class, new BootxLocalDateTimeDeserializer());
        this.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
        this.addDeserializer(LocalTime.class,new BootxLocalTimeDeserializer());
    }

}
