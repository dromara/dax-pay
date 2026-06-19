package cn.daxpay.open.platform.core.enums.unipay;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import cn.daxpay.open.platform.core.exception.config.ConfigNotExistException;
import java.util.Arrays;
import java.util.Objects;

/// # 通道认证类型
///
/// 字典: channel_auth_type
@Getter
@RequiredArgsConstructor
public enum ChannelAuthTypeEnum implements I18nSupport {

    /// 微信
    WECHAT("wechat"),
    /// 支付宝
    ALIPAY("alipay"),
    /// 银联
    UNION_PAY("union_pay");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.channel_auth_type";
    }
    public static ChannelAuthTypeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.code, code))
                .findFirst()
                // 通用: 认证类型不存在: {0}
                .orElseThrow(() -> new ConfigNotExistException("error.common.channelAuthTypeNotExist", code));
    }

}
