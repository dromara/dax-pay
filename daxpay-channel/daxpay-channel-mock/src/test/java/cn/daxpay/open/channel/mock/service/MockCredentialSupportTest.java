package cn.daxpay.open.channel.mock.service;

import cn.daxpay.open.channel.mock.client.credential.MockSdkCredential;
import cn.daxpay.open.channel.mock.entity.credential.MockChannelCredential;
import cn.daxpay.open.channel.mock.entity.credential.MockDirectCredential;
import cn.daxpay.open.channel.mock.entity.credential.MockIsvCredential;
import cn.daxpay.open.channel.mock.service.credential.MockChannelCredentialService;
import cn.daxpay.open.channel.mock.service.credential.MockDirectCredentialService;
import cn.daxpay.open.channel.mock.service.credential.MockIsvCredentialService;
import cn.daxpay.open.channel.mock.service.isv.MockIsvConfigAssembler;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
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
/// 覆盖三分支: 服务商级两件套整行下发(主体编号取服务商编号) / 服务商级未配置回落全局单行 /
/// 直连商户级必配(未配置报错, 不回落全局); 另覆盖无通道商户号的全局兜底与 fail-fast。
@ExtendWith(MockitoExtension.class)
class MockCredentialSupportTest {

    private static final String ISV_NO = "MISV1234567890123456";
    private static final String CHANNEL_MCH_NO = "MOCK-CM-001";

    @Mock
    private ChannelMerchantManager channelMerchantManager;
    @Mock
    private MockIsvConfigAssembler mockIsvConfigAssembler;
    @Mock
    private MockChannelCredentialService mockChannelCredentialService;
    @Mock
    private MockIsvCredentialService mockIsvCredentialService;
    @Mock
    private MockDirectCredentialService mockDirectCredentialService;

    private MockCredentialSupport newSupport() {
        return new MockCredentialSupport(channelMerchantManager, mockIsvConfigAssembler,
                mockChannelCredentialService, mockIsvCredentialService, mockDirectCredentialService);
    }

    private MockChannelCredential globalCredential() {
        return new MockChannelCredential()
                .setMerchantPrivateKey("GLOBAL-PRIVATE")
                .setPlatformPublicKey("GLOBAL-PLATFORM-PUB");
    }

    private void stubIsvAssembler() {
        // MockSdkCredential 无链式 setter, 逐字段赋值
        MockSdkCredential assembled = new MockSdkCredential();
        assembled.setIsvNo(ISV_NO);
        assembled.setSandbox(true);
        when(mockIsvConfigAssembler.buildConfig(CHANNEL_MCH_NO)).thenReturn(assembled);
    }

    @Test
    @DisplayName("服务商产品 + 服务商级两件套齐: 整行下发, 主体编号取服务商编号")
    void build_isvProductWithCompleteCredential_shouldUseRowValues() {
        stubIsvAssembler();
        when(mockIsvCredentialService.findUsable(ISV_NO)).thenReturn(new MockIsvCredential()
                .setIsvNo(ISV_NO)
                .setMerchantPrivateKey("ISV-PRIVATE")
                .setPlatformPublicKey("ISV-PLATFORM-PUB")
                .setGatewayBaseUrl("http://10.0.0.8:10400"));

        MockSdkCredential credential = newSupport().build(ProductEnum.MOCK_ISV.getCode(), CHANNEL_MCH_NO);

        assertEquals("ISV-PRIVATE", credential.getMerchantPrivateKey());
        assertEquals("ISV-PLATFORM-PUB", credential.getPlatformPublicKey());
        // 主体编号=服务商编号: one 侧随报文/查询串携带 key_owner_no, 模拟网关按主体编号定位登记公钥
        assertEquals(ISV_NO, credential.getKeyOwnerNo());
        assertEquals("http://10.0.0.8:10400", credential.getGatewayBaseUrl());
        assertEquals(ISV_NO, credential.getIsvNo());
        // 服务商级已配置: 不触碰全局单行
        verify(mockChannelCredentialService, never()).getRequiredConfigured();
    }

    @Test
    @DisplayName("服务商产品 + 服务商级未配置(findUsable=null): 整套回落全局单行, 主体编号/地址为空")
    void build_isvProductWithoutUsableCredential_shouldFallBackGlobal() {
        stubIsvAssembler();
        when(mockIsvCredentialService.findUsable(ISV_NO)).thenReturn(null);
        when(mockChannelCredentialService.getRequiredConfigured()).thenReturn(globalCredential());

        MockSdkCredential credential = newSupport().build(ProductEnum.MOCK_ISV.getCode(), CHANNEL_MCH_NO);

        assertEquals("GLOBAL-PRIVATE", credential.getMerchantPrivateKey());
        assertEquals("GLOBAL-PLATFORM-PUB", credential.getPlatformPublicKey());
        assertNull(credential.getKeyOwnerNo());
        assertNull(credential.getGatewayBaseUrl());
        // isvNo 仍保留, 供子应用识别服务商形态
        assertEquals(ISV_NO, credential.getIsvNo());
    }

    @Test
    @DisplayName("服务商产品 + 服务商级与全局均未配置: fail-fast 不静默下发空密钥")
    void build_isvProductWithoutGlobalCredential_shouldFailFast() {
        stubIsvAssembler();
        when(mockIsvCredentialService.findUsable(ISV_NO)).thenReturn(null);
        when(mockChannelCredentialService.getRequiredConfigured())
                .thenThrow(new BizInfoException(cn.daxpay.open.platform.core.code.CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.mock.globalCredentialNotConfigured"));

        assertThrows(BizInfoException.class,
                () -> newSupport().build(ProductEnum.MOCK_ISV.getCode(), CHANNEL_MCH_NO));
    }

    @Test
    @DisplayName("直连产品 + 商户级两件套齐: 整行下发, 主体编号取通道商户号")
    void build_directProductWithCompleteMchCredential_shouldUseRowValues() {
        when(mockDirectCredentialService.findUsable(CHANNEL_MCH_NO)).thenReturn(new MockDirectCredential()
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
        verify(mockIsvConfigAssembler, never()).buildConfig(CHANNEL_MCH_NO);
        verify(mockIsvCredentialService, never()).findUsable(ISV_NO);
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
        when(mockDirectCredentialService.findUsable(CHANNEL_MCH_NO)).thenReturn(new MockDirectCredential()
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
    @DisplayName("按通道商户号反查产品: product=mock_isv 时走服务商装配")
    void build_byChannelMchNo_shouldResolveProduct() {
        when(channelMerchantManager.findProductByChannelMchNo(CHANNEL_MCH_NO))
                .thenReturn(ProductEnum.MOCK_ISV.getCode());
        stubIsvAssembler();
        when(mockIsvCredentialService.findUsable(ISV_NO)).thenReturn(new MockIsvCredential()
                .setIsvNo(ISV_NO)
                .setMerchantPrivateKey("ISV-PRIVATE")
                .setPlatformPublicKey("ISV-PLATFORM-PUB"));

        MockSdkCredential credential = newSupport().build(CHANNEL_MCH_NO);

        assertEquals(ISV_NO, credential.getIsvNo());
        assertEquals("ISV-PRIVATE", credential.getMerchantPrivateKey());
        assertEquals(ISV_NO, credential.getKeyOwnerNo());
    }
}
