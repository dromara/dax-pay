package cn.daxpay.open.channel.mock.service;

import cn.daxpay.open.channel.mock.client.MockChannelClient;
import cn.daxpay.open.channel.mock.client.req.MockRefundReq;
import cn.daxpay.open.channel.mock.client.resp.MockRefundResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.ChannelResultUnknownException;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Mock 通道退款业务服务
///
/// 通过 [MockChannelClient] 调用独立 mock 子应用发起模拟退款, 机制对齐支付宝/易支付退款服务。
///
/// 请求字段口径:
/// - outTradeNo = [RefundOrder#getTradeNo](原支付资金交易号, mock 侧凭此挂靠原支付单);
/// - outRefundNo = [RefundOrder#getRelationOrderNo](实际上送通道的商户退款关联号, 回调/同步反查权威)。
///
/// 退款结果判定:
/// - mock 返回 complete=true → 退款即时成功;
/// - 结果未知(10009, 退款规则未命中个位 1/2) → 退款中, 交退款同步/回调纠正;
/// - 其他业务失败(10010 等) → 退款失败终态。
///
/// 与支付宝的差异: mock 退款请求额外携带 notifyUrl 供 mock 在退款终态时回推退款回调
/// (支付宝 SDK 退款无通知地址字段, 该差异属通道特性, 拼装机制与支付回调共用同一入口)。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockRefundService {

    private final MockChannelClient mockChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final MockCredentialSupport mockCredentialSupport;

    /// 执行模拟退款
    ///
    /// @param refundOrder 退款订单
    /// @return 退款结果(含映射后的退款状态)
    public RefundResultBo refund(RefundOrder refundOrder) {
        // 构建请求(凭证按订单 product/通道商户号取商户级两件套)
        MockRefundReq req = new MockRefundReq();
        // 原支付单商户订单号 = 原支付资金交易号(与 mock 支付下单同一口径, mock 侧凭此找到原单)
        req.setOutTradeNo(refundOrder.getTradeNo());
        // 商户退款号 = 实际上送通道的商户退款关联号(对齐支付宝 outRequestNo 口径)
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        req.setRefundAmount(refundOrder.getAmount());
        // 退款原因透传(建单时已从退款参数落入退款单, 可空)
        req.setReason(refundOrder.getReason());
        // 退款回调地址(支付/退款共用同一回调入口, 机制与 MockPayService.buildNotifyUrl 一致)
        req.setNotifyUrl(this.buildNotifyUrl(refundOrder));
        req.setCredential(mockCredentialSupport.build(refundOrder.getProduct(), refundOrder.getChannelMchNo()));

        // 调用 mock 子应用
        DaxResult<MockRefundResp> result = mockChannelClient.refund(req);
        // mock 返回"结果未知"(退款规则未命中, 处理中): 退款中, 交退款同步/异步回调纠正
        if (result.getCode() == ChannelResultUnknownException.RESULT_UNKNOWN_CODE) {
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.PROGRESS);
        }
        if (result.getCode() != 0) {
            log.error("模拟通道退款失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            // 退款失败走失败终态, 业务失败原因由 DaxResult 透传, 不抛异常(与支付宝一致由编排判定)
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toRefundResult(result.getData());
    }

    /// 解析 mock 响应(complete=true 即时成功, 否则退款中交同步确认)
    private RefundResultBo toRefundResult(MockRefundResp resp) {
        RefundResultBo bo = new RefundResultBo();
        // mock 退款流水号作为通道退款交易号
        bo.setOutRefundNo(resp.getRefundId());
        bo.setFinishTime(resp.getFinishTime());
        bo.setRefundAmount(resp.getRefundAmount());
        if (Boolean.TRUE.equals(resp.getComplete())) {
            return bo.setComplete(true)
                    .setStatus(RefundOrderStatusEnum.SUCCESS);
        }
        return bo.setComplete(false)
                .setStatus(RefundOrderStatusEnum.PROGRESS);
    }

    /// 生成 mock 通道退款异步通知地址(mock→平台)
    ///
    /// 路径约定与支付共用同一回调入口: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/mock`
    /// (机制对齐支付宝/易支付支付/退款共用回调端点, 见 [MockPayService] 同名方法)。
    private String buildNotifyUrl(RefundOrder refundOrder) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            // 平台后端访问地址(backendBaseUrl)未配置, 无法生成回调地址
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/mock",
                base, refundOrder.getMchNo(), refundOrder.getChannelMchNo());
    }
}
