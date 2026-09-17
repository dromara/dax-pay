package cn.daxpay.open.channel.sheng.service.payment;

import cn.daxpay.open.channel.sheng.client.ShengChannelClient;
import cn.daxpay.open.channel.sheng.client.credential.ShengSdkCredential;
import cn.daxpay.open.channel.sheng.client.req.ShengRefundReq;
import cn.daxpay.open.channel.sheng.client.resp.ShengRefundResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 盛付通退款执行业务服务
///
/// 通过 [ShengChannelClient] 调用子应用完成盛付通退款。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengRefundService {

    private final ShengChannelClient shengChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行退款
    public RefundResultBo refund(RefundOrder refundOrder, ShengSdkCredential credential) {
        ShengRefundReq req = new ShengRefundReq();
        req.setCredential(credential);
        // 退款单号(作为盛付通商户退款单号)
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 原支付订单号(平台 tradeNo 作为原商户订单号)
        req.setOriginOutTradeNo(refundOrder.getTradeNo());
        // 原通道交易号(首次退款时可能为空)
        req.setOriginTradeNo(refundOrder.getOutOrderNo());
        req.setAmount(refundOrder.getAmount());
        req.setReason(refundOrder.getReason());
        // 退款异步通知地址(盛付通 → 平台退款回调端点)
        req.setNotifyUrl(this.buildRefundNotifyUrl(refundOrder));
        req.setClientIp(refundOrder.getClientIp());

        DaxResult<ShengRefundResp> result = shengChannelClient.refund(req);
        if (result.getCode() != 0) {
            // 盛付通退款异常
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.sheng.refundFailed", result.getMsg());
        }

        ShengRefundResp resp = result.getData();
        RefundResultBo bo = new RefundResultBo()
                .setOutRefundNo(resp.getTradeNo())
                .setFinishTime(resp.getFinishTime())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()));
        // 退款状态: 已完成视为成功, 否则处理中
        bo.setStatus(Boolean.TRUE.equals(resp.getComplete())
                ? RefundOrderStatusEnum.SUCCESS
                : RefundOrderStatusEnum.PROGRESS);
        return bo;
    }

    /// 生成盛付通退款异步通知地址(盛付通 → 平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/sheng/refund`
    private String buildRefundNotifyUrl(RefundOrder refundOrder) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            // 后端服务地址未配置
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/sheng/refund",
                base, refundOrder.getMchNo(), refundOrder.getChannelMchNo());
    }
}
