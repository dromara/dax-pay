package cn.daxpay.open.payment.trade.alloc.enums;

import cn.daxpay.open.platform.core.exception.system.StatusNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 分账订单状态
///
/// alloc_order 表的分账状态, 标识单笔分账交易的生命周期。
/// 单次分账语义: 发起即完结, 无"分账处理完成待完结"中间态。
/// - processing: 已受理, 等待通道异步推进(支付宝/微信靠查询, 抖音靠回调)
/// - success: 全部接收方分账成功
/// - partial: 部分接收方成功, 部分失败(单次分账终态, 不可再追加)
/// - fail: 全部接收方分账失败
/// 字典: alloc_order_status
@Getter
@RequiredArgsConstructor
public enum AllocOrderStatusEnum implements I18nSupport {

    /// 分账中(已受理, 等待通道异步结果)
    PROCESSING("processing"),
    /// 分账成功(全部接收方成功)
    SUCCESS("success"),
    /// 部分成功(部分接收方成功, 部分失败)
    PARTIAL("partial"),
    /// 分账失败(全部接收方失败)
    FAIL("fail"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.alloc_order_status";
    }

    /// 根据编码获取枚举
    public static AllocOrderStatusEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 通用: 该分账订单状态不存在: {0}
                .orElseThrow(() -> new StatusNotExistException("error.common.allocOrderStatusNotExist", code));
    }

    /// 是否终态
    public boolean isTerminal() {
        return this == SUCCESS || this == PARTIAL || this == FAIL;
    }
}
