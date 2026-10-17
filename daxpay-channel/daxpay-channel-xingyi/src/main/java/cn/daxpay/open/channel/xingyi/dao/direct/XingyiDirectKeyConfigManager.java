package cn.daxpay.open.channel.xingyi.dao.direct;

import cn.daxpay.open.channel.xingyi.entity.direct.XingyiDirectKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 星驿付直连密钥配置
@Repository
public class XingyiDirectKeyConfigManager extends BaseManager<XingyiDirectKeyConfigMapper, XingyiDirectKeyConfig> {

    /// 根据通道商户号和沙箱标志查询(双环境并存)
    public Optional<XingyiDirectKeyConfig> findByChannelMchNoAndSandbox(String channelMchNo, boolean sandbox) {
        return lambdaQuery()
                .eq(XingyiDirectKeyConfig::getChannelMchNo, channelMchNo)
                .eq(XingyiDirectKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }

    /// 根据通道商户号删除(两环境记录一并清理)
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(XingyiDirectKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
