package cn.bootx.platform.daxpay.service.core.payment.pay.factory;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.param.pay.PayChannelParam;
import cn.bootx.platform.daxpay.service.core.payment.pay.strategy.*;
import cn.bootx.platform.daxpay.service.func.AbsPayStrategy;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.bootx.platform.daxpay.code.PayChannelEnum.ASYNC_TYPE_CODE;


/**
 * 支付策略工厂
 *
 * @author xxm
 * @since 2020/12/11
 */
@UtilityClass
public class PayStrategyFactory {

    /**
     * 根据传入的支付通道创建策略
     * @param payChannelParam 支付类型
     * @return 支付策略
     */
    public AbsPayStrategy create(PayChannelParam payChannelParam) {
        AbsPayStrategy strategy;
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(payChannelParam.getChannel());
        switch (channelEnum) {
            case ALI:
                strategy = SpringUtil.getBean(AliPayStrategy.class);
                break;
            case WECHAT:
                strategy = SpringUtil.getBean(WeChatPayStrategy.class);
                break;
            case UNION_PAY:
                strategy = SpringUtil.getBean(UnionPayStrategy.class);
                break;
            case WALLET:
                strategy = SpringUtil.getBean(WalletPayStrategy.class);
                break;
            default:
                throw new PayUnsupportedMethodException();
        }
        strategy.setPayChannelParam(payChannelParam);
        return strategy;
    }

    /**
     * 根据传入的支付类型批量创建策略, 异步支付在后面
     * 同步支付逻辑完后才执行异步支付的逻辑, 预防异步执行成功, 然同步执行失败. 导致异步支付无法回滚的问题
     */
    public static List<AbsPayStrategy> createAsyncLast(List<PayChannelParam> payChannelParamList) {
        return createAsyncFront(payChannelParamList, false);
    }

    /**
     * 根据传入的支付类型批量创建策略, 异步支付在前面 font
     */
    public List<AbsPayStrategy> createAsyncFront(List<PayChannelParam> payChannelParamList) {
        return createAsyncFront(payChannelParamList, true);
    }

    /**
     * 根据传入的支付类型批量创建策略
     * @param payChannelParamList 支付类型
     * @return 支付策略
     */
    private List<AbsPayStrategy> createAsyncFront(List<PayChannelParam> payChannelParamList, boolean asyncFirst) {
        if (CollectionUtil.isEmpty(payChannelParamList)) {
            return Collections.emptyList();
        }
        List<AbsPayStrategy> list = new ArrayList<>(payChannelParamList.size());

        // 同步支付
        List<PayChannelParam> syncPayChannelParamList = payChannelParamList.stream()
            .filter(Objects::nonNull)
            .filter(payModeParam -> !ASYNC_TYPE_CODE.contains(payModeParam.getChannel()))
            .collect(Collectors.toList());

        // 异步支付
        List<PayChannelParam> asyncPayChannelParamList = payChannelParamList.stream()
            .filter(Objects::nonNull)
            .filter(payModeParam -> ASYNC_TYPE_CODE.contains(payModeParam.getChannel()))
            .collect(Collectors.toList());

        List<PayChannelParam> sortList = new ArrayList<>(payChannelParamList.size());

        // 异步在后面
        if (asyncFirst) {
            sortList.addAll(asyncPayChannelParamList);
            sortList.addAll(syncPayChannelParamList);
        } else {
            sortList.addAll(syncPayChannelParamList);
            sortList.addAll(asyncPayChannelParamList);
        }

        // 此处有一个根据Type的反转排序，
        sortList.stream().filter(Objects::nonNull).forEach(payMode -> list.add(create(payMode)));
        return list;
    }

}
