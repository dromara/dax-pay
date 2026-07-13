package cn.daxpay.open.channel.alipay.service.isv;

import cn.daxpay.open.channel.alipay.dao.isv.AlipayIsvAppManager;
import cn.daxpay.open.channel.alipay.dao.isv.AlipayIsvChannelMerchantManager;
import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvApp;
import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvChannelMerchant;
import cn.daxpay.open.channel.alipay.param.isv.AlipayIsvAppAuthTokenUpdateParam;
import cn.daxpay.open.channel.alipay.param.isv.AlipayIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.alipay.result.isv.AlipayIsvChannelMerchantResult;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.util.ChannelMchNoGenerateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 支付宝服务商通道商户管理
///
/// 一条记录代表"子商户挂靠在某个服务商应用下"的授权关系。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final AlipayIsvChannelMerchantManager alipayIsvChannelMerchantManager;
    private final AlipayIsvAppManager alipayIsvAppManager;

    /// 创建支付宝服务商通道商户
    @Transactional(rollbackFor = Exception.class)
    public void create(AlipayIsvChannelMerchantCreateParam param) {
        // 校验服务商应用存在(用 isvAppId 系统主键查询)
        var isvApp = alipayIsvAppManager.findById(param.getIsvAppId())
                // 支付宝: 服务商应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        // 校验同一应用下子商户号不重复
        if (alipayIsvChannelMerchantManager.existsByIsvAppIdAndAlipayUserId(
                param.getIsvAppId(), param.getAlipayUserId())) {
            // 支付宝: 该应用下已存在此子商户号
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.subMerchantDuplicate");
        }
        // 生成通道商户号: 通道前缀 + 雪花ID(无分隔符, 仅供排障辨识)
        String channelMchNo = ChannelMchNoGenerateUtil.generate("ALIPAY");
        // 写通用通道商户主表
        var channelMerchant = new ChannelMerchant();
        channelMerchant.setMchNo(param.getMchNo());
        channelMerchant.setChannelMchNo(channelMchNo);
        channelMerchant.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchant.setProduct(param.getProduct());
        channelMerchant.setSource(ChannelMerchantSourceEnum.MANUAL.getCode());
        channelMerchant.setEnable(true);
        channelMerchantManager.save(channelMerchant);
        // 写服务商绑定表(含挂靠关系专属字段)
        var entity = new AlipayIsvChannelMerchant();
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(param.getProduct());
        entity.setIsvAppId(isvApp.getId());
        entity.setAlipayUserId(param.getAlipayUserId());
        entity.setAppAuthToken(param.getAppAuthToken());
        alipayIsvChannelMerchantManager.save(entity);
    }

    /// 根据通道商户号查询服务商通道商户配置
    public AlipayIsvChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        return alipayIsvChannelMerchantManager.lambdaQuery()
                .eq(AlipayIsvChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .map(AlipayIsvChannelMerchant::toResult)
                // 支付宝: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
    }

    /// 更新应用授权令牌
    ///
    /// 手动设置或更新子商户的 app_auth_token, 适用于创建时未填写后续补充,
    /// 或令牌过期/变更后重新绑定的场景。
    @Transactional(rollbackFor = Exception.class)
    public void updateAppAuthToken(AlipayIsvAppAuthTokenUpdateParam param) {
        var entity = alipayIsvChannelMerchantManager.lambdaQuery()
                .eq(AlipayIsvChannelMerchant::getChannelMchNo, param.getChannelMchNo())
                .oneOpt()
                // 支付宝: 通道商户配置不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        entity.setAppAuthToken(param.getAppAuthToken());
        alipayIsvChannelMerchantManager.updateById(entity);
    }
}
