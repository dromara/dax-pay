package cn.bootx.platform.daxpay.core.payment.refund.factory;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.func.AbsPayRefundStrategy;
import cn.bootx.platform.daxpay.core.payment.refund.strategy.*;
import cn.bootx.platform.daxpay.exception.pay.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.param.pay.RefundChannelParam;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.val;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.bootx.platform.daxpay.code.PayChannelEnum.ASYNC_TYPE_CODE;

/**
 * 退款策略工厂
 * @author xxm
 * @since 2023/7/4
 */
public class PayRefundStrategyFactory {

    /**
     * 根据传入的支付渠道创建策略
     * @return 支付策略
     */
    public static AbsPayRefundStrategy createAsyncFront(RefundChannelParam refundChannelParam) {

        AbsPayRefundStrategy strategy;
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(refundChannelParam.getChannel());
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
            default:
                throw new PayUnsupportedMethodException();
        }
        strategy.setChannelParam(refundChannelParam);
        return strategy;
    }

    /**
     * 根据传入的支付类型批量创建策略, 异步支付在后面
     * 同步支付逻辑完后才执行异步支付的逻辑, 预防异步执行成功, 然同步执行失败. 导致异步支付无法回滚的问题
     */
    public static List<AbsPayRefundStrategy> createAsyncLast(List<RefundChannelParam> refundChannelParams) {
        return createAsyncFront(refundChannelParams, true);
    }

    /**
     * 根据传入的支付类型批量创建策略, 异步支付在前面
     */
    public static List<AbsPayRefundStrategy> createAsyncFront(List<RefundChannelParam> refundChannelParams) {
        return createAsyncFront(refundChannelParams, false);
    }

    /**
     * 根据传入的支付类型批量创建策略
     * @param refundChannelParams 支付类型
     * @return 支付策略
     */
    private static List<AbsPayRefundStrategy> createAsyncFront(List<RefundChannelParam> refundChannelParams, boolean description) {
        if (CollectionUtil.isEmpty(refundChannelParams)) {
            return Collections.emptyList();
        }
        List<AbsPayRefundStrategy> list = new ArrayList<>(refundChannelParams.size());

        // 同步支付
        val syncRefundModeParams = refundChannelParams.stream()
                .filter(Objects::nonNull)
                .filter(payModeParam -> !ASYNC_TYPE_CODE.contains(payModeParam.getChannel()))
                .collect(Collectors.toList());

        // 异步支付
        val asyncRefundModeParams = refundChannelParams.stream()
                .filter(Objects::nonNull)
                .filter(payModeParam -> ASYNC_TYPE_CODE.contains(payModeParam.getChannel()))
                .collect(Collectors.toList());

        List<RefundChannelParam> sortList = new ArrayList<>(refundChannelParams.size());

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
        sortList.stream().filter(Objects::nonNull).forEach(payMode -> list.add(createAsyncFront(payMode)));
        return list;
    }
}
