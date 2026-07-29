package cn.daxpay.open.payment.douyin.facade;

import cn.daxpay.open.payment.douyin.enums.DyAppScopeEnum;

/// # 抖音应用只读视图
///
/// 供通道组装器/OAuth 使用；appSecret 为解密后明文，仅服务内传递，禁止写入 Result/日志明文。
///
/// @param scope        档位
/// @param id           主数据主键
/// @param douyinAppId  抖音 AppId
/// @param appType      应用类型
/// @param appSecret    解密后密钥
/// @param appName      应用名称(可选展示)
///
public record DyAppView(
        DyAppScopeEnum scope,
        Long id,
        String douyinAppId,
        String appType,
        String appSecret,
        String appName
) {
}
