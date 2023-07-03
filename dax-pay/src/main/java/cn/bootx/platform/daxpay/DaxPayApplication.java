package cn.bootx.platform.daxpay;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * DaxPay 支付开发平台
 *
 * @author xxm
 * @since 2023/4/20
 */
@Slf4j
@ConfigurationPropertiesScan
@MapperScan(annotationClass = Mapper.class)
public class DaxPayApplication {

}
