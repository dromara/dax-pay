package cn.bootx.platform.daxpay.service.core.payment.repair.factory;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.service.core.payment.repair.strategy.refund.*;
import cn.bootx.platform.daxpay.service.func.AbsRefundRepairStrategy;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.bootx.platform.daxpay.code.PayChannelEnum.*;


/**
 * 支付修复策略工厂类
 * @author xxm
 * @since 2023/12/29
 */
@UtilityClass
public class RefundRepairStrategyFactory {
    /**
     * 根据传入的通道创建策略
     * @return 退款修复策略
     */
    public static AbsRefundRepairStrategy create(PayChannelEnum channelEnum) {

        AbsRefundRepairStrategy strategy;
        switch (channelEnum) {
            case ALI:
                strategy = SpringUtil.getBean(AliRefundRepairStrategy.class);
                break;
            case WECHAT:
                strategy = SpringUtil.getBean(WeChatRefundRepairStrategy.class);
                break;
            case UNION_PAY:
                strategy = SpringUtil.getBean(UnionRefundRepairStrategy.class);
                break;
            case CASH:
                strategy = SpringUtil.getBean(CashRefundRepairStrategy.class);
                break;
            case WALLET:
                strategy = SpringUtil.getBean(WalletRefundRepairStrategy.class);
                break;
            case VOUCHER:
                strategy = SpringUtil.getBean(VoucherRefundRepairStrategy.class);
                break;
            default:
                throw new PayUnsupportedMethodException();
        }
        return strategy;
    }

    /**
     * 根据传入的支付类型批量创建策略, 异步支付在后面
     */
    public static List<AbsRefundRepairStrategy> createAsyncLast(List<String> channelCodes) {
        return create(channelCodes, true);
    }

    /**
     * 根据传入的支付类型批量创建策略, 异步支付在前面
     */
    public static List<AbsRefundRepairStrategy> createAsyncFront(List<String> channelCodes) {
        return create(channelCodes, false);
    }

    /**
     * 根据传入的支付类型批量创建策略
     * @param asyncSort 是否异步支付在后面
     * @return 支付策略
     */
    private static List<AbsRefundRepairStrategy> create(List<String> channelCodes, boolean asyncSort) {
        if (CollectionUtil.isEmpty(channelCodes)) {
            return Collections.emptyList();
        }

        // 同步支付
        val syncChannels = channelCodes.stream()
                .filter(code -> !ASYNC_TYPE_CODE.contains(code))
                .map(PayChannelEnum::findByCode)
                .collect(Collectors.toList());

        // 异步支付
        val asyncChannels = channelCodes.stream()
                .filter(ASYNC_TYPE_CODE::contains)
                .map(PayChannelEnum::findByCode)
                .collect(Collectors.toList());

        List<PayChannelEnum> sortList = new ArrayList<>(channelCodes.size());

        // 异步在后面
        if (asyncSort) {
            sortList.addAll(syncChannels);
            sortList.addAll(asyncChannels);
        }
        else {
            sortList.addAll(asyncChannels);
            sortList.addAll(syncChannels);
        }

        // 此处有一个根据Type的反转排序，
        return sortList.stream()
                .filter(Objects::nonNull)
                .map(RefundRepairStrategyFactory::create)
                .collect(Collectors.toList());
    }
}
