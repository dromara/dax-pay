package cn.bootx.platform.notice;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * 通知中心
 *
 * @author xxm
 * @since 2021/8/5
 */
@ComponentScan
@MapperScan(annotationClass = Mapper.class)
public class NoticeApplication {

}
