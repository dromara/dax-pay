package cn.daxpay.open.channel.mock.service.isv;

import cn.daxpay.open.channel.mock.dao.isv.MockIsvChannelMerchantManager;
import cn.daxpay.open.channel.mock.dao.isv.MockIsvInfoManager;
import cn.daxpay.open.channel.mock.entity.isv.MockIsvChannelMerchant;
import cn.daxpay.open.channel.mock.entity.isv.MockIsvInfo;
import cn.daxpay.open.channel.mock.param.isv.MockIsvChannelMerchantCreateParam;
import cn.daxpay.open.channel.mock.result.isv.MockIsvChannelMerchantResult;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.channel.ChannelMerchantSourceEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.util.ChannelMchNoGenerateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # Mock 服务商通道商户管理
///
/// 服务商形态通道商户(mock_isv 产品)建档: 纯本地登记——商户档案由运营在模拟通道控制台
/// 自行创建, 此处登记其通道商户号, 不调用 mock 子应用任何接口;
/// 通道侧无对应档案(或归属不一致)的商户号支付时将被拒单(真实通道同构形态)。
/// 双表写入(通用主表 mch_channel_merchant + 私有绑定表 mock_isv_channel_merchant)。
///
/// open 版通用主表无 isv_no 列, 服务商归属只落私有绑定表, 由
/// [MockIsvConfigAssembler] 按通道商户号反解; 运营端建档必须逐行显式 setMchNo。
///
/// 通道商户删除时的私有表清理由
/// [cn.daxpay.open.channel.mock.strategy.MockIsvChannelMerchantCleanupStrategy] 挂通用删除链承担。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockIsvChannelMerchantService {

    private final ChannelMerchantManager channelMerchantManager;
    private final MockIsvChannelMerchantManager mockIsvChannelMerchantManager;
    private final MockIsvInfoManager mockIsvInfoManager;

    /// 创建 Mock 服务商通道商户(纯本地登记, 双表建档)
    ///
    /// 流程: 校验归属服务商 → 子商户号(空白按 MOCK 前缀自动生成) → 通道商户号本地查重
    /// → 同事务双写通用主表与私有绑定表。
    /// 通用主表: product=mock_isv / channelMchNo=登记值或生成值 / sandbox=true / source=MANUAL;
    /// 私有绑定表: mchNo(显式)/product/isvId。
    /// 不调用 mock 子应用: 通道侧无对应档案的商户号支付时将被拒单(真实通道同构形态)。
    @Transactional(rollbackFor = Exception.class)
    public void create(MockIsvChannelMerchantCreateParam param) {
        // 校验归属模拟服务商存在且启用
        MockIsvInfo isv = mockIsvInfoManager.findById(param.getIsvId())
                // mock: 归属的模拟服务商不存在
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.mock.isvNotExist", param.getIsvId()));
        if (!Boolean.TRUE.equals(isv.getEnable())) {
            // 归属模拟服务商已停用, 不可创建通道商户
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.mock.isvDisabled");
        }
        // 子商户号: 显式登记优先, 空白按 MOCK 前缀 + 雪花ID 生成(与直连建档一致)
        String channelMchNo = StrUtil.isNotBlank(param.getChannelMchNo())
                ? param.getChannelMchNo()
                : ChannelMchNoGenerateUtil.generate("MOCK");
        // 通道商户号查重(登记值需唯一, 重复登记给出明确提示)
        boolean channelMchNoExists = channelMerchantManager.lambdaQuery()
                .eq(ChannelMerchant::getChannelMchNo, channelMchNo)
                .exists();
        if (channelMchNoExists) {
            // 该通道商户号已存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.mock.channelMchNoExists", channelMchNo);
        }
        // 双表建档(通道商户号为登记值或生成值)
        this.saveChannelMerchant(param, channelMchNo);
        this.saveBinding(isv, channelMchNo, param.getMchNo());
    }

    /// 查询服务商通道商户绑定(详情工作台基本信息展示用)
    ///
    /// 绑定行带出归属服务商编号/名称(基本信息卡片展示), 绑定行不存在视为数据异常。
    public MockIsvChannelMerchantResult findByChannelMchNo(String channelMchNo) {
        MockIsvChannelMerchant binding = mockIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // mock: 模拟服务商通道商户绑定不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.mock.isvBindingNotExist", channelMchNo));
        MockIsvChannelMerchantResult result = binding.toResult();
        mockIsvInfoManager.findById(binding.getIsvId()).ifPresent(isv -> {
            result.setIsvNo(isv.getIsvNo());
            result.setIsvName(isv.getIsvName());
        });
        return result;
    }

    /// 写通用通道商户主表
    ///
    /// 运营端创建, 商户号显式携带不依赖商户上下文装载(setMchNo 链式返回父类, 单独调用)。
    private void saveChannelMerchant(MockIsvChannelMerchantCreateParam param, String channelMchNo) {
        ChannelMerchant channelMerchant = new ChannelMerchant();
        channelMerchant.setMchNo(param.getMchNo());
        channelMerchant.setChannelMchNo(channelMchNo);
        channelMerchant.setChannelMerchantName(param.getChannelMerchantName());
        channelMerchant.setProduct(ProductEnum.MOCK_ISV.getCode());
        channelMerchant.setSource(ChannelMerchantSourceEnum.MANUAL.getCode());
        channelMerchant.setEnable(true);
        // 模拟通道天然属沙箱语义, 创建时固定为沙箱商户
        channelMerchant.setSandbox(true);
        channelMerchantManager.save(channelMerchant);
    }

    /// 写服务商归属绑定表
    ///
    /// 运营端创建, 商户号必须逐行显式 setMchNo(admin 不装载商户上下文,
    /// 依赖 MchNoMetaObjectHandler 的 Fill 会抛上下文缺失异常)。
    private void saveBinding(MockIsvInfo isv, String channelMchNo, String mchNo) {
        MockIsvChannelMerchant entity = new MockIsvChannelMerchant();
        entity.setMchNo(mchNo);
        entity.setChannelMchNo(channelMchNo);
        entity.setProduct(ProductEnum.MOCK_ISV.getCode());
        entity.setIsvId(isv.getId());
        mockIsvChannelMerchantManager.save(entity);
    }
}
