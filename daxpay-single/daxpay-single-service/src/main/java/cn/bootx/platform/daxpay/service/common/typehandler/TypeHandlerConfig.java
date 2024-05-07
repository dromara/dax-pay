package cn.bootx.platform.daxpay.service.common.typehandler;

import cn.bootx.platform.starter.data.perm.configuration.DataPermProperties;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 *
 * @author xxm
 * @since 2024/5/7
 */
@Configuration
public class TypeHandlerConfig {

    @PostConstruct
    public void init(){
        DataPermProperties permProperties = SpringUtil.getBean(DataPermProperties.class);
        DecryptTypeHandler.setDataPermProperties(permProperties);
    }
}
