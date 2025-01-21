package cn.bootx.platform.common.spring.configuration;

import cn.bootx.platform.core.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.reflect.Method;

/**
 * 异步执行配置
 *
 * @author xxm
 * @since 2021/6/11
 */
@Slf4j
@EnableAsync
@Configuration
@RequiredArgsConstructor
public class AsyncExecutorConfiguration implements AsyncConfigurer {

    /**
     * 获取异步未捕获异常处理程序
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SpringAsyncExceptionHandler();
    }

    /**
     * Spring 异步异常处理器
     */
    private static class SpringAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @SuppressWarnings("NullableProblems")
        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {

            log.error("异步方法中发生异常，方法：{}，参数：{}，异常：{}", method.getName(), JsonUtil.toJsonStr(objects),
                    throwable.getMessage());
            log.error("详细异常信息", throwable);
        }

    }

}
