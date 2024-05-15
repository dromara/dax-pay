package cn.bootx.platform.starter.data.perm.configuration;

import cn.bootx.platform.common.mybatisplus.interceptor.MpInterceptor;
import cn.bootx.platform.starter.data.perm.scope.DataScopeInterceptor;
import cn.bootx.platform.starter.data.perm.select.SelectFieldPermInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 数据权限配置
 *
 * @author xxm
 * @since 2021/12/21
 */
@Configuration
@RequiredArgsConstructor
public class DatePermConfiguration {

    /**
     * 数据范围权限插件
     */
    @Bean
    @ConditionalOnProperty(prefix = "bootx.starter.data-perm", value = "enableDataPerm", havingValue = "true",
            matchIfMissing = true)
    public MpInterceptor dataPermInterceptorMp(DataScopeInterceptor dataScopeInterceptor) {
        return new MpInterceptor(dataScopeInterceptor);
    }

    /**
     * 查询字段权限插件
     */
    @Bean
    @ConditionalOnProperty(prefix = "bootx.starter.data-perm", value = "enableSelectFieldPerm", havingValue = "true",
            matchIfMissing = true)
    public MpInterceptor selectFieldPermInterceptorMp(SelectFieldPermInterceptor selectFieldPermInterceptor) {
        return new MpInterceptor(selectFieldPermInterceptor);
    }

}
