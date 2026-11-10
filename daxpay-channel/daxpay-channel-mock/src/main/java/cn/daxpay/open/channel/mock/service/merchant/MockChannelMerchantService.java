package cn.daxpay.open.channel.mock.service.merchant;

import cn.daxpay.open.channel.mock.dao.isv.MockIsvChannelMerchantManager;
import cn.daxpay.open.channel.mock.entity.isv.MockIsvChannelMerchant;
import cn.daxpay.open.channel.mock.entity.isv.MockIsvInfo;
import cn.daxpay.open.channel.mock.param.MockChannelMerchantCreateParam;
import cn.daxpay.open.channel.mock.service.isv.MockIsvInfoService;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # Mock 模拟通道商户管理
///
/// 创建流程: 纯本地登记——商户档案由运营在模拟通道控制台自行创建, 此处仅登记其通道商户号,
/// 落通用通道商户主表(mch_channel_merchant), 不调用 mock 子应用任何接口
/// (模拟通道为独立通道, 主应用侧无对接接口); 通道侧无对应档案的商户号支付时将被模拟通道拒单。
/// 直连商户级接入密钥(mock_pay_credential)不在此录入, 由密钥端点后置维护。
///
/// 一通道双产品共用本端点: mock_pay 仅写通用主表; mock_isv 额外写一行归属绑定表
/// (mock_isv_channel_merchant, 归属恒为全局唯一模拟服务商), 绑定表仍作为 mock_isv 建档留痕
/// 与通道商户删除时的清理对象(清理由
/// [cn.daxpay.open.channel.mock.strategy.MockIsvChannelMerchantCleanupStrategy] 挂通用删除链承担)。
///
/// 沙箱标记创建时固定为 true: 模拟通道仅 DEV 装配、天然属联调/演示语义, 无生产商户形态。
/// 列表/详情统一走通用通道商户链路(全局列表 + 详情工作台), 模块内不单独提供分页查询。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final MockIsvInfoService mockIsvInfoService;
    private final MockIsvChannelMerchantManager mockIsvChannelMerchantManager;

    /// 创建 Mock 模拟通道商户(纯本地登记)
    ///
    /// product 支持 mock_pay / mock_isv: 前者仅写通用主表; 后者同事务再写一行服务商归属绑定
    /// (isv_id 取全局唯一模拟服务商)。通道商户号为运营在模拟通道控制台创建的商户号, 必填且全局唯一;
    /// 沙箱标记固定为 true(模拟通道无生产语义)。
    /// 运营端写通用通道商户主表/绑定表必须显式 setMchNo(不依赖商户上下文装载)。
    @Transactional(rollbackFor = Exception.class)
    public void create(MockChannelMerchantCreateParam param) {
        boolean isvProduct = ProductEnum.MOCK_ISV.getCode().equals(param.getProduct());
        // 仅模拟通道两产品可登记
        if (!isvProduct && !ProductEnum.MOCK_PAY.getCode().equals(param.getProduct())) {
            // 模拟通道暂不支持该支付产品
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.mock.productNotSupported", param.getProduct());
        }
        // 通道商户号查重(登记值需唯一, 重复登记给出明确提示)
        boolean exists = channelMerchantManager.lambdaQuery()
                .eq(ChannelMerchant::getChannelMchNo, param.getChannelMchNo())
                .exists();
        if (exists) {
            // 该通道商户号已存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.mock.channelMchNoExists", param.getChannelMchNo());
        }
        // 写通用通道商户主表(运营端创建, 商户号显式携带不依赖上下文装载)
        ChannelMerchant channelMerchant = new ChannelMerchant();
        channelMerchant.setMchNo(param.getMchNo());
        channelMerchant.setChannelMchNo(param.getChannelMchNo());
        channelMerchant.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchant.setProduct(param.getProduct());
        channelMerchant.setSource(ChannelMerchantSourceEnum.MANUAL.getCode());
        channelMerchant.setEnable(true);
        // 模拟通道仅 DEV 装配, 天然属沙箱语义, 创建时固定为沙箱商户
        channelMerchant.setSandbox(true);
        channelMerchantManager.save(channelMerchant);
        if (isvProduct) {
            this.saveIsvBinding(param);
        }
    }

    /// 写服务商归属绑定表(归属恒为全局唯一模拟服务商)
    ///
    /// 运营端创建, 商户号必须逐行显式 setMchNo(admin 不装载商户上下文,
    /// 依赖 MchNoMetaObjectHandler 的 Fill 会抛上下文缺失异常)。
    /// 绑定表保留的用途: mock_isv 建档留痕 + 通道商户删除时按通道商户号清理。
    private void saveIsvBinding(MockChannelMerchantCreateParam param) {
        MockIsvInfo isv = mockIsvInfoService.getSingleton();
        MockIsvChannelMerchant binding = new MockIsvChannelMerchant();
        binding.setMchNo(param.getMchNo());
        binding.setChannelMchNo(param.getChannelMchNo());
        binding.setProduct(ProductEnum.MOCK_ISV.getCode());
        binding.setIsvId(isv.getId());
        mockIsvChannelMerchantManager.save(binding);
    }
}
