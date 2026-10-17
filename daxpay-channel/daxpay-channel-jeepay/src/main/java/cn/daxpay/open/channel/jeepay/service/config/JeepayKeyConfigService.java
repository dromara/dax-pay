package cn.daxpay.open.channel.jeepay.service.config;

import cn.daxpay.open.channel.jeepay.convert.JeepayKeyConfigConvert;
import cn.daxpay.open.channel.jeepay.dao.JeepayKeyConfigManager;
import cn.daxpay.open.channel.jeepay.entity.JeepayKeyConfig;
import cn.daxpay.open.channel.jeepay.param.JeepayKeyConfigParam;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/// # Jeepay通道密钥配置
///
/// 管理通道商户维度的 Jeepay 密钥配置, 查询只读回显(不存在不落库), 保存时加载或创建并合并敏感字段(空值不覆盖)。
/// Jeepay 不提供集测环境, 无沙箱双环境, 每通道商户仅一条配置。
@Slf4j
@Service
@RequiredArgsConstructor
public class JeepayKeyConfigService {

    private final JeepayKeyConfigManager jeepayKeyConfigManager;

    private final ChannelMerchantManager channelMerchantManager;

    /// 根据通道商户号查询密钥配置(只读回显, 不写库)
    ///
    /// 记录不存在时返回未持久化的空配置(仅身份字段) —— 避免 VIEW 权限的 GET 产生写副作用,
    /// 新记录由 [save] 以 MANAGE 权限创建。
    public JeepayKeyConfig findByChannelMchNo(String channelMchNo) {
        return jeepayKeyConfigManager.findByChannelMchNo(channelMchNo)
                .orElseGet(() -> {
                    var config = new JeepayKeyConfig()
                            .setChannelMchNo(channelMchNo);
                    // 查询通用通道商户主表填充商户号(单独赋值, 不链式); 只读回显容忍通道商户缺失
                    channelMerchantManager.findByChannelMchNo(channelMchNo)
                            .ifPresent(mch -> config.setMchNo(mch.getMchNo()));
                    return config;
                });
    }

    /// 支付场景查询密钥配置(必填校验, 不创建记录)
    ///
    /// 只读不写: 记录不存在或关键字段(serverUrl/jeepayMchNo/appId/appSecret)任一为空时 fail-fast,
    /// 避免空凭证下发到子应用后子应用才发现问题。
    public JeepayKeyConfig getByChannelMchNoForPay(String channelMchNo) {
        JeepayKeyConfig config = jeepayKeyConfigManager.findByChannelMchNo(channelMchNo)
                // Jeepay: 通道密钥未配置
                .orElseThrow(() -> new BizInfoException("error.channel.jeepay.keyNotConfigured"));
        if (StrUtil.hasBlank(config.getServerUrl(), config.getJeepayMchNo(),
                config.getAppId(), config.getAppSecret())) {
            throw new BizInfoException("error.channel.jeepay.keyNotConfigured");
        }
        return config;
    }

    /// 保存密钥配置(不存在则创建)
    ///
    /// 以 channelMchNo 定位记录, 仅更新可编辑字段; 记录不存在时要求通道商户已存在并立即落库;
    /// mchNo/channelMchNo 为不可变身份字段(实体 FieldStrategy.NEVER), 由创建路径从通道商户主表对齐;
    /// 平台网关地址去尾斜杠归一; Jeepay 商户号变更时校验同一商户下唯一(防止重复绑定同一上游商户)。
    @Transactional(rollbackFor = Exception.class)
    public void save(JeepayKeyConfigParam param) {
        var config = jeepayKeyConfigManager.findByChannelMchNo(param.getChannelMchNo())
                .orElseGet(() -> this.createConfig(param.getChannelMchNo()));
        // 对接参数归一: 网关地址去尾斜杠, Jeepay 商户号空白转 NULL(空值不覆盖原值)
        param.setServerUrl(StrUtil.removeSuffix(StrUtil.trimToNull(param.getServerUrl()), "/"));
        param.setJeepayMchNo(StrUtil.trimToNull(param.getJeepayMchNo()));
        // 唯一性校验: Jeepay 商户号发生变更时, 同一商户下不允许重复绑定同一 Jeepay 商户号+应用
        if (Objects.nonNull(param.getJeepayMchNo()) && !param.getJeepayMchNo().equals(config.getJeepayMchNo())) {
            boolean duplicated = jeepayKeyConfigManager.lambdaQuery()
                    .eq(JeepayKeyConfig::getMchNo, config.getMchNo())
                    .eq(JeepayKeyConfig::getJeepayMchNo, param.getJeepayMchNo())
                    .eq(Objects.nonNull(param.getAppId()), JeepayKeyConfig::getAppId, param.getAppId())
                    .ne(JeepayKeyConfig::getId, config.getId())
                    .exists();
            if (duplicated) {
                // Jeepay: 该商户下已存在此 Jeepay 商户号+应用配置
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.jeepay.mchDuplicate");
            }
        }
        JeepayKeyConfigConvert.CONVERT.copy(param, config);
        jeepayKeyConfigManager.updateById(config);
    }

    /// 创建并落库新密钥记录(保存流程专用)
    ///
    /// 通道商户不存在时不允许凭空创建密钥记录(mch_no 非空约束转为友好提示);
    /// 运营端写 [cn.daxpay.open.payment.common.entity.MchBaseEntity] 必须显式 setMchNo(单独赋值, 不链式)。
    private JeepayKeyConfig createConfig(String channelMchNo) {
        var channelMerchant = channelMerchantManager.findByChannelMchNo(channelMchNo)
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        var config = new JeepayKeyConfig()
                .setChannelMchNo(channelMchNo);
        config.setMchNo(channelMerchant.getMchNo());
        jeepayKeyConfigManager.save(config);
        return config;
    }
}
