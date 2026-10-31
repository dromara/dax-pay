package cn.daxpay.open.channel.mock.service.credential;

import cn.daxpay.open.channel.mock.dao.credential.MockIsvCredentialManager;
import cn.daxpay.open.channel.mock.dao.isv.MockIsvInfoManager;
import cn.daxpay.open.channel.mock.entity.credential.MockIsvCredential;
import cn.daxpay.open.channel.mock.entity.isv.MockIsvInfo;
import cn.daxpay.open.channel.mock.param.credential.MockIsvCredentialParam;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/// # Mock 服务商接入密钥服务测试(服务商级独立密钥: save/get 配置范式 + 两件套口径)
@ExtendWith(MockitoExtension.class)
class MockIsvCredentialServiceTest {

    private static final String ISV_NO = "MISV1234567890123456";

    @Mock
    private MockIsvCredentialManager mockIsvCredentialManager;
    @Mock
    private MockIsvInfoManager mockIsvInfoManager;

    private MockIsvCredentialService newService() {
        return new MockIsvCredentialService(mockIsvCredentialManager, mockIsvInfoManager);
    }

    private void stubIsvExists() {
        when(mockIsvInfoManager.findByIsvNo(ISV_NO)).thenReturn(Optional.of(new MockIsvInfo()));
    }

    @Test
    @DisplayName("保存: 服务商不存在时抛业务异常")
    void save_shouldRejectWhenIsvNotExist() {
        when(mockIsvInfoManager.findByIsvNo(ISV_NO)).thenReturn(Optional.empty());
        MockIsvCredentialParam param = new MockIsvCredentialParam().setIsvNo(ISV_NO);

        assertThrows(BizInfoException.class, () -> newService().save(param));
        verifyNoInteractions(mockIsvCredentialManager);
    }

    @Test
    @DisplayName("保存: 无行时插入新行, 业务字段全量落库")
    void save_shouldInsertWhenAbsent() {
        stubIsvExists();
        when(mockIsvCredentialManager.findByIsvNo(ISV_NO)).thenReturn(null);
        MockIsvCredentialParam param = new MockIsvCredentialParam()
                .setIsvNo(ISV_NO)
                .setGatewayBaseUrl("http://10.0.0.8:10400")
                .setMerchantPrivateKey("ISV-PRIVATE")
                .setPlatformPublicKey("ISV-PLATFORM-PUB");

        newService().save(param);

        ArgumentCaptor<MockIsvCredential> captor = ArgumentCaptor.forClass(MockIsvCredential.class);
        verify(mockIsvCredentialManager).save(captor.capture());
        MockIsvCredential saved = captor.getValue();
        assertEquals(ISV_NO, saved.getIsvNo());
        assertEquals("http://10.0.0.8:10400", saved.getGatewayBaseUrl());
        assertEquals("ISV-PRIVATE", saved.getMerchantPrivateKey());
        assertEquals("ISV-PLATFORM-PUB", saved.getPlatformPublicKey());
        verify(mockIsvCredentialManager, never()).updateById(any(MockIsvCredential.class));
    }

    @Test
    @DisplayName("保存: 已有行时整体更新(行内对象), 网关地址留空保留原值")
    void save_shouldUpdateExistingRow() {
        stubIsvExists();
        MockIsvCredential existing = new MockIsvCredential()
                .setIsvNo(ISV_NO)
                .setMerchantPrivateKey("OLD-PRIVATE")
                .setPlatformPublicKey("OLD-PLATFORM-PUB")
                .setGatewayBaseUrl("http://10.0.0.8:10400");
        when(mockIsvCredentialManager.findByIsvNo(ISV_NO)).thenReturn(existing);
        MockIsvCredentialParam param = new MockIsvCredentialParam()
                .setIsvNo(ISV_NO)
                .setMerchantPrivateKey("NEW-PRIVATE")
                .setPlatformPublicKey("NEW-PLATFORM-PUB");

        newService().save(param);

        verify(mockIsvCredentialManager).updateById(existing);
        assertEquals("NEW-PRIVATE", existing.getMerchantPrivateKey());
        assertEquals("NEW-PLATFORM-PUB", existing.getPlatformPublicKey());
        // 网关地址参数为空(null), 保留原值
        assertEquals("http://10.0.0.8:10400", existing.getGatewayBaseUrl());
    }

    @Test
    @DisplayName("保存: 已配置服务商仅改网关地址(两段 PEM 留空)时放行, 两件套保留原值")
    void save_shouldAllowAddressOnlyUpdate() {
        stubIsvExists();
        MockIsvCredential existing = new MockIsvCredential()
                .setIsvNo(ISV_NO)
                .setMerchantPrivateKey("OLD-PRIVATE")
                .setPlatformPublicKey("OLD-PLATFORM-PUB")
                .setGatewayBaseUrl("http://10.0.0.8:10400");
        when(mockIsvCredentialManager.findByIsvNo(ISV_NO)).thenReturn(existing);
        MockIsvCredentialParam param = new MockIsvCredentialParam()
                .setIsvNo(ISV_NO)
                .setGatewayBaseUrl("http://10.0.0.9:10400");

        newService().save(param);

        verify(mockIsvCredentialManager).updateById(existing);
        assertEquals("OLD-PRIVATE", existing.getMerchantPrivateKey());
        assertEquals("OLD-PLATFORM-PUB", existing.getPlatformPublicKey());
        assertEquals("http://10.0.0.9:10400", existing.getGatewayBaseUrl());
    }

