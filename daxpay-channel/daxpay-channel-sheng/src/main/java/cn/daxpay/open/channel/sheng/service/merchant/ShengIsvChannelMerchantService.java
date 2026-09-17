package cn.daxpay.open.channel.sheng.service.merchant;

import cn.daxpay.open.channel.sheng.dao.ShengIsvChannelMerchantManager;
import cn.daxpay.open.channel.sheng.entity.ShengIsvChannelMerchant;
import cn.daxpay.open.channel.sheng.param.ShengIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.sheng.result.ShengIsvChannelMerchantResult;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductConfigManager;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.util.ChannelMchNoGenerateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 盛付通服务商通道商户管理
///
/// 创建时写通用通道商户主表 + sheng_isv_channel_merchant 子商户绑定行;
/// 服务商签名密钥不在此录入, 由产品级密钥配置全局统一维护。
/// 同一商户下子商户号唯一, 重复创建直接拒绝。
/// 盛付通无沙箱环境, 沙箱标记仍按支付产品生效环境读取(实际恒为 false, 与商户模式同款写法)。
///
/// 通道商户删除时的绑定行清理由独立的策略类
/// [cn.daxpay.open.channel.sheng.strategy.merchant.ShengIsvChannelMerchantCleanupStrategy] 承担
/// (服务商密钥为产品级共享, 不随通道商户删除)。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengIsvChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final PayProductConfigManager payProductConfigManager;
    private final ShengIsvChannelMerchantManager shengIsvChannelMerchantManager;

    /// 创建盛付通服务商通道商户(子商户绑定)
    ///
    /// 写表顺序: 通用通道商户主表(mch_channel_merchant) → 子商户绑定行(sheng_isv_channel_merchant)。
    @Transactional(rollbackFor = Exception.class)
    public void create(ShengIsvChannelMerchantCreateParam param) {
        // 子商户号必填, 空串归一为 NULL(前端未填时可能上送空串), 保证唯一性判断与落库语义一致
        String subMchId = StrUtil.trimToNull(param.getSubMchId());
        String sdpAppId = StrUtil.trimToNull(param.getSdpAppId());
        // 唯一性校验: 同一商户下子商户号不重复(列 NOT NULL, 归一后 eq 精确匹配即可)
        boolean exists = shengIsvChannelMerchantManager.lambdaQuery()
                .eq(ShengIsvChannelMerchant::getMchNo, param.getMchNo())
                .eq(ShengIsvChannelMerchant::getSubMchId, subMchId)
                .exists();
        if (exists) {
            // 盛付通服务商: 该商户下已存在此子商户号
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.sheng.isvMchDuplicate");
        }
        // 生成通道商户号: 通道前缀 + 雪花ID(与商户模式共用 SHENG 前缀, 两产品同属盛付通通道)
        String channelMchNo = ChannelMchNoGenerateUtil.generate("SHENG");
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
        // 写子商户绑定行, 子商户身份随创建录入, 服务商密钥由产品级密钥配置全局统一维护
        var bindRow = new ShengIsvChannelMerchant()
                .setChannelMchNo(channelMchNo)
                .setProduct(param.getProduct())
                .setSubMchId(subMchId)
                .setSdpAppId(sdpAppId);
        // 运营端写入必须显式 setMchNo(父类 setter 返回父类型, 不链式)
        bindRow.setMchNo(param.getMchNo());
        shengIsvChannelMerchantManager.save(bindRow);
    }

    /// 根据通道商户号查询子商户绑定(通道商户详情页基本信息卡数据源)
    ///
    /// 只读查询, 绑定行不存在时返回 null(调用方按未配置降级展示)。
    public ShengIsvChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        return shengIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                .map(ShengIsvChannelMerchant::toResult)
                .orElse(null);
    }
}
