package cn.daxpay.open.channel.kuaiqian.service.payment.sync;

import cn.daxpay.open.channel.kuaiqian.client.KuaiqianChannelClient;
import cn.daxpay.open.channel.kuaiqian.client.credential.KuaiqianSdkCredential;
import cn.daxpay.open.channel.kuaiqian.client.req.KuaiqianSyncReq;
import cn.daxpay.open.channel.kuaiqian.client.resp.KuaiqianSyncResp;
import cn.daxpay.open.channel.kuaiqian.code.KuaiqianCode;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 快钱支付同步业务服务
///
/// 通过 [KuaiqianChannelClient] 调用子应用查询快钱 A7006 订单状态,
/// 将快钱交易状态(txnStatus: S 成功 / F 失败 / R 拒绝 / 其他处理中)映射为平台 [PayFundStatusEnum]。
///
/// A7006 仅支持按快钱订单控制号(idOrderCtrl)查询, 未收到下单回执/回调(控制号为空)时返回软失败。
@Slf4j
@Service
@RequiredArgsConstructor
public class KuaiqianSyncService {

    private final KuaiqianChannelClient kuaiqianChannelClient;

    /// 执行快钱支付同步
    public PaySyncResultBo sync(PayTrade trade, KuaiqianSdkCredential credential) {
        // 通道订单号未知时无法发起 A7006 查询, 软失败等待回调补充
        if (StrUtil.isBlank(trade.getOutOrderNo())) {
            return new PaySyncResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg("快钱订单控制号未知(等待回调补充), 暂无法查询");
        }
        KuaiqianSyncReq req = new KuaiqianSyncReq();
        req.setOutTradeNo(trade.getTradeNo());
        req.setOutOrderNo(trade.getOutOrderNo());
        req.setCredential(credential);

        DaxResult<KuaiqianSyncResp> result = kuaiqianChannelClient.sync(req);
        if (result.getCode() != 0) {
            log.error("快钱通道同步失败: outTradeNo={}, msg={}", trade.getTradeNo(), result.getMsg());
            return new PaySyncResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应
    private PaySyncResultBo toSyncResult(KuaiqianSyncResp resp) {
        PaySyncResultBo bo = new PaySyncResultBo();
        bo.setOutOrderNo(resp.getOutOrderNo());
        bo.setRealAmount(resp.getRealAmount());
        bo.setBuyerId(resp.getBuyerId());
        bo.setFinishTime(resp.getFinishTime());

        String tradeStatus = resp.getTradeState();
        if (StrUtil.isBlank(tradeStatus)) {
            return bo.setSyncSuccess(false)
                    .setSyncErrorMsg(StrUtil.blankToDefault(resp.getSyncData(), "快钱同步查询失败"));
        }

        // 快钱交易状态映射
        return switch (tradeStatus) {
            case KuaiqianCode.TRADE_STATUS_SUCCESS -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.SUCCESS);
            case KuaiqianCode.TRADE_STATUS_FAIL, KuaiqianCode.TRADE_STATUS_REJECT ->
                    bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.FAIL);
            default -> bo.setSyncSuccess(true).setPayStatus(PayFundStatusEnum.PROCESSING);
        };
    }
}