    @Test
    @DisplayName("保存: 两段 PEM 全空时允许保存(仅建档, 密钥字段不动)")
    void save_shouldAllowAllBlank() {
        stubIsvExists();
        when(mockIsvCredentialManager.findByIsvNo(ISV_NO)).thenReturn(null);
        MockIsvCredentialParam param = new MockIsvCredentialParam().setIsvNo(ISV_NO);

        newService().save(param);

        ArgumentCaptor<MockIsvCredential> captor = ArgumentCaptor.forClass(MockIsvCredential.class);
        verify(mockIsvCredentialManager).save(captor.capture());
        assertEquals(ISV_NO, captor.getValue().getIsvNo());
        assertNull(captor.getValue().getMerchantPrivateKey());
        assertNull(captor.getValue().getPlatformPublicKey());
    }

    @Test
    @DisplayName("脱敏视图: 未保存过时 configured=false 其余为空")
    void getMasked_shouldReportUnconfiguredWhenAbsent() {
        when(mockIsvCredentialManager.findByIsvNo(ISV_NO)).thenReturn(null);

        var result = newService().getMasked(ISV_NO);

        assertEquals(ISV_NO, result.getIsvNo());
        assertEquals(false, result.getConfigured());
        assertNull(result.getGatewayBaseUrl());
    }

    @Test
    @DisplayName("脱敏视图: 两件套不齐(缺平台公钥)时 configured=false(半配置态)")
    void getMasked_shouldReportUnconfiguredWhenPemPairIncomplete() {
        MockIsvCredential row = new MockIsvCredential()
                .setIsvNo(ISV_NO)
                .setMerchantPrivateKey("ISV-PRIVATE");
        when(mockIsvCredentialManager.findByIsvNo(ISV_NO)).thenReturn(row);

        var result = newService().getMasked(ISV_NO);

        assertEquals(false, result.getConfigured());
        assertEquals("ISV-PRIVATE", result.getMerchantPrivateKeyMasked());
        assertNull(result.getPlatformPublicKeyMasked());
    }

    @Test
    @DisplayName("脱敏视图: 两件套齐时 configured=true, 脱敏字段承载原文, 更新时间回落创建时间")
    void getMasked_shouldMapConfiguredRow() {
        MockIsvCredential row = new MockIsvCredential()
                .setIsvNo(ISV_NO)
                .setGatewayBaseUrl("http://10.0.0.8:10400")
                .setMerchantPrivateKey("ISV-PRIVATE")
                .setPlatformPublicKey("ISV-PLATFORM-PUB");
        OffsetDateTime createTime = OffsetDateTime.of(2026, 10, 27, 12, 0, 0, 0, ZoneOffset.UTC);
        row.setCreateTime(createTime);
        when(mockIsvCredentialManager.findByIsvNo(ISV_NO)).thenReturn(row);

        var result = newService().getMasked(ISV_NO);

        assertEquals(true, result.getConfigured());
        assertEquals("http://10.0.0.8:10400", result.getGatewayBaseUrl());
        // 脱敏字段承载原文, 序列化时由 @SensitiveInfo 统一脱敏
        assertEquals("ISV-PRIVATE", result.getMerchantPrivateKeyMasked());
        assertEquals("ISV-PLATFORM-PUB", result.getPlatformPublicKeyMasked());
        assertNotNull(result.getUpdateTime());
        assertEquals(createTime, result.getUpdateTime());
    }

    @Test
    @DisplayName("装配链入口: 两件套齐的行整行返回; 两件套不齐或 isvNo 空白返回 null")
    void findUsable_shouldGateByPemPair() {
        MockIsvCredentialService service = newService();
        // isvNo 空白不查库直接 null
        assertNull(service.findUsable(null));
        assertNull(service.findUsable(" "));
        verifyNoInteractions(mockIsvCredentialManager);

        // 行存在但两件套不齐(半配置) → null(回落全局)
        when(mockIsvCredentialManager.findByIsvNo(ISV_NO)).thenReturn(
                new MockIsvCredential().setIsvNo(ISV_NO).setMerchantPrivateKey("PRIVATE-ONLY"));
        assertNull(service.findUsable(ISV_NO));

        // 两件套齐 → 原行返回
        MockIsvCredential usable = new MockIsvCredential()
                .setIsvNo(ISV_NO)
                .setMerchantPrivateKey("ISV-PRIVATE")
                .setPlatformPublicKey("ISV-PLATFORM-PUB");
        when(mockIsvCredentialManager.findByIsvNo(ISV_NO)).thenReturn(usable);
        assertSame(usable, service.findUsable(ISV_NO));
    }
}
