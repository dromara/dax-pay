package cn.bootx.platform.common.mybatisplus.interceptor;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 插件容器
 *
 * @author xxm
 * @since 2021/12/21
 */
@Configuration
public class MpInterceptorConfiguration {

    /**
     * 分页
     */
    @Bean
    public MpInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.POSTGRE_SQL);
        paginationInnerInterceptor.setOptimizeJoin(false);
        return new MpInterceptor(paginationInnerInterceptor, 1);
    }

    /**
     * 乐观锁
     */
    @Bean
    public MpInterceptor optimisticLockerInnerInterceptor() {
        return new MpInterceptor(new OptimisticLockerInnerInterceptor(), 1);
    }

    /**
     * 防止全表更新与删除
     */
    @Bean
    public MpInterceptor blockAttackInnerInterceptor() {
        return new MpInterceptor(new BlockAttackInnerInterceptor(), 2);
    }

}
