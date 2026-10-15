package cn.daxpay.open.channel.fuyou.dao.direct;

import cn.daxpay.open.channel.fuyou.entity.direct.FuyouDirectKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 富友直连商户密钥配置 Manager
@Slf4j
@Service
public class FuyouDirectKeyConfigManager extends BaseManager<FuyouDirectKeyConfigMapper, FuyouDirectKeyConfig> {

    /// 根据通道商户号查询
    public Optional<FuyouDirectKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(FuyouDirectKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 校验同一商户号下富友商户号是否重复
    public boolean existsByMchNoAndMchntCd(String mchNo, String mchntCd) {
        return lambdaQuery()
                .eq(FuyouDirectKeyConfig::getMchNo, mchNo)
                .eq(FuyouDirectKeyConfig::getMchntCd, mchntCd)
                .exists();
    }
}
