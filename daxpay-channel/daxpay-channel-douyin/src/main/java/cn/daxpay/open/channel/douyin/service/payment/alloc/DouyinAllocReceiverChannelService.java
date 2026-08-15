package cn.daxpay.open.channel.douyin.service.payment.alloc;

import cn.daxpay.open.channel.douyin.client.DouyinChannelClient;
import cn.daxpay.open.channel.douyin.client.req.DouyinAllocReceiverReq;
import cn.daxpay.open.channel.douyin.client.resp.DouyinAllocReceiverResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 抖音分账接收方通道服务
///
/// 通过 [DouyinChannelClient] 调用子应用 dax-pay-channel-one 完成接收方
/// 通道侧注册/删除(addSplitReceiver / deleteSplitReceiver)。
/// 绑定/解绑均为同步调用, 失败抛业务异常由编排层落库失败状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinAllocReceiverChannelService {

    private final DouyinChannelClient douyinChannelClient;

    /// 绑定接收方(addSplitReceiver)
    public void bind(DouyinAllocReceiverReq req) {
        DaxResult<DouyinAllocReceiverResp> result = douyinChannelClient.allocReceiverBind(req);
        if (result.getCode() != 0) {
            // 抖音: 分账接收方绑定失败
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL,
                    "error.channel.douyin.allocReceiverBindFailed", result.getMsg());
        }
        DouyinAllocReceiverResp resp = result.getData();
        if (resp != null && StrUtil.isNotBlank(resp.getErrorCode())) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL,
                    "error.channel.douyin.allocReceiverBindFailed",
                    StrUtil.blankToDefault(resp.getErrorMsg(), resp.getErrorCode()));
        }
    }

    /// 解绑接收方(deleteSplitReceiver)
    public void unbind(DouyinAllocReceiverReq req) {
        DaxResult<DouyinAllocReceiverResp> result = douyinChannelClient.allocReceiverUnbind(req);
        if (result.getCode() != 0) {
            // 抖音: 分账接收方解绑失败
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL,
                    "error.channel.douyin.allocReceiverUnbindFailed", result.getMsg());
        }
        DouyinAllocReceiverResp resp = result.getData();
        if (resp != null && StrUtil.isNotBlank(resp.getErrorCode())) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL,
                    "error.channel.douyin.allocReceiverUnbindFailed",
                    StrUtil.blankToDefault(resp.getErrorMsg(), resp.getErrorCode()));
        }
    }
}
