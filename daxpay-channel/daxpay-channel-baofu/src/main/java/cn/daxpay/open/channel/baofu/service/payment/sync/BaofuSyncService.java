package cn.daxpay.open.channel.baofu.service.payment.sync;

import cn.daxpay.open.channel.baofu.client.BaofuChannelClient;
import cn.daxpay.open.channel.baofu.client.credential.BaofuSdkCredential;
import cn.daxpay.open.channel.baofu.client.req.BaofuSyncReq;
import cn.daxpay.open.channel.baofu.client.resp.BaofuSyncResp;
import cn.daxpay.open.channel.baofu.code.BaofuCode;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 宝付支付同步业务服务
///
/// 通过 [BaofuChannelClient] 调用子应用查询宝付 order_query 订单状态,
/// 将宝付交易状态(txnState: SUCCESS 成功, 其他处理中/未支付)映射为平台 [PayFundStatusEnum]。
///
/// 按商户订单号(tradeNo)查询, 通道订单号未知也可查询。
/// 订单不存在时子应用抛通道业务异常, 本服务转为软失败等待回调。
@Slf4j
@Service
@RequiredArgsConstructor
public class BaofuSyncService {

    private final BaofuChannelClient baofuChannelClient;

    /// 执行宝付支付同步
    public PaySyncResultBo sync(PayTrade trade, BaofuSdkCredential credential) {
        BaofuSyncReq req = new BaofuSyncReq();
        req.setOutTradeNo(trade.getTradeNo());
        req.setCredential(credential);

        DaxResult<BaofuSyncResp> result = baofuChannelClient.sync(req);
        if (result.getCode() != 0) {
            log.error("宝付通道同步失败: outTradeNo={}, msg={}", trade.getTradeNo(), result.getMsg());
            return new PaySyncResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应
    private PaySyncResultBo toSyncResult(BaofuSyncResp resp) {
        PaySyncResultBo bo = new PaySyncResultBo();
        bo.setOutOrderNo(resp.getOutOrderNo());
        bo.setRealAmount(resp.getRealAmount());
        bo.setBuyerId(resp.getBuyerId());
        bo.setFinishTime(resp.getFinishTime());

        String tradeStatus = resp.getTradeState();
        if (StrUtil.isBlank(tradeStatus)) {
            return bo.setSyncSuccess(false)
                    .setSyncErrorMsg(StrUtil.blankToDefault(resp.getSyncData(), "宝付同步查询失败"));
        }

        // 宝付交易状态映射(SUCCESS 成功, 其他视为处理中/未支付, 对齐参考实现仅认 SUCCESS)
        return switch (tradeStatus) {
            case BaofuCode.TRADE_STATUS_SUCCESS -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.SUCCESS);
            default -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.PROCESSING);
        };
    }
}
