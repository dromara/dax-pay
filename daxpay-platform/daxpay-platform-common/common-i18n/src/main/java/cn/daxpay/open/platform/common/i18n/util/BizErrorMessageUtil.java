package cn.daxpay.open.platform.common.i18n.util;

import cn.daxpay.open.platform.core.exception.BizException;
import lombok.experimental.UtilityClass;

import java.util.Locale;
import java.util.Objects;

/// # 业务异常错误消息解析工具
///
/// 将异常解析为固定中文(Locale.CHINA)的错误文案, 用于交易单/资金单等落库场景,
/// 保证落库文案不随请求语言变化。
@UtilityClass
public class BizErrorMessageUtil {

    /// 解析异常为本地化(固定中文)错误消息
    ///
    /// [BizException] 的 getMessage() 返回 i18n messageKey(未经 I18nUtil 解析), 直接落库会导致单据
    /// errorMsg 存 key 字符串(如 "error.channel.wechat.refundFailed")。本方法按固定中文解析;
    /// 非 BizException 的 getMessage() 已是真实文案, 直接使用。
    public String resolve(Throwable e) {
        if (e instanceof BizException biz) {
            String key = biz.resolveMessageKey();
            if (Objects.nonNull(key)) {
                return I18nUtil.get(key, Locale.CHINA, biz.getArgs());
            }
        }
        return e.getMessage();
    }
}
