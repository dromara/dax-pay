package cn.daxpay.open.channel.wechat.service.isv;

import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvChannelMerchantManager;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvChannelMerchant;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvChannelMerchantResult;
import cn.daxpay.open.payment.channel.dao.mch.ChannelMerchantManager;
import cn.daxpay.open.payment.channel.entity.mch.ChannelMerchant;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 微信服务商通道商户管理
///
/// 微信特约商户关联到服务商本身(服务商密钥全局唯一), 不挂靠具体服务商应用。
/// 一个商户下同一特约商户号(subMchId)只允许绑定一次。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final WechatIsvChannelMerchantManager wechatIsvChannelMerchantManager;

    /// 创建微信服务商通道商户
    @Transactional(rollbackFor = Exception.class)
    public void create(WechatIsvChannelMerchantCreateParam param) {
        // 校验同一商户下特约商户号不重复
        if (wechatIsvChannelMerchantManager.existsByMchNoAndSubMchId(
                param.getMchNo(), param.getSubMchId())) {
            // 微信: 该服务商应用下已存在此特约商户号
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.subMerchantDuplicate");
        }
        // 生成通道商户号：前缀 WISV + 雪花ID(无分隔符, 符合 TradeNoGenerateUtil 约定)
        String channelMchNo = "WISV" + IdUtil.getSnowflakeNextId();
        // 写通用通道商户主表
        ChannelMerchant channelMerchant = new ChannelMerchant();
        channelMerchant.setMchNo(param.getMchNo());
        channelMerchant.setChannelMchNo(channelMchNo);
        channelMerchant.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchant.setProduct(param.getProduct());
        channelMerchant.setSource(ChannelMerchantSourceEnum.MANUAL.getCode());
        channelMerchant.setEnable(true);
        channelMerchantManager.save(channelMerchant);
        // 写服务商绑定表
        var entity = new WechatIsvChannelMerchant();
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(param.getProduct());
        entity.setSubMchId(param.getSubMchId());
        wechatIsvChannelMerchantManager.save(entity);
    }

    /// 根据通道商户号查询服务商通道商户配置
    public WechatIsvChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        return wechatIsvChannelMerchantManager.lambdaQuery()
                .eq(WechatIsvChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .map(WechatIsvChannelMerchant::toResult)
                // 微信: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }
}
