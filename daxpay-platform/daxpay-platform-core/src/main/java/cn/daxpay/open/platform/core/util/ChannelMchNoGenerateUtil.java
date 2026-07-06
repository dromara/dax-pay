package cn.daxpay.open.platform.core.util;

import cn.hutool.core.util.IdUtil;

/// # 通道商户号生成工具类
///
/// 格式: {6字母通道前缀}{雪花ID}, 例如 LAKALA1872635419283456
/// 前缀仅供排障肉眼辨识通道归属, 不参与路由/不暴露给第三方通道。
///
/// 各通道前缀统一约定为 6 字母英文全名或通用缩写:
/// - ALIPAY (支付宝)
/// - WECHAT (微信)
/// - LAKALA (拉卡拉)
/// - UMSPAY (银联商务)
/// - DOUYIN (抖音)
public final class ChannelMchNoGenerateUtil {

    private ChannelMchNoGenerateUtil() {
    }

    /// 生成通道商户号
    ///
    /// @param channelPrefix 6 字母通道前缀(如 ALIPAY/WECHAT/LAKALA/UMSPAY/DOUYIN)
    /// @return 通道前缀 + 雪花ID(无分隔符, 部分通道不允许特殊符号)
    public static String generate(String channelPrefix) {
        return channelPrefix + IdUtil.getSnowflakeNextId();
    }
}
