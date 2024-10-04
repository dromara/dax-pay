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
 * 反序列化: 时间格式支持秒 {@link DatePattern#NORM_DATETIME_PATTERN} 或毫秒 {@link DatePattern#NORM_DATETIME_MS_PATTERN}
 * 序列化: 时间格式默认序列化到秒级 {@link DatePattern#NORM_DATETIME_PATTERN}, 毫秒级需要手动指定
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
