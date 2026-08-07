package cn.daxpay.open.channel.alipay.dao.direct;

import cn.daxpay.open.channel.alipay.entity.direct.AlipayTransferConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 支付宝转账配置
///
/// 一个通道商户一条转账配置(一对一), 提供按通道商户号查询/删除。
///
@Repository
public class AlipayTransferConfigManager extends BaseManager<AlipayTransferConfigMapper, AlipayTransferConfig> {

    /// 按通道商户号查询转账配置(一对一)
    public Optional<AlipayTransferConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(AlipayTransferConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 按通道商户号删除转账配置(逻辑删除, 通道商户删除时级联清理)
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(AlipayTransferConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
