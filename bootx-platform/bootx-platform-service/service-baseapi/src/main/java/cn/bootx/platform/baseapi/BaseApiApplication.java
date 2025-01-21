package cn.bootx.platform.baseapi;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * 框架基础功能
 *
 * @author xxm
 * @since 2021/8/4
 */
@ComponentScan
@MapperScan(annotationClass = Mapper.class)
public class BaseApiApplication {

}
