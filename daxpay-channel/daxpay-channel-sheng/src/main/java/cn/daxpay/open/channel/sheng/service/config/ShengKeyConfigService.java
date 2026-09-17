package cn.daxpay.open.channel.sheng.service.config;

import cn.daxpay.open.channel.sheng.convert.ShengKeyConfigConvert;
import cn.daxpay.open.channel.sheng.dao.ShengKeyConfigManager;
import cn.daxpay.open.channel.sheng.entity.ShengKeyConfig;
import cn.daxpay.open.channel.sheng.param.ShengKeyConfigParam;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 盛付通通道密钥配置
///
/// 管理通道商户维度的盛付通密钥配置, 查询只读回显(不存在不落库), 保存时加载或创建并合并敏感字段(空值不覆盖)。
/// 盛付通不提供集测接口, 无沙箱双环境, 每通道商户仅一条配置。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengKeyConfigService {

    private final ShengKeyConfigManager shengKeyConfigManager;
    private final ChannelMerchantManager channelMerchantManager;

    /// 根据通道商户号查询密钥配置(只读回显, 不写库)
    ///
    /// 记录不存在时返回未持久化的空配置(仅身份字段) —— 避免 VIEW 权限的 GET 产生写副作用,
    /// 新记录由 [save] 以 MANAGE 权限创建。
    ///
    /// 运营端写入 [cn.daxpay.open.payment.common.entity.MchBaseEntity] 必须显式 setMchNo
    /// (运营端不装载商户 PaymentContext, mchNo 从通用通道商户主表回填)。
    public ShengKeyConfig findByChannelMchNo(String channelMchNo) {
        return shengKeyConfigManager.findByChannelMchNo(channelMchNo)
                .orElseGet(() -> {
                    var config = new ShengKeyConfig()
                            .setChannelMchNo(channelMchNo);
                    // 查询通用通道商户主表填充商户号(单独赋值, 不链式); 只读回显容忍通道商户缺失
                    channelMerchantManager.findByChannelMchNo(channelMchNo)
                            .ifPresent(mch -> config.setMchNo(mch.getMchNo()));
                    return config;
                });
    }

    /// 支付场景查询密钥配置(必填校验, 不创建记录)
    ///
    /// 只读不写: 记录不存在或关键字段(shengMchId/merchantPrivateKey/shengpayPublicKey)任一为空时 fail-fast,
    /// 避免空凭证下发到子应用后子应用才发现问题。
    public ShengKeyConfig getByChannelMchNoForPay(String channelMchNo) {
        ShengKeyConfig config = shengKeyConfigManager.findByChannelMchNo(channelMchNo)
                // 盛付通: 通道密钥未配置
                .orElseThrow(() -> new BizInfoException("error.channel.sheng.keyNotConfigured"));
        if (StrUtil.hasBlank(config.getShengMchId(), config.getMerchantPrivateKey(), config.getShengpayPublicKey())) {
            throw new BizInfoException("error.channel.sheng.keyNotConfigured");
        }
        return config;
    }

    /// 保存密钥配置(不存在则创建)
    ///
    /// 以 channelMchNo 定位记录, 仅更新可编辑字段; 记录不存在时要求通道商户已存在并立即落库;
    /// mchNo/channelMchNo 为不可变身份字段(实体 FieldStrategy.NEVER), 由创建路径从通道商户主表对齐。
    @Transactional(rollbackFor = Exception.class)
    public void save(ShengKeyConfigParam param) {
        var config = shengKeyConfigManager.findByChannelMchNo(param.getChannelMchNo())
                .orElseGet(() -> this.createConfig(param.getChannelMchNo()));
        ShengKeyConfigConvert.CONVERT.copy(param, config);
        shengKeyConfigManager.updateById(config);
    }

    /// 创建并落库新密钥记录(保存流程专用)
    ///
    /// 通道商户不存在时不允许凭空创建密钥记录(mch_no 非空约束转为友好提示);
    /// 运营端写 [cn.daxpay.open.payment.common.entity.MchBaseEntity] 必须显式 setMchNo(单独赋值, 不链式)。
    private ShengKeyConfig createConfig(String channelMchNo) {
        var channelMerchant = channelMerchantManager.findByChannelMchNo(channelMchNo)
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        var config = new ShengKeyConfig()
                .setChannelMchNo(channelMchNo);
        config.setMchNo(channelMerchant.getMchNo());
        shengKeyConfigManager.save(config);
        return config;
    }
}
