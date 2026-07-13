package cn.daxpay.open.payment.unipay.aop;

import java.lang.annotation.*;

/// # 开放支付验签标识
///
/// 入参出参要求:
///
/// 1. 方法至少有一个参数，并且需要签名的参数放在第一位, 并为 MerchantPaymentCommonParam 的子类
/// 2. 返回对象必须为 DaxResult 格式
///
/// 注解实现的功能(按先后顺序):
/// 1. 参数校验
/// 2. 商户身份初始化（mchNo 入线程上下文）
/// 3. 参数签名校验
/// 4. 响应写入 resTime 并签名
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface PaymentVerify {
}
