package cn.bootx.platform.starter.code.gen;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 代码生成
 *
 * @author xxm
 * @since 2022/1/27
 */
@ComponentScan
@MapperScan(annotationClass = Mapper.class)
@AutoConfiguration
public class CodeGenAutoConfiguration {

}
