package cn.bootx.platform.daxpay.service.core.payment.close.factory;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.service.core.payment.close.strategy.*;
import cn.bootx.platform.daxpay.service.func.AbsPayCloseStrategy;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.bootx.platform.daxpay.code.PayChannelEnum.ASYNC_TYPE_CODE;

/**
 * 支付关闭策略工厂
 * @author xxm
 * @since 2023/12/29
 */
@UtilityClass
public class PayCloseStrategyFactory {

    /**
     * 根据传入的支付通道创建策略
     * @return 支付策略
     */
    public static AbsPayCloseStrategy create(PayChannelEnum channelEnum) {

        AbsPayCloseStrategy strategy;
        switch (channelEnum) {
            case ALI:
                strategy = SpringUtil.getBean(AliPayCloseStrategy.class);
                break;
            case WECHAT:
                strategy = SpringUtil.getBean(WeChatPayCloseStrategy.class);
                break;
            case UNION_PAY:
                strategy = SpringUtil.getBean(UnionPayCloseStrategy.class);
                break;
            case WALLET:
                strategy = SpringUtil.getBean(WalletPayCloseStrategy.class);
                break;
            default:
                throw new PayUnsupportedMethodException();
        }
        return strategy;
    }

    /**
     * 根据传入的支付类型批量创建策略, 异步支付在后面
     */
    public static List<AbsPayCloseStrategy> createAsyncLast(List<String> channelCodes) {
        return create(channelCodes, true);
    }

    /**
     * 根据传入的支付类型批量创建策略, 异步支付在前面
     */
    public static List<AbsPayCloseStrategy> createAsyncFront(List<String> channelCodes) {
        return create(channelCodes, false);
    }

    /**
     * 根据传入的支付类型批量创建策略
     * @param asyncSort 是否异步支付在后面
     * @return 支付策略
     */
    private static List<AbsPayCloseStrategy> create(List<String> channelCodes, boolean asyncSort) {
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
                .map(PayCloseStrategyFactory::create)
                .collect(Collectors.toList());
    }
}
