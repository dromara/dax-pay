package cn.daxpay.open.payment.common.json;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import tools.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/// # unipay 契约金额字段(强制数字输出)
///
/// 平台全局 Jackson 配置将 Long 输出为字符串(防前端 ID 精度丢失, 见 [JavaLongTypeModule]),
/// 而 unipay 契约的金额字段按契约文档以**数字**承载(上限 9999999999 远小于 JS 安全整数 2^53, 无精度风险)。
/// 本注解在字段级覆盖全局 Long 序列化(字段注解优先于模块注册), 仅用于金额类 Long 字段,
/// ID 类 Long 字段(C 端收银台 itemId 等)保持字符串输出, 不加本注解。
///
/// 签名口径不受影响: 待签串由扁平化取值字面量构造, 数字与字符串形态结果一致。
/// 用法(注解属性必须是编译期常量, 与 [UnipayTimeFormat] 同套约定):
/// ```java
/// @UnipayAmount
/// private Long amount;
/// ```
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = UnipayAmountSerializer.class)
public @interface UnipayAmount {
}
