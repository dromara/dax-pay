package cn.daxpay.open.channel.douyin.service.payment.alloc;

import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectAllocReceiverManager;
import cn.daxpay.open.payment.douyin.facade.DouyinAllocReceiverFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 抖音分账接收方引用查询实现
///
/// [DouyinAllocReceiverFacade] 的 channel-douyin 实现: 检查直连接收方档案的应用引用,
/// 供主应用侧(payment-core)的抖音应用删除前调用。
@Service
@RequiredArgsConstructor
public class DouyinAllocReceiverRefService implements DouyinAllocReceiverFacade {

    private final DouyinDirectAllocReceiverManager douyinDirectAllocReceiverManager;

    /// 商户档应用引用检查: 直连 channelAppId
    @Override
    public boolean existsReceiverByMchApp(String mchNo, String douyinAppId) {
        return douyinDirectAllocReceiverManager.existsByMchNoAndChannelAppId(mchNo, douyinAppId);
    }
}
