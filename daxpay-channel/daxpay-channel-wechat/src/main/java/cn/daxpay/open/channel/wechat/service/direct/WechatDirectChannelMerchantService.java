package cn.daxpay.open.channel.wechat.service.direct;

import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectChannelMerchantManager;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectChannelMerchant;
import cn.daxpay.open.channel.wechat.param.direct.WechatDirectChannelMerchantCreateParam;
import cn.daxpay.open.channel.wechat.result.direct.WechatDirectChannelMerchantResult;
import cn.daxpay.open.payment.channel.dao.ChannelMerchantManager;
import cn.daxpay.open.payment.channel.entity.ChannelMerchant;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.util.ChannelMchNoGenerateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 微信直连通道商户管理
///
/// 一个微信商户号(wxMchId)对应一个 channelMchNo, 商户的多个应用共享此绑定。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatDirectChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final WechatDirectChannelMerchantManager wechatDirectChannelMerchantManager;

    /// 创建微信直连通道商户
    @Transactional(rollbackFor = Exception.class)
    public void create(WechatDirectChannelMerchantCreateParam param) {
        // 校验同一商户下微信商户号不重复
        if (wechatDirectChannelMerchantManager.existsByMchNoAndWxMchId(
                param.getMchNo(), param.getWxMchId())) {
            // 微信: 同一商户下该微信商户已存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.directMchDuplicate");
        }
        // 生成通道商户号: 通道前缀 + 雪花ID(无分隔符, 仅供排障辨识)
        String channelMchNo = ChannelMchNoGenerateUtil.generate("WECHAT");
        // 写通用通道商户主表
        ChannelMerchant channelMerchant = new ChannelMerchant();
        channelMerchant.setMchNo(param.getMchNo());
        channelMerchant.setChannelMchNo(channelMchNo);
        channelMerchant.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchant.setProduct(param.getProduct());
        channelMerchant.setSource(ChannelMerchantSourceEnum.MANUAL.getCode());
        channelMerchant.setEnable(true);
        channelMerchantManager.save(channelMerchant);
        // 写直连绑定表(wxMchId 作为业务字段, 不参与关联)
        var entity = new WechatDirectChannelMerchant();
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(param.getProduct());
        entity.setWxMchId(param.getWxMchId());
        wechatDirectChannelMerchantManager.save(entity);
    }

    /// 根据通道商户号查询直连通道商户配置
    public WechatDirectChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        return wechatDirectChannelMerchantManager.lambdaQuery()
                .eq(WechatDirectChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .map(WechatDirectChannelMerchant::toResult)
                // 微信: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }
}
