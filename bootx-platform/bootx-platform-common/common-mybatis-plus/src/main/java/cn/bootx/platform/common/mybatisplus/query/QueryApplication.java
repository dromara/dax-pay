package cn.bootx.platform.common.mybatisplus.query;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * 高级查询器
 *
 * @author xxm
 * @since 2021/11/17
 */
@ComponentScan
@MapperScan(annotationClass = Mapper.class)
public class QueryApplication {

}
