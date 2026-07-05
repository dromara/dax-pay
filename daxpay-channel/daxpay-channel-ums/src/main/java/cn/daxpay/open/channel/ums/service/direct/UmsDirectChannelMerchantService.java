package cn.daxpay.open.channel.ums.service.direct;

import cn.daxpay.open.channel.ums.dao.direct.UmsDirectChannelMerchantManager;
import cn.daxpay.open.channel.ums.entity.direct.UmsDirectChannelMerchant;
import cn.daxpay.open.channel.ums.param.direct.UmsDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.ums.param.direct.UmsDirectKeyConfigParam;
import cn.daxpay.open.channel.ums.result.direct.UmsDirectChannelMerchantResult;
import cn.daxpay.open.payment.channel.dao.mch.ChannelMerchantManager;
import cn.daxpay.open.payment.channel.entity.mch.ChannelMerchant;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 银联商务直连通道商户管理
///
/// 管理通道商户绑定的创建/查询/删除。
/// 银联商务签名无证书, 创建时需同时写入密钥配置(umsAppId/appKey/secretKey)。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmsDirectChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final UmsDirectChannelMerchantManager umsDirectChannelMerchantManager;
    private final UmsDirectKeyConfigService umsDirectKeyConfigService;

    /// 创建通道商户绑定
    ///
    /// 同时写入通用通道商户主表、银联商务直连绑定表和密钥配置表。
    @Transactional(rollbackFor = Exception.class)
    public void create(UmsDirectChannelMerchantCreateParam param) {
        // 生成通道商户号(雪花号)
        String channelMchNo = String.valueOf(IdUtil.getSnowflakeNextId());
        // 写通用通道商户主表
        ChannelMerchant channelMerchant = new ChannelMerchant();
        channelMerchant.setMchNo(param.getMchNo());
        channelMerchant.setChannelMchNo(channelMchNo);
        channelMerchant.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchant.setProduct(param.getProduct());
        channelMerchant.setSource(ChannelMerchantSourceEnum.MANUAL.getCode());
        channelMerchant.setEnable(true);
        channelMerchantManager.save(channelMerchant);
        // 写银联商务直连绑定表
        var entity = new UmsDirectChannelMerchant();
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(param.getProduct());
        entity.setMerchantNo(param.getMerchantNo());
        entity.setTerminalNo(param.getTerminalNo());
        entity.setOrderPrefix(param.getOrderPrefix());
        entity.setSandbox(param.isSandbox());
        umsDirectChannelMerchantManager.save(entity);
        // 同时保存密钥配置(umsAppId/appKey/secretKey)
        var keyParam = new UmsDirectKeyConfigParam();
        keyParam.setChannelMchNo(channelMchNo);
        keyParam.setMchNo(param.getMchNo());
        keyParam.setUmsAppId(param.getUmsAppId());
        keyParam.setAppKey(param.getAppKey());
        keyParam.setSecretKey(param.getSecretKey());
        umsDirectKeyConfigService.save(keyParam);
    }

    /// 根据通道商户号查询银联商务直连通道商户配置
    public UmsDirectChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        return umsDirectChannelMerchantManager.findByChannelMchNo(channelMchNo)
                .map(UmsDirectChannelMerchant::toResult)
                // 通道: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }

    /// 根据通道商户号删除(级联删除密钥)
    @Transactional(rollbackFor = Exception.class)
    public void deleteByChannelMchNo(String channelMchNo) {
        umsDirectKeyConfigService.deleteByChannelMchNo(channelMchNo);
        umsDirectChannelMerchantManager.lambdaUpdate()
                .eq(UmsDirectChannelMerchant::getChannelMchNo, channelMchNo)
                .remove();
    }
}
