package cn.daxpay.open.payment.testsupport;

import cn.daxpay.open.platform.common.json.jdk.Java8TimeFormatModule;
import cn.daxpay.open.platform.common.json.jdk.JavaLongTypeModule;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import lombok.experimental.UtilityClass;
import tools.jackson.databind.json.JsonMapper;

import java.util.Objects;

/// # 单元测试用 Jackson 初始化
///
/// 纯单元测试无 Spring 容器, [JacksonUtil] 的静态 mapper 需手工注入。surefire 在同一个 JVM 内复用,
/// 而 [JacksonUtil#setObjectMapper] 只允许设置一次(重复设置抛异常), 故统一经此入口初始化:
/// 装配与生产一致的模块(长整型转字符串 + 平台时间格式), 已初始化时直接跳过。
///
/// 注意: 必须装配 [Java8TimeFormatModule], 否则时间字段按 Jackson 默认格式输出, 契约断言会失真。
@UtilityClass
public class JacksonTestSupport {

    /// 初始化 JacksonUtil(幂等)
    public static void init() {
        if (Objects.nonNull(JacksonUtil.getObjectMapper())) {
            return;
        }
        JsonMapper mapper = JsonMapper.builder()
                .addModule(new JavaLongTypeModule())
                .addModule(new Java8TimeFormatModule())
                .build();
        JacksonUtil.setObjectMapper(mapper);
        JacksonUtil.setIgnoreNullObjectMapper(mapper);
    }
}
