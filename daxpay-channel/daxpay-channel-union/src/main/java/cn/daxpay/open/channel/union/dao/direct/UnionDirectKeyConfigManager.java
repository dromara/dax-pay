package cn.daxpay.open.channel.union.dao.direct;

import cn.daxpay.open.channel.union.entity.direct.UnionDirectKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 云闪付直连密钥配置
@Repository
public class UnionDirectKeyConfigManager extends BaseManager<UnionDirectKeyConfigMapper, UnionDirectKeyConfig> {

    /// 根据通道商户号和沙箱标志查询(双环境并存)
    public Optional<UnionDirectKeyConfig> findByChannelMchNoAndSandbox(String channelMchNo, boolean sandbox) {
        return lambdaQuery()
                .eq(UnionDirectKeyConfig::getChannelMchNo, channelMchNo)
                .eq(UnionDirectKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }

    /// 根据通道商户号删除
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(UnionDirectKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
