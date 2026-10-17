package cn.daxpay.open.channel.fuyou.service.rsa;

import cn.daxpay.open.channel.fuyou.convert.rsa.FuyouRsaKeyConfigConvert;
import cn.daxpay.open.channel.fuyou.dao.rsa.FuyouRsaKeyConfigManager;
import cn.daxpay.open.channel.fuyou.entity.rsa.FuyouRsaKeyConfig;
import cn.daxpay.open.channel.fuyou.param.rsa.FuyouRsaKeyConfigParam;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 富友 RSA 密钥对配置服务
///
/// 管理「互联网直连」(fuyou_internet) 与「H5 聚合收银台」(fuyou_h5) 共用的 RSA 密钥对配置,
/// 密钥行在创建通道商户时预建, 查询回显不存在时不落库(仅返回空壳)。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouRsaKeyConfigService {

    private final FuyouRsaKeyConfigManager fuyouRsaKeyConfigManager;

    /// 根据通道商户号查询密钥配置(仅回显不落库, 不存在时返回空壳)
    public FuyouRsaKeyConfig findByChannelMchNo(String channelMchNo) {
        return fuyouRsaKeyConfigManager.findByChannelMchNo(channelMchNo)
                .orElseGet(() -> new FuyouRsaKeyConfig().setChannelMchNo(channelMchNo));
    }

    /// 支付/回调查询密钥配置(必填校验, 不创建记录)
    ///
    /// 记录不存在或关键字段(mchntCd/privateKey/publicKey)任一为空时 fail-fast,
    /// 避免空凭证下发到子应用后子应用才发现问题。
    public FuyouRsaKeyConfig getByChannelMchNoForPay(String channelMchNo) {
        FuyouRsaKeyConfig config = fuyouRsaKeyConfigManager.findByChannelMchNo(channelMchNo)
                // 富友: 直连商户密钥未配置(互联网/H5 共用直连语义文案)
                .orElseThrow(() -> new BizInfoException("error.channel.fuyou.directKeyNotConfigured"));
        if (StrUtil.hasBlank(config.getMchntCd(), config.getPrivateKey(), config.getPublicKey())) {
            throw new BizInfoException("error.channel.fuyou.directKeyNotConfigured");
        }
        return config;
    }

    /// 保存密钥配置(密钥为空时保留原值)
    @Transactional(rollbackFor = Exception.class)
    public void saveConfig(FuyouRsaKeyConfigParam param) {
        FuyouRsaKeyConfig entity = findByChannelMchNo(param.getChannelMchNo());
        FuyouRsaKeyConfigConvert.CONVERT.copy(param, entity);
        if (entity.getId() == null) {
            fuyouRsaKeyConfigManager.save(entity);
        } else {
            fuyouRsaKeyConfigManager.updateById(entity);
        }
    }
}
