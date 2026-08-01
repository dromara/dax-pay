package cn.daxpay.open.channel.wechat.dao.isv;

import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvChannelMerchant;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 微信服务商通道商户绑定
///
@Repository
public class WechatIsvChannelMerchantManager extends BaseManager<WechatIsvChannelMerchantMapper, WechatIsvChannelMerchant> {

    /// 校验同一商户下特约商户号不重复
    public boolean existsByMchNoAndSubMchId(String mchNo, String subMchId) {
        return lambdaQuery()
                .eq(WechatIsvChannelMerchant::getMchNo, mchNo)
                .eq(WechatIsvChannelMerchant::getSubMchId, subMchId)
                .exists();
    }

    /// 根据通道商户号查询（支付/回调, 已装载 mchNo, 租户内）
    ///
    /// channelMchNo 系统生成全局唯一; 非 admin 端由 TenantLine 拦截器自动追加 mch_no 过滤,
    /// admin 端拦截器关闭走全局。忽略租户查询用 [findByChannelMchNoNotTenant]。
    public Optional<WechatIsvChannelMerchant> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(WechatIsvChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号查询（认证引导，忽略租户）
    @IgnoreTenant
    public Optional<WechatIsvChannelMerchant> findByChannelMchNoNotTenant(String channelMchNo) {
        return findByChannelMchNo(channelMchNo);
    }
}
