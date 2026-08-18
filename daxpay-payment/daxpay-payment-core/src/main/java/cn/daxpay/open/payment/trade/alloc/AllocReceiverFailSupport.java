package cn.daxpay.open.payment.trade.alloc;

import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.experimental.UtilityClass;
import org.springframework.web.client.ResourceAccessException;

/// # 分账接收方通道调用异常支持
///
/// 供各通道接收方服务绑定/解绑的 catch 块统一使用: 判定"通道结果未知"、
/// 生成落库失败文案、将结果未知异常包装为对用户友好的业务异常。
@UtilityClass
public class AllocReceiverFailSupport {

    /// 绑定"结果未知"用户提示 key(主应用→子应用 HTTP 网络异常)
    public static final String KEY_BIND_UNKNOWN = "error.channel.allocReceiverBindUnknownResult";
    /// 解绑"结果未知"用户提示 key(主应用→子应用 HTTP 网络异常)
    public static final String KEY_UNBIND_UNKNOWN = "error.channel.allocReceiverUnbindUnknownResult";

    /// 落库文案的结果未知标注前缀(诊断数据固定中文, 便于运营检索)
    private static final String UNKNOWN_MARK = "[通道结果未知] ";

    /// 是否为"通道结果未知"异常
    ///
    /// 仅识别主应用到子应用 HTTP 层的 IO/超时类异常([ResourceAccessException]):
    /// 请求可能已送达子应用并执行成功(读超时), 通道侧真实状态需人工核实后决定是否重试。
    /// 子应用内部 SDK 网络失败(如支付宝网关超时)以业务失败形式返回, 与明确拒绝无法区分, 为已知限制。
    public boolean isUnknownOutcome(Throwable e) {
        return e instanceof ResourceAccessException;
    }

    /// 生成落库失败文案
    ///
    /// [BizException] 按当前语言解析为可读文案(裸 messageKey 对运营不可读);
    /// 结果未知异常加标注前缀提示核实后重试; 其余异常取原始信息。
    public String recordMessage(Throwable e) {
        if (isUnknownOutcome(e)) {
            return UNKNOWN_MARK + "网络异常, 通道侧执行结果未知, 请核实后重试; 原始: " + e.getMessage();
        }
        if (e instanceof BizException biz) {
            return I18nUtil.get(biz.resolveMessageKey(), biz.getArgs());
        }
        return e.getMessage();
    }

    /// 结果未知时包装为友好业务异常(网络异常原文对用户无意义), 其余原样上抛
    public RuntimeException toUserException(Throwable e, String unknownKey) {
        if (isUnknownOutcome(e)) {
            return new BizInfoException(DaxPayErrorCode.TRADE_FAIL, unknownKey);
        }
        return (RuntimeException) e;
    }
}
