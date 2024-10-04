package cn.bootx.platform.common.mybatisplus.configuration;

import cn.bootx.platform.common.mybatisplus.handler.SnowflakeIdentifierGenerator;
import cn.bootx.platform.common.mybatisplus.interceptor.MpInterceptor;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.parser.JsqlParserGlobal;
import com.baomidou.mybatisplus.extension.parser.cache.JdkSerialCaffeineJsqlParseCache;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * mybatis自动配置
 *
 * @author xxm
 * @since 2021/7/27
 */
@Configuration
@RequiredArgsConstructor
public class MpConfiguration {

    /*
     * MyBatis-Plus 本地缓存 SQL 解析，这对于分页、租户等插件特别有效。
     */
    static {
        JsqlParserGlobal.setJsqlParseCache(new JdkSerialCaffeineJsqlParseCache(
                (cache) -> cache.maximumSize(1024)
                        .expireAfterWrite(5, TimeUnit.SECONDS))
        );
    }

    /**
     * 使用多个功能需要注意顺序关系,建议使用如下顺序 多租户,动态表名 分页,乐观锁 sql性能规范,防止全表更新与删除
     * 总结: 对sql进行单次改造的优先放入,不对sql进行改造的最后放入
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(List<MpInterceptor> interceptors) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptors.stream()
            .sorted(Comparator.comparing(MpInterceptor::getSortNo))
            .map(MpInterceptor::getInnerInterceptor)
            .forEach(interceptor::addInnerInterceptor);

        return interceptor;
    }

    /**
     * 数据库主键生成
     */
    @Bean
    public IdentifierGenerator idGenerator() {
        return new SnowflakeIdentifierGenerator();
    }

}
