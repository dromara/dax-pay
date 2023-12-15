package cn.bootx.platform.daxpay;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
/**
 * 网关端
 * @author xxm
 * @since 2023/12/15
 */
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
public class DaxpaySingleGatewayApp {
}
