package cn.bootx.platform.starter.audit.log.configuration;

import cn.bootx.platform.common.mybatisplus.interceptor.MpInterceptor;
import cn.bootx.platform.common.mybatisplus.extension.DataChangeRecorderInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xxm
 * @since 2023/1/2
 */
@Configuration
public class DataVersionConfiguration {

    /**
     * 数据变更记录
     */
    @Bean
    public MpInterceptor dataChangeRecorderInnerInterceptor(
            DataChangeRecorderInnerInterceptor dataChangeRecorderInnerInterceptor) {
        return new MpInterceptor(dataChangeRecorderInnerInterceptor, 2);
    }

}
