package cn.daxpay.open.channel.duolabao.dao.direct;

import cn.daxpay.open.channel.duolabao.entity.direct.DuolabaoDirectKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 哆啦宝直连密钥配置
@Repository
public class DuolabaoDirectKeyConfigManager extends BaseManager<DuolabaoDirectKeyConfigMapper, DuolabaoDirectKeyConfig> {

    /// 根据通道商户号查询
    public Optional<DuolabaoDirectKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(DuolabaoDirectKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号和沙箱标志查询(双环境并存)
    public Optional<DuolabaoDirectKeyConfig> findByChannelMchNoAndSandbox(String channelMchNo, boolean sandbox) {
        return lambdaQuery()
                .eq(DuolabaoDirectKeyConfig::getChannelMchNo, channelMchNo)
                .eq(DuolabaoDirectKeyConfig::getSandbox, sandbox)
                .oneOpt();
    }

    /// 根据通道商户号删除
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(DuolabaoDirectKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
