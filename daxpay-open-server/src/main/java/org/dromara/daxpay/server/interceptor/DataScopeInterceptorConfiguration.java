package org.dromara.daxpay.server.interceptor;

import cn.bootx.platform.common.mybatisplus.interceptor.MpInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author xxm
 * @since 2025/2/2
 */
@Configuration
public class DataScopeInterceptorConfiguration {

    /**
     * 数据范围权限插件
     */
    @Bean
    public MpInterceptor dataPermInterceptorMp(MchDataScopeHandler dataScopeInterceptor) {
        return new MpInterceptor(new DataPermissionInterceptor(dataScopeInterceptor),-99);
    }
}
