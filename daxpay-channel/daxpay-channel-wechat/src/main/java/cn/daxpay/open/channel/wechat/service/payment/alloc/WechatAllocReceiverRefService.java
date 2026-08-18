package cn.daxpay.open.channel.wechat.service.payment.alloc;

import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectAllocReceiverManager;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvAllocReceiverManager;
import cn.daxpay.open.payment.wx.facade.WxAllocReceiverFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 微信分账接收方引用查询实现
///
/// [WxAllocReceiverFacade] 的 channel-wechat 实现: 聚合直连/服务商接收方档案的应用引用检查,
/// 供主应用侧(payment-core)的微信应用删除前调用。
@Service
@RequiredArgsConstructor
public class WechatAllocReceiverRefService implements WxAllocReceiverFacade {

    private final WechatDirectAllocReceiverManager wechatDirectAllocReceiverManager;
    private final WechatIsvAllocReceiverManager wechatIsvAllocReceiverManager;

    /// 商户档应用引用检查: 直连 channelAppId + 服务商 subAppId
    @Override
    public boolean existsReceiverByMchApp(String mchNo, String wxAppId) {
        return wechatDirectAllocReceiverManager.existsByMchNoAndChannelAppId(mchNo, wxAppId)
                || wechatIsvAllocReceiverManager.existsByMchNoAndSubAppId(mchNo, wxAppId);
    }

    /// 平台档应用引用检查: 服务商 spAppId(全局)
    @Override
    public boolean existsReceiverByPlatformApp(String wxAppId) {
        return wechatIsvAllocReceiverManager.existsBySpAppId(wxAppId);
    }
}
