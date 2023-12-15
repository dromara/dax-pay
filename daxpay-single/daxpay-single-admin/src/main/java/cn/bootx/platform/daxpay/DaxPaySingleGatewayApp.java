package cn.bootx.platform.daxpay;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * 管理端
 * @author xxm
 * @since 2023/12/14
 */
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
public class DaxPaySingleGatewayApp {
}
