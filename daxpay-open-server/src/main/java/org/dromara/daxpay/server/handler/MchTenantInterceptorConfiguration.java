package org.dromara.daxpay.server.handler;

import cn.bootx.platform.common.mybatisplus.interceptor.MpInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 商户数据隔离插件
 * @author xxm
 * @since 2024/6/25
 */
@Configuration
@RequiredArgsConstructor
public class MchTenantInterceptorConfiguration {

    private final CustomTenantLineHandler customTenantHandler;

    /**
     * 租户拦截器
     */
    @Bean
    public MpInterceptor MchTenantInterceptor() {
        TenantLineInnerInterceptor tenantInterceptor = new TenantLineInnerInterceptor();
        tenantInterceptor.setTenantLineHandler(customTenantHandler);
        return new MpInterceptor(tenantInterceptor, -999);
    }
}
