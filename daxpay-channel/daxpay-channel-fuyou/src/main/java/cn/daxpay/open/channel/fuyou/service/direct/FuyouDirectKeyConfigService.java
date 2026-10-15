package cn.daxpay.open.channel.fuyou.service.direct;

import cn.daxpay.open.channel.fuyou.convert.direct.FuyouDirectKeyConfigConvert;
import cn.daxpay.open.channel.fuyou.dao.direct.FuyouDirectKeyConfigManager;
import cn.daxpay.open.channel.fuyou.entity.direct.FuyouDirectKeyConfig;
import cn.daxpay.open.channel.fuyou.param.direct.FuyouDirectKeyConfigParam;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 富友直连商户密钥配置
///
/// 管理直连通道商户的商户号 / 商户密钥 / 终端号配置, 密钥行在创建通道商户时预建,
/// 查询回显不存在时不落库(仅返回空壳)。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouDirectKeyConfigService {

    private final FuyouDirectKeyConfigManager fuyouDirectKeyConfigManager;

    /// 根据通道商户号查询密钥配置(仅回显不落库, 不存在时返回空壳)
    public FuyouDirectKeyConfig findByChannelMchNo(String channelMchNo) {
        return fuyouDirectKeyConfigManager.findByChannelMchNo(channelMchNo)
                .orElseGet(() -> new FuyouDirectKeyConfig().setChannelMchNo(channelMchNo));
    }

    /// 支付场景查询密钥配置(必填校验, 不创建记录)
    ///
    /// 记录不存在或关键字段(mchntCd/mchntKey/termNo)任一为空时 fail-fast,
    /// 避免空凭证下发到子应用后子应用才发现问题。
    public FuyouDirectKeyConfig getByChannelMchNoForPay(String channelMchNo) {
        FuyouDirectKeyConfig config = fuyouDirectKeyConfigManager.findByChannelMchNo(channelMchNo)
                // 富友: 直连商户密钥未配置
                .orElseThrow(() -> new BizInfoException("error.channel.fuyou.directKeyNotConfigured"));
        if (StrUtil.hasBlank(config.getMchntCd(), config.getMchntKey(), config.getTermNo())) {
            throw new BizInfoException("error.channel.fuyou.directKeyNotConfigured");
        }
        return config;
    }

    /// 保存直连商户密钥配置(密钥为空时保留原值)
    @Transactional(rollbackFor = Exception.class)
    public void saveConfig(FuyouDirectKeyConfigParam param) {
        FuyouDirectKeyConfig entity = findByChannelMchNo(param.getChannelMchNo());
        FuyouDirectKeyConfigConvert.CONVERT.copy(param, entity);
        if (entity.getId() == null) {
            fuyouDirectKeyConfigManager.save(entity);
        } else {
            fuyouDirectKeyConfigManager.updateById(entity);
        }
    }
}
