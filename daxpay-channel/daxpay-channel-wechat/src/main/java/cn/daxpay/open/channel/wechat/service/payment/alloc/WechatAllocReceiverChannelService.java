package cn.daxpay.open.channel.wechat.service.payment.alloc;

import cn.daxpay.open.channel.wechat.client.WechatChannelClient;
import cn.daxpay.open.channel.wechat.client.req.WechatAllocReceiverReq;
import cn.daxpay.open.channel.wechat.client.resp.WechatAllocReceiverResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信分账接收方通道服务
///
/// 通过 [WechatChannelClient] 调用子应用 dax-pay-channel-one 完成接收方
/// 通道侧注册/删除(V3 profitsharing/receivers/add / delete)。
/// 直连与服务商身份由凭证字段区分(subMchId/subAppId), 请求业务字段一致。
/// 绑定/解绑均为同步调用, 失败抛业务异常由编排层落库失败状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatAllocReceiverChannelService {

    private final WechatChannelClient wechatChannelClient;

    /// 绑定接收方(V3 profitsharing/receivers/add)
    public void bind(WechatAllocReceiverReq req) {
        DaxResult<WechatAllocReceiverResp> result = wechatChannelClient.allocReceiverBind(req);
        if (result.getCode() != 0) {
            // 微信: 分账接收方绑定失败
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL,
                    "error.channel.wechat.allocReceiverBindFailed", result.getMsg());
        }
        WechatAllocReceiverResp resp = result.getData();
        if (resp != null && StrUtil.isNotBlank(resp.getErrorCode())) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL,
                    "error.channel.wechat.allocReceiverBindFailed",
                    StrUtil.blankToDefault(resp.getErrorMsg(), resp.getErrorCode()));
        }
    }

    /// 解绑接收方(V3 profitsharing/receivers/delete)
    public void unbind(WechatAllocReceiverReq req) {
        DaxResult<WechatAllocReceiverResp> result = wechatChannelClient.allocReceiverUnbind(req);
        if (result.getCode() != 0) {
            // 微信: 分账接收方解绑失败
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL,
                    "error.channel.wechat.allocReceiverUnbindFailed", result.getMsg());
        }
        WechatAllocReceiverResp resp = result.getData();
        if (resp != null && StrUtil.isNotBlank(resp.getErrorCode())) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL,
                    "error.channel.wechat.allocReceiverUnbindFailed",
                    StrUtil.blankToDefault(resp.getErrorMsg(), resp.getErrorCode()));
        }
    }

    /// 服务商绑定接收方(V3 profitsharing/receivers/add, sub_mchid 维度)
    public void isvBind(WechatAllocReceiverReq req) {
        DaxResult<WechatAllocReceiverResp> result = wechatChannelClient.isvAllocReceiverBind(req);
        if (result.getCode() != 0) {
            // 微信: 分账接收方绑定失败
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL,
                    "error.channel.wechat.allocReceiverBindFailed", result.getMsg());
        }
        WechatAllocReceiverResp resp = result.getData();
        if (resp != null && StrUtil.isNotBlank(resp.getErrorCode())) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL,
                    "error.channel.wechat.allocReceiverBindFailed",
                    StrUtil.blankToDefault(resp.getErrorMsg(), resp.getErrorCode()));
        }
    }

    /// 服务商解绑接收方(V3 profitsharing/receivers/delete, sub_mchid 维度)
    public void isvUnbind(WechatAllocReceiverReq req) {
        DaxResult<WechatAllocReceiverResp> result = wechatChannelClient.isvAllocReceiverUnbind(req);
        if (result.getCode() != 0) {
            // 微信: 分账接收方解绑失败
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL,
                    "error.channel.wechat.allocReceiverUnbindFailed", result.getMsg());
        }
        WechatAllocReceiverResp resp = result.getData();
        if (resp != null && StrUtil.isNotBlank(resp.getErrorCode())) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL,
                    "error.channel.wechat.allocReceiverUnbindFailed",
                    StrUtil.blankToDefault(resp.getErrorMsg(), resp.getErrorCode()));
        }
    }
}
