package cn.bootx.platform.baseapi.handler.dynamicsource;

import cn.bootx.platform.baseapi.core.dynamicsource.service.DynamicDataSourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 初始化数据源函数
 * @author xxm
 * @since 2023/5/17
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicDataSourceLoadHandler {
    private final DynamicDataSourceService dynamicDataSourceService;

    /**
     * 启动时初始化加载多数据源
     */
    @Bean
    public void dynamicDataSourceService$initLoad(){
        dynamicDataSourceService.initLoad();
    }

}
