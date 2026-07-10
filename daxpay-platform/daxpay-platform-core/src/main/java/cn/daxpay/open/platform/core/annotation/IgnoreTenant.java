package cn.daxpay.open.platform.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// # 忽略租户（商户）数据隔离
///
/// 在标注的方法/类执行期间关闭 TenantLine 租户行拦截（不拼 mch_no）。
/// 基于线程内引用计数，支持嵌套：外层 + 内层同时标注时，内层返回后外层仍保持忽略，
/// 直到最外层退出才恢复过滤。
///
/// 默认态为开启租户过滤；仅需局部忽略时在对应方法上标注本注解即可。
/// 不跨线程：异步任务需在子线程入口再次标注或手动 MpUtil.ignoreTenant/clearIgnoreTenant。
///
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreTenant {
}
