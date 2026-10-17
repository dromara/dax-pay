package cn.daxpay.open.channel.fuyou.service.rsa;

import cn.daxpay.open.channel.fuyou.dao.rsa.FuyouRsaKeyConfigManager;
import cn.daxpay.open.channel.fuyou.entity.rsa.FuyouRsaKeyConfig;
import cn.daxpay.open.channel.fuyou.param.rsa.FuyouRsaChannelMerchantCreateParam;
import cn.daxpay.open.channel.fuyou.result.rsa.FuyouRsaChannelMerchantResult;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductConfigManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.util.ChannelMchNoGenerateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 富友 RSA 通道商户管理
///
/// 「互联网直连」(fuyou_internet) 与「H5 聚合收银台」(fuyou_h5) 共用的通道商户管理:
/// 商户自己在富友开户, 平台仅登记商户号(免进件), RSA 密钥对由通道商户详情页的密钥配置维护。
/// 同一商户号下同一产品内同一富友商户号只允许绑定一次。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouRsaChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final PayProductConfigManager payProductConfigManager;
    private final FuyouRsaKeyConfigManager fuyouRsaKeyConfigManager;

    /// 创建富友 RSA 通道商户(免进件, 仅登记身份)
    @Transactional(rollbackFor = Exception.class)
    public void create(FuyouRsaChannelMerchantCreateParam param) {
        // 产品范围校验(仅互联网直连/H5 聚合收银台两产品共用本表)
        ProductEnum product = ProductEnum.findByCode(param.getProduct());
        if (product != ProductEnum.FUYOU_INTERNET && product != ProductEnum.FUYOU_H5) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.fuyou.unsupportedPayMethod", param.getProduct());
        }
        // 校验同一商户号同一产品下富友商户号不重复
        if (fuyouRsaKeyConfigManager.existsByMchNoAndProductAndMchntCd(
                param.getMchNo(), param.getProduct(), param.getMchntCd())) {
            // 富友: 该商户下已存在此富友商户号
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.fuyou.merchantDuplicate");
        }
        // 生成通道商户号: 通道前缀 + 雪花ID
        String channelMchNo = ChannelMchNoGenerateUtil.generate("FUYOU");
        // 写通用通道商户主表
        ChannelMerchant channelMerchant = new ChannelMerchant();
        channelMerchant.setMchNo(param.getMchNo());
        channelMerchant.setChannelMchNo(channelMchNo);
        channelMerchant.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchant.setProduct(param.getProduct());
        channelMerchant.setSource(ChannelMerchantSourceEnum.MANUAL.getCode());
        channelMerchant.setEnable(true);
        // 沙箱标记从支付产品生效环境同步写入, 禁止商户/表单设置
        boolean sandbox = payProductConfigManager.isSandboxActive(param.getProduct());
        channelMerchant.setSandbox(sandbox);
        channelMerchantManager.save(channelMerchant);
        // 写 RSA 密钥配置身份行(密钥字段留空, 由密钥配置端点后置维护)
        var keyConfig = new FuyouRsaKeyConfig();
        keyConfig.setMchNo(param.getMchNo());
        keyConfig.setChannelMchNo(channelMchNo);
        keyConfig.setProduct(param.getProduct());
        keyConfig.setSandbox(sandbox);
        keyConfig.setMchntCd(param.getMchntCd());
        fuyouRsaKeyConfigManager.save(keyConfig);
    }

    /// 根据通道商户号查询富友 RSA 通道商户配置
    public FuyouRsaChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        return fuyouRsaKeyConfigManager.findByChannelMchNo(channelMchNo)
                .map(entity -> new FuyouRsaChannelMerchantResult()
                        .setMchNo(entity.getMchNo())
                        .setChannelMchNo(entity.getChannelMchNo())
                        .setProduct(entity.getProduct())
                        .setMchntCd(entity.getMchntCd())
                        .setSandbox(entity.isSandbox()))
                // 富友: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }
}
