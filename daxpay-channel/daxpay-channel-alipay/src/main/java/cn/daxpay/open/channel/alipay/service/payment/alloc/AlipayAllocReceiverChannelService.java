package cn.daxpay.open.channel.alipay.service.payment.alloc;

import cn.daxpay.open.channel.alipay.client.AlipayChannelClient;
import cn.daxpay.open.channel.alipay.client.req.AlipayAllocReceiverReq;
import cn.daxpay.open.channel.alipay.client.resp.AlipayAllocReceiverResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 支付宝分账接收方通道服务
///
/// 通过 [AlipayChannelClient] 调用子应用 dax-pay-channel-one 完成接收方
/// 通道侧注册/删除(alipay.trade.royalty.relation.bind / unbind)。
/// 直连与服务商身份由凭证字段区分(appAuthToken), 请求业务字段一致, 共用端点。
/// 绑定/解绑均为同步调用, 失败抛业务异常由编排层落库失败状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayAllocReceiverChannelService {

    private final AlipayChannelClient alipayChannelClient;

    /// 绑定接收方(alipay.trade.royalty.relation.bind)
    public void bind(AlipayAllocReceiverReq req) {
        DaxResult<AlipayAllocReceiverResp> result = alipayChannelClient.allocReceiverBind(req);
        if (result.getCode() != 0) {
            // 支付宝: 分账接收方绑定失败
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL,
                    "error.channel.alipay.allocReceiverBindFailed", result.getMsg());
        }
        AlipayAllocReceiverResp resp = result.getData();
        if (resp != null && StrUtil.isNotBlank(resp.getErrorCode())) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL,
                    "error.channel.alipay.allocReceiverBindFailed",
                    StrUtil.blankToDefault(resp.getErrorMsg(), resp.getErrorCode()));
        }
    }

    /// 解绑接收方(alipay.trade.royalty.relation.unbind)
    public void unbind(AlipayAllocReceiverReq req) {
        DaxResult<AlipayAllocReceiverResp> result = alipayChannelClient.allocReceiverUnbind(req);
        if (result.getCode() != 0) {
            // 支付宝: 分账接收方解绑失败
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL,
                    "error.channel.alipay.allocReceiverUnbindFailed", result.getMsg());
        }
        AlipayAllocReceiverResp resp = result.getData();
        if (resp != null && StrUtil.isNotBlank(resp.getErrorCode())) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL,
                    "error.channel.alipay.allocReceiverUnbindFailed",
                    StrUtil.blankToDefault(resp.getErrorMsg(), resp.getErrorCode()));
        }
    }
}
