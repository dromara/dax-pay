package org.dromara.daxpay.payment.pay.anno;

import java.lang.annotation.*;

/// # 支付校验校验标识 
///
/// 入参出参要求:
///
/// 1. 方法至少有一个参数，并且需要签名的参数放在第一位, 并为PaymentCommonParam的子类 
/// 2. 返回对象必须为 DaxResult 格式
///
/// 注解实现的功能(按先后顺序): 
/// 1. 参数校验
/// 2. 商户和应用信息初始化
/// 3. 终端信息初始化
/// 4. 参数签名校验
/// 5. 参数请求时间校验
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface PaymentVerify {
}
