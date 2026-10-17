package cn.daxpay.open.channel.yizhifubj.dao.direct;

import cn.daxpay.open.channel.yizhifubj.entity.direct.YizhifubjDirectKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 一直付直连密钥配置
@Repository
public class YizhifubjDirectKeyConfigManager extends BaseManager<YizhifubjDirectKeyConfigMapper, YizhifubjDirectKeyConfig> {

    /// 根据通道商户号查询
    public Optional<YizhifubjDirectKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(YizhifubjDirectKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号和沙箱标志查询(双环境并存)
    public Optional<YizhifubjDirectKeyConfig> findByChannelMchNoAndSandbox(String channelMchNo, boolean sandbox) {
        return lambdaQuery()
                .eq(YizhifubjDirectKeyConfig::getChannelMchNo, channelMchNo)
                .eq(YizhifubjDirectKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }

    /// 根据通道商户号删除
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(YizhifubjDirectKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
