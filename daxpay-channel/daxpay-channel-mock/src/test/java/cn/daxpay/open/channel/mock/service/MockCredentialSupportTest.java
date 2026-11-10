package cn.daxpay.open.channel.mock.service;

import cn.daxpay.open.channel.mock.client.credential.MockSdkCredential;
import cn.daxpay.open.channel.mock.entity.credential.MockGatewayCredential;
import cn.daxpay.open.channel.mock.entity.credential.MockPayCredential;
import cn.daxpay.open.channel.mock.service.credential.MockChannelCredentialService;
import cn.daxpay.open.channel.mock.service.credential.MockDirectCredentialService;
import cn.daxpay.open.channel.mock.service.isv.MockIsvConfigAssembler;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/// # Mock 通道凭证装配分发测试
///
/// 覆盖三分支: 服务商产品直接使用全局单份密钥(isvNo 回填主体编号, 仅归属留痕) /
/// 直连商户级必配(未配置报错, 不回落全局) / 无通道商户号的全局兜底与 fail-fast。
@ExtendWith(MockitoExtension.class)
class MockCredentialSupportTest {

    private static final String ISV_NO = "MISV1000000000001";
    private static final String CHANNEL_MCH_NO = "MOCK-CM-001";

    @Mock
    private ChannelMerchantManager channelMerchantManager;
    @Mock
    private MockIsvConfigAssembler mockIsvConfigAssembler;
    @Mock
    private MockChannelCredentialService mockChannelCredentialService;
    @Mock
    private MockDirectCredentialService mockDirectCredentialService;

    private MockCredentialSupport newSupport() {
        return new MockCredentialSupport(channelMerchantManager, mockIsvConfigAssembler,
                mockChannelCredentialService, mockDirectCredentialService);
    }

    private MockGatewayCredential globalCredential() {
        return new MockGatewayCredential()
                .setMerchantPrivateKey("GLOBAL-PRIVATE")
                .setPlatformPublicKey("GLOBAL-PLATFORM-PUB");
    }

    private void stubIsvAssembler() {
        // MockSdkCredential 无链式 setter, 逐字段赋值
        MockSdkCredential assembled = new MockSdkCredential();
        assembled.setIsvNo(ISV_NO);
        assembled.setSandbox(true);
        when(mockIsvConfigAssembler.buildConfig()).thenReturn(assembled);
    }

    @Test
    @DisplayName("服务商产品: 直接使用全局单份密钥, 主体编号回填服务商编号, 网关地址为空")
    void build_isvProduct_shouldUseGlobalCredential() {
        stubIsvAssembler();
        when(mockChannelCredentialService.getRequiredConfigured()).thenReturn(globalCredential());

        MockSdkCredential credential = newSupport().build(ProductEnum.MOCK_ISV.getCode(), CHANNEL_MCH_NO);

        assertEquals("GLOBAL-PRIVATE", credential.getMerchantPrivateKey());
        assertEquals("GLOBAL-PLATFORM-PUB", credential.getPlatformPublicKey());
        // isvNo 仍保留: mock 网关按 isv_no 匹配商户档案(协议字段)
        assertEquals(ISV_NO, credential.getIsvNo());
        // 全局密钥: 主体编号回填服务商编号(网关按全局公钥验签, key_owner_no 仅做归属留痕)
        assertEquals(ISV_NO, credential.getKeyOwnerNo());
        assertNull(credential.getGatewayBaseUrl());
        verify(mockDirectCredentialService, never()).findUsable(CHANNEL_MCH_NO);
    }

