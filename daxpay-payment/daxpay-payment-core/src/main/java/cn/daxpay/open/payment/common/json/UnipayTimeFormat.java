package cn.daxpay.open.payment.common.json;

/// # 对外开放接口(unipay)时间格式契约
///
/// 对外开放契约(/unipay/**)的请求、响应、异步通知统一使用**北京时间、秒精度、不带时区后缀**的字面量
/// (形如 `2026-09-11 18:46:45`), 与已发布接口文档、四语言 SDK 测试向量、商业增强版一致;
/// 微信支付 / 支付宝同惯例。
///
/// **内部 Web 接口(运营端/商户端/H5/小程序)不使用本格式**, 仍由全局 [cn.daxpay.open.platform.common.json.jdk.Java8TimeFormatModule]
/// 输出 UTC 秒精度 ISO 8601(形如 `2026-09-11T10:46:45Z`)—— 前端按 `Z`/`+`/`T` 判定后做用户时区渲染,
/// 改动会直接破坏时间展示。
///
/// 用法(注解属性必须是编译期常量, 故集中在此声明):
/// ```java
/// @JsonFormat(pattern = UnipayTimeFormat.PATTERN, timezone = UnipayTimeFormat.ZONE)
/// private OffsetDateTime resTime;
/// ```
public final class UnipayTimeFormat {

    /// 时间格式: 年月日时分秒, 不带时区后缀
    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    /// 基准时区: 东八区(北京时间)
    public static final String ZONE = "GMT+8";

    private UnipayTimeFormat() {
    }
}
