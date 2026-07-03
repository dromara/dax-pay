package cn.daxpay.open.channel.douyin.strategy;

import cn.daxpay.open.channel.douyin.client.DouyinChannelClient;
import cn.daxpay.open.channel.douyin.dto.DouyinPayReq;
import cn.daxpay.open.channel.douyin.dto.DouyinPayResp;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.bo.PayTradeResultBo;
import cn.daxpay.open.payment.core.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/// # 抖音支付直连支付策略
///
/// 通过 [DouyinChannelClient] 调用子应用 dax-pay-channel-one 完成抖音支付下单。
/// TODO 通道配置获取逻辑待补充(需抖音通道配置管理实体)
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinDirectPayStrategy extends AbsNormalPayStrategy {

    private final DouyinChannelClient douyinChannelClient;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.DOUYIN_PAY;
    }

    @Override
    public PayTradeResultBo doPay(PayStrategyContext context) {
        // 构建请求
        DouyinPayReq req = new DouyinPayReq();
        req.setChannel(ProductEnum.DOUYIN_PAY.getChannel());
        req.setBizOrderNo(context.getPayParam().getBizOrderNo());
        req.setAmount(context.getPayParam().getAmount());
        req.setSubject(context.getPayParam().getTitle());
        req.setMethod(context.getPayParam().getMethod());
        // TODO 从数据库获取抖音通道配置
        req.setConfig(Map.of());

        // 调用子应用
        DaxResult<DouyinPayResp> result = douyinChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new IllegalStateException("抖音通道支付失败: " + result.getMsg());
        }

        // 解析响应
        DouyinPayResp data = result.getData();
        return new PayTradeResultBo()
                .setOutOrderNo(data.getOutOrderNo())
                .setComplete(Boolean.TRUE.equals(data.getComplete()))
                .setPayBody(data.getPayBody())
                .setTransOrderNo(data.getTransOrderNo());
    }
}
