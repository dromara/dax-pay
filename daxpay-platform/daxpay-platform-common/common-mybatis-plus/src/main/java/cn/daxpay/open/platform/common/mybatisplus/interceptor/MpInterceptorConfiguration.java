package cn.daxpay.open.platform.common.mybatisplus.interceptor;

import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/// # 插件容器
///
@Configuration
public class MpInterceptorConfiguration {

    /// 分页(无参构造：由 MyBatis-Plus 从 DataSource 的 JDBC URL 自动推断数据库方言，换库零改动)
    @Bean
    public MpInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setOptimizeJoin(false);
        return new MpInterceptor(paginationInnerInterceptor, 1);
    }

    /// 乐观锁
    @Bean
    public MpInterceptor optimisticLockerInnerInterceptor() {
        return new MpInterceptor(new OptimisticLockerInnerInterceptor(), 1);
    }

}
