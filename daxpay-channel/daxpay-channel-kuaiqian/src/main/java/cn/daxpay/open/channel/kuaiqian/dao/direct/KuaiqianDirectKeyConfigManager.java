package cn.daxpay.open.channel.kuaiqian.dao.direct;

import cn.daxpay.open.channel.kuaiqian.entity.direct.KuaiqianDirectKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 快钱直连密钥配置
@Repository
public class KuaiqianDirectKeyConfigManager extends BaseManager<KuaiqianDirectKeyConfigMapper, KuaiqianDirectKeyConfig> {

    /// 根据通道商户号查询
    public Optional<KuaiqianDirectKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(KuaiqianDirectKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号和沙箱标志查询(双环境并存)
    public Optional<KuaiqianDirectKeyConfig> findByChannelMchNoAndSandbox(String channelMchNo, boolean sandbox) {
        return lambdaQuery()
                .eq(KuaiqianDirectKeyConfig::getChannelMchNo, channelMchNo)
                .eq(KuaiqianDirectKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }

    /// 根据通道商户号删除
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(KuaiqianDirectKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
