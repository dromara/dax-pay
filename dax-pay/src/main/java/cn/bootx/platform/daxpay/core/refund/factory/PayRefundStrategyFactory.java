package cn.bootx.platform.daxpay.core.refund.factory;

import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.core.refund.func.AbsPayRefundStrategy;
import cn.bootx.platform.daxpay.core.refund.strategy.*;
import cn.bootx.platform.daxpay.exception.payment.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.param.refund.RefundModeParam;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.val;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.bootx.platform.daxpay.code.pay.PayChannelEnum.ASYNC_TYPE_CODE;

/**
 * 退款策略工厂
 * @author xxm
 * @since 2023/7/4
 */
public class PayRefundStrategyFactory {

    /**
     * 根据传入的支付通道创建策略
     * @return 支付策略
     */
    public static AbsPayRefundStrategy create(RefundModeParam refundModeParam) {

        AbsPayRefundStrategy strategy = null;
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(refundModeParam.getPayChannel());
        switch (channelEnum) {
            case ALI:
                strategy = SpringUtil.getBean(AliPayRefundStrategy.class);
                break;
            case WECHAT:
                strategy = SpringUtil.getBean(WeChatPayRefundStrategy.class);
                break;
            case UNION_PAY:
                strategy = SpringUtil.getBean(UnionPayRefundStrategy.class);
                break;
            case CASH:
                strategy = SpringUtil.getBean(CashPayRefundStrategy.class);
                break;
            case WALLET:
                strategy = SpringUtil.getBean(WalletPayRefundStrategy.class);
                break;
            case VOUCHER:
                strategy = SpringUtil.getBean(VoucherPayRefundStrategy.class);
                break;
            case CREDIT_CARD:
                break;
            case APPLE_PAY:
                break;
            default:
                throw new PayUnsupportedMethodException();
        }
        // noinspection ConstantConditions
        strategy.setRefundModeParam(refundModeParam);
        return strategy;
    }

    /**
     * 根据传入的支付类型批量创建策略, 异步支付在后面
     */
    public static List<AbsPayRefundStrategy> createDesc(List<RefundModeParam> refundModeParams) {
        return create(refundModeParams, true);
    }

    /**
     * 根据传入的支付类型批量创建策略, 默认异步支付在前面
     */
    public static List<AbsPayRefundStrategy> create(List<RefundModeParam> refundModeParams) {
        return create(refundModeParams, false);
    }

    /**
     * 根据传入的支付类型批量创建策略
     * @param refundModeParams 支付类型
     * @return 支付策略
     */
    private static List<AbsPayRefundStrategy> create(List<RefundModeParam> refundModeParams, boolean description) {
        if (CollectionUtil.isEmpty(refundModeParams)) {
            return Collections.emptyList();
        }
        List<AbsPayRefundStrategy> list = new ArrayList<>(refundModeParams.size());

        // 同步支付
        val syncRefundModeParams = refundModeParams.stream()
                .filter(Objects::nonNull)
                .filter(payModeParam -> !ASYNC_TYPE_CODE.contains(payModeParam.getPayChannel()))
                .collect(Collectors.toList());

        // 异步支付
        val asyncRefundModeParams = refundModeParams.stream()
                .filter(Objects::nonNull)
                .filter(payModeParam -> ASYNC_TYPE_CODE.contains(payModeParam.getPayChannel()))
                .collect(Collectors.toList());

        List<RefundModeParam> sortList = new ArrayList<>(refundModeParams.size());

        // 异步在后面
        if (description) {
            sortList.addAll(syncRefundModeParams);
            sortList.addAll(asyncRefundModeParams);
        }
        else {
            sortList.addAll(asyncRefundModeParams);
            sortList.addAll(syncRefundModeParams);
        }

        // 此处有一个根据Type的反转排序，
        sortList.stream().filter(Objects::nonNull).forEach(payMode -> list.add(create(payMode)));
        return list;
    }
}
