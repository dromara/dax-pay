package cn.daxpay.open.channel.baofu.dao.direct;

import cn.daxpay.open.channel.baofu.entity.direct.BaofuDirectKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 宝付直连密钥配置
@Repository
public class BaofuDirectKeyConfigManager extends BaseManager<BaofuDirectKeyConfigMapper, BaofuDirectKeyConfig> {

    /// 根据通道商户号查询
    public Optional<BaofuDirectKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(BaofuDirectKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号和沙箱标志查询(双环境并存)
    public Optional<BaofuDirectKeyConfig> findByChannelMchNoAndSandbox(String channelMchNo, boolean sandbox) {
        return lambdaQuery()
                .eq(BaofuDirectKeyConfig::getChannelMchNo, channelMchNo)
                .eq(BaofuDirectKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }

    /// 根据通道商户号删除
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(BaofuDirectKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
