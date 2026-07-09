package cn.daxpay.open.channel.wechat.service.isv;

import cn.daxpay.open.channel.wechat.code.WechatAuthAppTypeEnum;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvChannelMerchantManager;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvChannelMerchant;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvAuthAppTypeUpdateParam;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.wechat.result.isv.WechatIsvChannelMerchantResult;
import cn.daxpay.open.payment.channel.dao.mch.ChannelMerchantManager;
import cn.daxpay.open.payment.channel.entity.mch.ChannelMerchant;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.util.ChannelMchNoGenerateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

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
        // 写服务商绑定表
        var entity = new WechatIsvChannelMerchant();
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(param.getProduct());
        entity.setSubMchId(param.getSubMchId());
        // 认证应用类型: 不传默认 SP_APP(服务商应用), 认证策略据此路由 sp/sub 应用
        if (StrUtil.isNotBlank(param.getAuthAppType())) {
            validateAuthAppType(param.getAuthAppType());
            entity.setAuthAppType(param.getAuthAppType());
        } else {
            entity.setAuthAppType(WechatAuthAppTypeEnum.SP_APP.getCode());
        }
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

    /// 更新认证应用类型
    ///
    /// 控制 OpenId 授权使用服务商应用还是子商户应用。
    @Transactional(rollbackFor = Exception.class)
    public void updateAuthAppType(WechatIsvAuthAppTypeUpdateParam param) {
        validateAuthAppType(param.getAuthAppType());
        var entity = wechatIsvChannelMerchantManager.lambdaQuery()
                .eq(WechatIsvChannelMerchant::getChannelMchNo, param.getChannelMchNo())
                .oneOpt()
                // 微信: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        entity.setAuthAppType(param.getAuthAppType());
        wechatIsvChannelMerchantManager.updateById(entity);
    }

    /// 校验认证应用类型
    private void validateAuthAppType(String authAppType) {
        boolean valid = Arrays.stream(WechatAuthAppTypeEnum.values())
                .anyMatch(item -> item.getCode().equals(authAppType));
        if (!valid) {
            // 微信: 认证应用类型无效
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.authAppTypeInvalid");
        }
    }
}
