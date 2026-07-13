package cn.daxpay.open.payment.common.tenant;

import cn.daxpay.open.platform.common.mybatisplus.interceptor.MpInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/// # mch_no 行级隔离插件注册
///
/// 注册 MyBatis-Plus [TenantLineInnerInterceptor]，处理逻辑见 [MchNoTenantLineHandler]。
/// 优先级 -999，尽量靠前拼条件。
@Configuration
@RequiredArgsConstructor
public class MchNoTenantInterceptorConfiguration {

    private final MchNoTenantLineHandler mchNoTenantLineHandler;

    /// mch_no 租户行拦截器
    @Bean
    public MpInterceptor mchNoTenantInterceptor() {
        var tenantInterceptor = new TenantLineInnerInterceptor();
        tenantInterceptor.setTenantLineHandler(mchNoTenantLineHandler);
        return new MpInterceptor(tenantInterceptor, -999);
    }
}
