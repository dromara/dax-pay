package cn.daxpay.open.device.virtual.support;

import cn.daxpay.open.device.virtual.enums.VirtualTestMode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.hutool.core.util.StrUtil;

/// # 试播/试打样例规整(运营端与商户端共用)
///
/// 三个收敛点:
/// - **金额上下限**: 试播试打统一 0.01~999,999.99 元(与运营端输入习惯一致; 真实支付播报能力上限另由
///   APP 词元合成器决定, 不受此约束);
/// - **收款方式合法性与设备侧归一化**: 前端传 [PayProviderEnum] 的 code, 下发前归一化为 APP 词元表的
///   五键(wechat/alipay/douyin/union/other) —— 历史缺口: APP 的渠道归一化未收录 `union_pay`,
///   直接透传会让银联退化为"收款到账"; 此处先归一化(双保险);
/// - **模式缺省**: 试打缺省播报+打印, 试播缺省纯语音。
///
/// 移植自 plus `daxpay-device-core`(快照 773134af), 异常文案 messageKey 化。
public final class VirtualTestSampleSupport {

    /// 试播/试打金额上限(分): 999,999.99 元
    public static final long MAX_AMOUNT_FEN = 99_999_999L;

    /// 试播/试打金额下限(分)
    public static final long MIN_AMOUNT_FEN = 1L;

    /// 缺省样例金额(分): 12.50 元(与历史试打口径一致)
    public static final long DEFAULT_AMOUNT_FEN = 1250L;

    /// 缺省收款方式
    public static final String DEFAULT_CHANNEL = PayProviderEnum.WECHAT.getCode();

    /// APP 词元表兜底键("收款到账")
    public static final String DEVICE_CHANNEL_OTHER = "other";

    private VirtualTestSampleSupport() {
    }

    /// 金额规整(空=缺省 1250 分; 越界抛业务异常)
    public static long resolveAmountFen(Long amountFen) {
        if (amountFen == null) {
            return DEFAULT_AMOUNT_FEN;
        }
        if (amountFen < MIN_AMOUNT_FEN || amountFen > MAX_AMOUNT_FEN) {
            // 设备: 试播试打金额超出允许范围
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.testAmountRange");
        }
        return amountFen;
    }

    /// 收款方式校验(空=缺省 wechat; 非枚举 code 拒绝)
    public static String resolveChannel(String channel) {
        if (StrUtil.isBlank(channel)) {
            return DEFAULT_CHANNEL;
        }
        PayProviderEnum provider = PayProviderEnum.findByCode(channel.trim().toLowerCase());
        if (provider == null) {
            // 设备: 收款方式不在支持范围
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.testChannelInvalid");
        }
        return provider.getCode();
    }

    /// 收款方式归一化为 APP 播报词元键(五键闭集, 未知归 other)
    ///
    /// `union_pay` → `union`(银联词元), 与 [cn.daxpay.open.device.comm.AudioBroadcastMessage]
    /// 的支付链路归一化同口径。空值归 `other`(调用方一般已用 [resolveChannel] 兜底为 wechat)。
    public static String toDeviceChannel(String channel) {
        String code = StrUtil.nullToEmpty(channel).trim().toLowerCase();
        return switch (code) {
            case "wechat", "wx" -> "wechat";
            case "alipay", "ali" -> "alipay";
            case "douyin", "tt" -> "douyin";
            case "union", "union_pay" -> "union";
            default -> DEVICE_CHANNEL_OTHER;
        };
    }

    /// 试打模式规整(空=PRINT_BOTH; 试播接口不接受打印模式, PREVIEW_ONLY 不支持)
    public static VirtualTestMode resolvePrintMode(VirtualTestMode mode) {
        if (mode == null) {
            return VirtualTestMode.PRINT_BOTH;
        }
        if (mode == VirtualTestMode.ANNOUNCE) {
            // 设备: 试打不支持纯播报模式
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.printModeInvalid");
        }
        return mode;
    }

    /// 试播模式校验(仅 ANNOUNCE; 其余视为误用)
    public static void requireAnnounceMode(VirtualTestMode mode) {
        if (mode != null && mode != VirtualTestMode.ANNOUNCE) {
            // 设备: 试播仅支持语音模式
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.announceModeOnly");
        }
    }
}
