package cn.daxpay.open.channel.wechat.dao.direct;

import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectChannelMerchant;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 微信直连通道商户绑定
///
@Repository
public class WechatDirectChannelMerchantManager extends BaseManager<WechatDirectChannelMerchantMapper, WechatDirectChannelMerchant> {

    /// 校验同一商户下微信直连商户号不重复
    public boolean existsByMchNoAndWxMchId(String mchNo, String wxMchId) {
        return lambdaQuery()
                .eq(WechatDirectChannelMerchant::getMchNo, mchNo)
                .eq(WechatDirectChannelMerchant::getWxMchId, wxMchId)
                .exists();
    }

    /// 根据通道商户号查询（支付/回调, 已装载 mchNo, 租户内）
    ///
    /// channelMchNo 系统生成全局唯一; 非 admin 端由 TenantLine 拦截器自动追加 mch_no 过滤,
    /// admin 端拦截器关闭走全局。忽略租户查询见 [WechatIsvChannelMerchantManager] 同名 NotTenant 变体。
    public Optional<WechatDirectChannelMerchant> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(WechatDirectChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt();
    }
}
