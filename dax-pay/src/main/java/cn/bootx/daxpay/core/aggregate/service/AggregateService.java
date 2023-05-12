package cn.bootx.daxpay.core.aggregate.service;

import cn.bootx.platform.common.redis.RedisClient;
import cn.bootx.daxpay.code.pay.PayChannelCode;
import cn.bootx.daxpay.core.aggregate.entity.AggregatePayInfo;
import cn.bootx.daxpay.exception.payment.PayFailureException;
import cn.bootx.daxpay.param.cashier.CashierSinglePayParam;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 聚合支付
 *
 * @author xxm
 * @date 2022/3/6
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AggregateService {

    private final RedisClient redisClient;

    private final String PREFIX_KEY = "cashier:pay:aggregate:";

    /**
     * 创建聚合支付QR支付, 单渠道
     */
    public String createAggregatePay(CashierSinglePayParam param) {
        // 保存并生成code
        AggregatePayInfo aggregatePayInfo = new AggregatePayInfo().setAmount(param.getAmount())
            .setTitle(param.getTitle())
            .setBusinessId(param.getBusinessId());
        String key = RandomUtil.randomString(10);
        redisClient.setWithTimeout(PREFIX_KEY + key, JSONUtil.toJsonStr(aggregatePayInfo), 5 * 60 * 1000);
        return key;
    }

    /**
     * 获取聚合支付信息
     */
    public AggregatePayInfo getAggregateInfo(String key) {
        String jsonStr = Optional.ofNullable(redisClient.get(PREFIX_KEY + key))
            .orElseThrow(() -> new PayFailureException("支付超时"));
        return JSONUtil.toBean(jsonStr, AggregatePayInfo.class);
    }

    /**
     * 聚合付款码支付处理
     */
    public int getPayChannel(String authCode) {
        if (StrUtil.isBlank(authCode)) {
            throw new PayFailureException("付款码不可为空");
        }
        String[] wx = { "10", "11", "12", "13", "14", "15" };
        String[] ali = { "25", "26", "27", "28", "29", "30" };

        // 微信
        if (StrUtil.startWithAny(authCode.substring(0, 2), wx)) {
            return PayChannelCode.WECHAT;
        }
        // 支付宝
        else if (StrUtil.startWithAny(authCode.substring(0, 2), ali)) {
            return PayChannelCode.ALI;
        }
        else {
            throw new PayFailureException("不支持的支付方式");
        }
    }

}