    @Test
    @DisplayName("服务商产品 + 全局密钥未配置: fail-fast 不静默下发空密钥")
    void build_isvProductWithoutGlobalCredential_shouldFailFast() {
        stubIsvAssembler();
        when(mockChannelCredentialService.getRequiredConfigured())
                .thenThrow(new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.mock.globalCredentialNotConfigured"));

        assertThrows(BizInfoException.class,
                () -> newSupport().build(ProductEnum.MOCK_ISV.getCode(), CHANNEL_MCH_NO));
    }

    @Test
    @DisplayName("直连产品 + 商户级两件套齐: 整行下发, 主体编号取通道商户号")
    void build_directProductWithCompleteMchCredential_shouldUseRowValues() {
        when(mockDirectCredentialService.findUsable(CHANNEL_MCH_NO)).thenReturn(new MockPayCredential()
                .setChannelMchNo(CHANNEL_MCH_NO)
                .setMerchantPrivateKey("MCH-PRIVATE")
                .setPlatformPublicKey("MCH-PLATFORM-PUB")
                .setGatewayBaseUrl("http://10.0.0.8:10400"));

        MockSdkCredential credential = newSupport().build(ProductEnum.MOCK_PAY.getCode(), CHANNEL_MCH_NO);

        assertEquals("MCH-PRIVATE", credential.getMerchantPrivateKey());
        assertEquals("MCH-PLATFORM-PUB", credential.getPlatformPublicKey());
        assertEquals(CHANNEL_MCH_NO, credential.getKeyOwnerNo());
        assertEquals("http://10.0.0.8:10400", credential.getGatewayBaseUrl());
        assertNull(credential.getIsvNo());
        verify(mockIsvConfigAssembler, never()).buildConfig();
        verify(mockChannelCredentialService, never()).getRequiredConfigured();
    }

    @Test
    @DisplayName("直连产品 + 商户级未配置: 明确报错, 不回落全局单行")
    void build_directProductWithoutMchCredential_shouldThrow() {
        when(mockDirectCredentialService.findUsable(CHANNEL_MCH_NO)).thenReturn(null);

        assertThrows(BizInfoException.class,
                () -> newSupport().build(ProductEnum.MOCK_PAY.getCode(), CHANNEL_MCH_NO));
        verify(mockChannelCredentialService, never()).getRequiredConfigured();
    }

    @Test
    @DisplayName("直连产品 + product 缺失容错: 同样走商户级拟真凭证优先")
    void build_productMissing_shouldApplyMchCredential() {
        when(mockDirectCredentialService.findUsable(CHANNEL_MCH_NO)).thenReturn(new MockPayCredential()
                .setChannelMchNo(CHANNEL_MCH_NO)
                .setMerchantPrivateKey("MCH-PRIVATE")
                .setPlatformPublicKey("MCH-PLATFORM-PUB"));

        MockSdkCredential credential = newSupport().build(null, CHANNEL_MCH_NO);

        assertEquals("MCH-PRIVATE", credential.getMerchantPrivateKey());
        assertEquals("MCH-PLATFORM-PUB", credential.getPlatformPublicKey());
        assertEquals(CHANNEL_MCH_NO, credential.getKeyOwnerNo());
        assertNull(credential.getGatewayBaseUrl());
    }

    @Test
    @DisplayName("无通道商户号(无 isvNo): 全局单行兜底(未配置 fail-fast)")
    void build_withoutChannelMchNo_shouldFallBackGlobal() {
        when(mockChannelCredentialService.getRequiredConfigured()).thenReturn(globalCredential());

        MockSdkCredential credential = newSupport().build(null, null);

        assertEquals("GLOBAL-PRIVATE", credential.getMerchantPrivateKey());
        assertEquals("GLOBAL-PLATFORM-PUB", credential.getPlatformPublicKey());
        assertNull(credential.getKeyOwnerNo());
        assertNull(credential.getIsvNo());
    }

    @Test
    @DisplayName("按通道商户号反查产品: product=mock_isv 时走服务商装配 + 全局密钥")
    void build_byChannelMchNo_shouldResolveProduct() {
        when(channelMerchantManager.findProductByChannelMchNo(CHANNEL_MCH_NO))
                .thenReturn(ProductEnum.MOCK_ISV.getCode());
        stubIsvAssembler();
        when(mockChannelCredentialService.getRequiredConfigured()).thenReturn(globalCredential());

        MockSdkCredential credential = newSupport().build(CHANNEL_MCH_NO);

        assertEquals(ISV_NO, credential.getIsvNo());
        assertEquals("GLOBAL-PRIVATE", credential.getMerchantPrivateKey());
        assertEquals("GLOBAL-PLATFORM-PUB", credential.getPlatformPublicKey());
        // 主体编号回填服务商编号(网关按全局公钥验签, key_owner_no 仅做归属留痕)
        assertEquals(ISV_NO, credential.getKeyOwnerNo());
    }
}
