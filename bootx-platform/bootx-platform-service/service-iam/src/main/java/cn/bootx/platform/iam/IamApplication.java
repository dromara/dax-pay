package cn.bootx.platform.iam;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * 身份识别与访问管理
 *
 * @author xxm
 * @since 2021/7/29
 */
@ComponentScan
@MapperScan(annotationClass = Mapper.class)
public class IamApplication {

}
