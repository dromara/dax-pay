package cn.bootx.platform.common.spring.configuration;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

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

    /** TTL包装后的执行器 */
    private final Executor asyncExecutor;

    @Override
    public Executor getAsyncExecutor() {
        return asyncExecutor;
    }

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
            log.error("异步方法中发生异常，方法：{}，参数：{}，异常：{}", method.getName(), JSONUtil.toJsonStr(objects),
                    throwable.getMessage());
            log.error("详细异常信息", throwable);
        }

    }

}
