package cn.daxpay.open.channel.yizhifubj.service.payment.sync;

import cn.daxpay.open.channel.yizhifubj.client.YizhifubjChannelClient;
import cn.daxpay.open.channel.yizhifubj.client.credential.YizhifubjSdkCredential;
import cn.daxpay.open.channel.yizhifubj.client.req.YizhifubjSyncReq;
import cn.daxpay.open.channel.yizhifubj.client.resp.YizhifubjSyncResp;
import cn.daxpay.open.channel.yizhifubj.code.YizhifubjCode;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 一直付支付同步业务服务
///
/// 通过 [YizhifubjChannelClient] 调用子应用查询 5upay `/onlinePay/query` 订单状态,
/// 将一直付交易状态(SUCCESS 成功 / FAILED、CANCEL 失败 / 其他处理中)映射为平台 [PayFundStatusEnum]。
///
/// 查询按商户订单号(requestId)发起, 下单后即可查, 不依赖通道流水号。
@Slf4j
@Service
@RequiredArgsConstructor
public class YizhifubjSyncService {

    private final YizhifubjChannelClient yizhifubjChannelClient;

    /// 执行一直付支付同步
    public PaySyncResultBo sync(PayTrade trade, YizhifubjSdkCredential credential) {
        YizhifubjSyncReq req = new YizhifubjSyncReq();
        req.setOutTradeNo(trade.getTradeNo());
        req.setCredential(credential);

        DaxResult<YizhifubjSyncResp> result = yizhifubjChannelClient.sync(req);
        if (result.getCode() != 0) {
            log.error("一直付通道同步失败: outTradeNo={}, msg={}", trade.getTradeNo(), result.getMsg());
            return new PaySyncResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应
    private PaySyncResultBo toSyncResult(YizhifubjSyncResp resp) {
        PaySyncResultBo bo = new PaySyncResultBo();
        bo.setOutOrderNo(resp.getOutOrderNo());
        bo.setRealAmount(resp.getRealAmount());
        bo.setFinishTime(resp.getFinishTime());

        String tradeStatus = resp.getTradeState();
        if (tradeStatus == null || tradeStatus.isEmpty()) {
            return bo.setSyncSuccess(false)
                    .setSyncErrorMsg(StrUtil.blankToDefault(resp.getSyncData(), "一直付同步查询失败"));
        }

        // 一直付交易状态映射
        return switch (tradeStatus) {
            case YizhifubjCode.TRADE_STATUS_SUCCESS -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.SUCCESS);
            case YizhifubjCode.TRADE_STATUS_FAIL, YizhifubjCode.TRADE_STATUS_CANCEL ->
                    bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.FAIL);
            default -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.PROCESSING);
        };
    }
}
