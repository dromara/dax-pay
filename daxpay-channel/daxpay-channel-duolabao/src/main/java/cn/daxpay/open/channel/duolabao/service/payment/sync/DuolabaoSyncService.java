package cn.daxpay.open.channel.duolabao.service.payment.sync;

import cn.daxpay.open.channel.duolabao.client.DuolabaoChannelClient;
import cn.daxpay.open.channel.duolabao.client.credential.DuolabaoSdkCredential;
import cn.daxpay.open.channel.duolabao.client.req.DuolabaoSyncReq;
import cn.daxpay.open.channel.duolabao.client.resp.DuolabaoSyncResp;
import cn.daxpay.open.channel.duolabao.code.DuolabaoCode;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 哆啦宝支付同步业务服务
///
/// 通过 [DuolabaoChannelClient] 调用子应用查询哆啦宝订单状态(queryOrderPayDetail),
/// 将哆啦宝订单状态(status: SUCCESS=成功 / FAIL=失败 / 其他=处理中, 参考插件仅区分成功与非成功两类)
/// 映射为平台 [PayFundStatusEnum]。
///
/// 查单按商户单号(requestNum)路由, 不依赖通道订单号。
@Slf4j
@Service
@RequiredArgsConstructor
public class DuolabaoSyncService {

    private final DuolabaoChannelClient duolabaoChannelClient;

    /// 执行哆啦宝支付同步
    public PaySyncResultBo sync(PayTrade trade, DuolabaoSdkCredential credential) {
        DuolabaoSyncReq req = new DuolabaoSyncReq();
        req.setCredential(credential);
        // 商户单号 = 下单时的 requestNum = 平台支付交易号
        req.setOutTradeNo(trade.getTradeNo());

        DaxResult<DuolabaoSyncResp> result = duolabaoChannelClient.sync(req);
        if (result.getCode() != 0) {
            log.error("哆啦宝通道同步失败: outTradeNo={}, msg={}", trade.getTradeNo(), result.getMsg());
            return new PaySyncResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return this.toSyncResult(result.getData());
    }

    /// 解析子应用响应
    private PaySyncResultBo toSyncResult(DuolabaoSyncResp resp) {
        PaySyncResultBo bo = new PaySyncResultBo();
        bo.setSyncSuccess(true);
        bo.setSyncData(resp.getSyncData());
        bo.setOutOrderNo(resp.getOutOrderNo());
        bo.setRealAmount(resp.getRealAmount());
        bo.setFinishTime(resp.getFinishTime());
        bo.setBuyerId(resp.getBuyerId());
        // 哆啦宝订单状态映射(参考插件仅区分成功/非成功两类, FAIL 防御性识别为失败)
        if (DuolabaoCode.TRADE_STATUS_SUCCESS.equals(resp.getTradeState())) {
            bo.setPayStatus(PayFundStatusEnum.SUCCESS);
            bo.setAmount(resp.getRealAmount());
        } else if (DuolabaoCode.TRADE_STATUS_FAIL.equals(resp.getTradeState())) {
            bo.setPayStatus(PayFundStatusEnum.FAIL);
        } else {
            bo.setPayStatus(PayFundStatusEnum.PROCESSING);
        }
        return bo;
    }
}
