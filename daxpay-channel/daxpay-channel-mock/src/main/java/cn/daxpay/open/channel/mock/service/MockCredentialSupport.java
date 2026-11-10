package cn.daxpay.open.channel.mock.service;

import cn.daxpay.open.channel.mock.client.credential.MockSdkCredential;
import cn.daxpay.open.channel.mock.entity.credential.MockGatewayCredential;
import cn.daxpay.open.channel.mock.service.credential.MockChannelCredentialService;
import cn.daxpay.open.channel.mock.service.credential.MockDirectCredentialService;
import cn.daxpay.open.channel.mock.service.isv.MockIsvConfigAssembler;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Mock 通道凭证装配分发
///
/// mock 通道一通道双产品(mock_pay 直连 / mock_isv 服务商), 底层 Service 共用,
/// 组装通道凭证时按 product 二选一(统一收口在本类, 各 Service 不各自分支):
/// - 直连产品(mock_pay): 零资料空壳业务字段(sandbox=true);
/// - 服务商产品(mock_isv): 走 [MockIsvConfigAssembler] 回填全局唯一模拟服务商的 isvNo,
///   sandbox=true(isv_no 为网关协议字段, 必填; 网关按它匹配商户档案)。
///
/// 两分支产出后统一注入网关接入密钥, 供 one 子应用 mock 模块签名请求/验签响应:
/// - 直连产品: 商户级拟真凭证必配([MockDirectCredentialService#findUsable], 商户私钥+平台公钥
///   两件套齐才整行下发——主体编号取通道商户号, one 侧随请求报文/查询串以 key_owner_no 携带,
///   模拟网关按主体编号定位登记公钥, gatewayBaseUrl 供 one 侧优先选址), 未配置时交易报错;
/// - 服务商产品: 直接使用全局单份接入密钥([MockChannelCredentialService#getRequiredConfigured]),
///   主体编号 key_owner_no 不设置(按全局公钥验签), isvNo 仅留痕供网关匹配商户档案;
/// - 无通道商户号场景(如尚无通道商户号的进件链路): 全局单行兜底, 未配置 fail-fast。
///
/// 注意: 分账链路经本类按订单 product 正确分支([cn.daxpay.open.channel.mock.service.MockAllocService]),
/// 不照抄真实通道「分账固定走 Direct assembler」的非对称缺陷。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockCredentialSupport {

    private final ChannelMerchantManager channelMerchantManager;
    private final MockIsvConfigAssembler mockIsvConfigAssembler;
    private final MockChannelCredentialService mockChannelCredentialService;
    private final MockDirectCredentialService mockDirectCredentialService;

    /// 按产品组装通道调用凭证(订单实体已携带 product 时优先用本方法, 免反查)
    ///
    /// @param product      支付产品编码(订单固化值; mock_pay/mock_isv, 空或未知按直连空壳处理)
    /// @param channelMchNo 通道商户号(直连商户级凭证定位主键)
    public MockSdkCredential build(String product, String channelMchNo) {
        MockSdkCredential credential;
        if (ProductEnum.MOCK_ISV.getCode().equals(product)) {
            // 服务商产品: 回填全局唯一模拟服务商的 isvNo
            credential = mockIsvConfigAssembler.buildConfig();
        } else {
            // 直连产品(含 product 缺失容错): 零资料空壳业务字段
            credential = new MockSdkCredential();
        }
        applyGatewayCredentials(credential, channelMchNo);
        return credential;
    }

    /// 按通道商户号反查产品后组装(订单实体不携带 product 的场景, 如支付单 PayTrade)
    ///
    /// 通道商户号全局唯一, 其 product 与订单 product 同源(订单创建时自通道商户固化)。
    public MockSdkCredential build(String channelMchNo) {
        return this.build(channelMerchantManager.findProductByChannelMchNo(channelMchNo), channelMchNo);
    }

    /// 注入网关接入密钥(按 isvNo/通道商户号三分支: ISV 服务商 → 直连交易 → 无通道商户号兜底)
    ///
    /// 服务商分流: isvNo 非空时直接使用全局单份接入密钥(主体编号回填服务商编号,
    /// mock 网关按全局公钥验签, key_owner_no 仅做归属留痕);
    /// 直连交易分流: channelMchNo 非空白时商户级配置必配
    /// ([MockDirectCredentialService#findUsable], 主体编号取通道商户号), 未配置明确报错(不回落全局);
    /// 无通道商户号场景: 全局单行兜底(未配置 fail-fast)。
    private void applyGatewayCredentials(MockSdkCredential credential, String channelMchNo) {
        if (StrUtil.isNotBlank(credential.getIsvNo())) {
            // 服务商产品: 全局单份密钥, 主体编号回填服务商编号(网关按全局公钥验签, 归属留痕)
            MockGatewayCredential global = mockChannelCredentialService.getRequiredConfigured();
            credential.setMerchantPrivateKey(global.getMerchantPrivateKey());
            credential.setPlatformPublicKey(global.getPlatformPublicKey());
            credential.setKeyOwnerNo(credential.getIsvNo());
            return;
        }
        if (StrUtil.isNotBlank(channelMchNo)) {
            // 直连交易: 商户级必配, 未配置明确报错(不回落全局)
            var directCredential = mockDirectCredentialService.findUsable(channelMchNo);
            if (directCredential == null) {
                // 模拟直连商户未配置对接配置(商户私钥/平台公钥)
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.mock.directCredentialNotConfigured", channelMchNo);
            }
            credential.setMerchantPrivateKey(directCredential.getMerchantPrivateKey());
            credential.setPlatformPublicKey(directCredential.getPlatformPublicKey());
            // 主体编号取通道商户号: one 侧随报文/查询串携带 key_owner_no, 模拟网关按主体编号定位登记公钥
            credential.setKeyOwnerNo(channelMchNo);
            credential.setGatewayBaseUrl(directCredential.getGatewayBaseUrl());
            return;
        }
        // 无通道商户号场景: 全局单行兜底(未配置 fail-fast)
        MockGatewayCredential global = mockChannelCredentialService.getRequiredConfigured();
        credential.setMerchantPrivateKey(global.getMerchantPrivateKey());
        credential.setPlatformPublicKey(global.getPlatformPublicKey());
    }
}
