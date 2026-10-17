package cn.daxpay.open.channel.fuyou.dao.rsa;

import cn.daxpay.open.channel.fuyou.entity.rsa.FuyouRsaKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 富友 RSA 密钥对配置 Manager
@Slf4j
@Service
public class FuyouRsaKeyConfigManager extends BaseManager<FuyouRsaKeyConfigMapper, FuyouRsaKeyConfig> {

    /// 根据通道商户号查询(通道商户号全局唯一, 产品由行内 product 字段表达)
    public Optional<FuyouRsaKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(FuyouRsaKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 校验同一商户号同一产品下富友商户号是否重复
    public boolean existsByMchNoAndProductAndMchntCd(String mchNo, String product, String mchntCd) {
        return lambdaQuery()
                .eq(FuyouRsaKeyConfig::getMchNo, mchNo)
                .eq(FuyouRsaKeyConfig::getProduct, product)
                .eq(FuyouRsaKeyConfig::getMchntCd, mchntCd)
                .exists();
    }
}
