package cn.daxpay.open.channel.mock.service.credential;

import cn.daxpay.open.channel.mock.dao.credential.MockDirectCredentialManager;
import cn.daxpay.open.channel.mock.entity.credential.MockPayCredential;
import cn.daxpay.open.channel.mock.param.credential.MockDirectCredentialParam;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

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

/// # Mock 直连商户接入密钥服务测试(商户级拟真凭证: save 校验/空值合并 + findUsable 两件套口径)
@ExtendWith(MockitoExtension.class)
class MockDirectCredentialServiceTest {

    private static final String CHANNEL_MCH_NO = "MOCK-CM-001";

    @Mock
    private MockDirectCredentialManager mockDirectCredentialManager;

    private MockDirectCredentialService newService() {
        return new MockDirectCredentialService(mockDirectCredentialManager);
    }

    @Test
    @DisplayName("保存: channelMchNo 缺失时抛业务异常")
    void save_shouldRejectWhenChannelMchNoMissing() {
        MockDirectCredentialParam param = new MockDirectCredentialParam();

        assertThrows(BizInfoException.class, () -> newService().save(param));
        verifyNoInteractions(mockDirectCredentialManager);
    }

    @Test
    @DisplayName("保存: 两件套齐 + 网关地址时插入新行, 全字段落库")
    void save_shouldInsertWhenAbsent() {
        when(mockDirectCredentialManager.findByChannelMchNo(CHANNEL_MCH_NO)).thenReturn(null);
        MockDirectCredentialParam param = new MockDirectCredentialParam()
                .setChannelMchNo(CHANNEL_MCH_NO)
                .setGatewayBaseUrl("http://10.0.0.8:10400")
                .setMerchantPrivateKey("MCH-PRIVATE")
                .setPlatformPublicKey("MCH-PLATFORM-PUB");

        newService().save(param);

        ArgumentCaptor<MockPayCredential> captor = ArgumentCaptor.forClass(MockPayCredential.class);
        verify(mockDirectCredentialManager).save(captor.capture());
        MockPayCredential saved = captor.getValue();
        assertEquals(CHANNEL_MCH_NO, saved.getChannelMchNo());
        assertEquals("http://10.0.0.8:10400", saved.getGatewayBaseUrl());
        assertEquals("MCH-PRIVATE", saved.getMerchantPrivateKey());
        assertEquals("MCH-PLATFORM-PUB", saved.getPlatformPublicKey());
        verify(mockDirectCredentialManager, never()).updateById(any(MockPayCredential.class));
    }

    @Test
    @DisplayName("保存: 已有行时整体更新(行内对象), 网关地址留空保留原值")
    void save_shouldUpdateExistingRow() {
        MockPayCredential existing = new MockPayCredential()
                .setChannelMchNo(CHANNEL_MCH_NO)
                .setMerchantPrivateKey("OLD-PRIVATE")
                .setPlatformPublicKey("OLD-PLATFORM-PUB")
                .setGatewayBaseUrl("http://10.0.0.8:10400");
        when(mockDirectCredentialManager.findByChannelMchNo(CHANNEL_MCH_NO)).thenReturn(existing);
        MockDirectCredentialParam param = new MockDirectCredentialParam()
                .setChannelMchNo(CHANNEL_MCH_NO)
                .setMerchantPrivateKey("NEW-PRIVATE")
                .setPlatformPublicKey("NEW-PLATFORM-PUB");

        newService().save(param);

        verify(mockDirectCredentialManager).updateById(existing);
        assertEquals("NEW-PRIVATE", existing.getMerchantPrivateKey());
        assertEquals("NEW-PLATFORM-PUB", existing.getPlatformPublicKey());
        // 网关地址参数为空(null), 保留原值
        assertEquals("http://10.0.0.8:10400", existing.getGatewayBaseUrl());
    }

    @Test
    @DisplayName("保存: 已配置商户仅改网关地址(两段 PEM 留空)时放行, 两件套保留原值")
    void save_shouldAllowAddressOnlyUpdate() {
        MockPayCredential existing = new MockPayCredential()
                .setChannelMchNo(CHANNEL_MCH_NO)
                .setMerchantPrivateKey("OLD-PRIVATE")
                .setPlatformPublicKey("OLD-PLATFORM-PUB")
                .setGatewayBaseUrl("http://10.0.0.8:10400");
        when(mockDirectCredentialManager.findByChannelMchNo(CHANNEL_MCH_NO)).thenReturn(existing);
        MockDirectCredentialParam param = new MockDirectCredentialParam()
                .setChannelMchNo(CHANNEL_MCH_NO)
                .setGatewayBaseUrl("http://10.0.0.9:10400");

        newService().save(param);

        verify(mockDirectCredentialManager).updateById(existing);
        assertEquals("OLD-PRIVATE", existing.getMerchantPrivateKey());
        assertEquals("OLD-PLATFORM-PUB", existing.getPlatformPublicKey());
        assertEquals("http://10.0.0.9:10400", existing.getGatewayBaseUrl());
    }

    @Test
    @DisplayName("保存: 两段 PEM 全空时允许保存(仅建档, 密钥字段不动)")
    void save_shouldAllowAllBlank() {
        when(mockDirectCredentialManager.findByChannelMchNo(CHANNEL_MCH_NO)).thenReturn(null);
        MockDirectCredentialParam param = new MockDirectCredentialParam().setChannelMchNo(CHANNEL_MCH_NO);

        newService().save(param);

        ArgumentCaptor<MockPayCredential> captor = ArgumentCaptor.forClass(MockPayCredential.class);
        verify(mockDirectCredentialManager).save(captor.capture());
        assertEquals(CHANNEL_MCH_NO, captor.getValue().getChannelMchNo());
        assertNull(captor.getValue().getMerchantPrivateKey());
        assertNull(captor.getValue().getPlatformPublicKey());
    }

    @Test
    @DisplayName("脱敏视图: 未保存过时 configured=false 其余为空")
    void getMasked_shouldReportUnconfiguredWhenAbsent() {
        when(mockDirectCredentialManager.findByChannelMchNo(CHANNEL_MCH_NO)).thenReturn(null);

        var result = newService().getMasked(CHANNEL_MCH_NO);

        assertEquals(CHANNEL_MCH_NO, result.getChannelMchNo());
        assertEquals(false, result.getConfigured());
        assertNull(result.getGatewayBaseUrl());
    }

    @Test
    @DisplayName("脱敏视图: 两件套不齐(缺平台公钥)时 configured=false(半配置态)")
    void getMasked_shouldReportUnconfiguredWhenPemPairIncomplete() {
        MockPayCredential row = new MockPayCredential()
                .setChannelMchNo(CHANNEL_MCH_NO)
                .setMerchantPrivateKey("MCH-PRIVATE");
        when(mockDirectCredentialManager.findByChannelMchNo(CHANNEL_MCH_NO)).thenReturn(row);

        var result = newService().getMasked(CHANNEL_MCH_NO);

        assertEquals(false, result.getConfigured());
        assertEquals("MCH-PRIVATE", result.getMerchantPrivateKeyMasked());
        assertNull(result.getPlatformPublicKeyMasked());
    }

    @Test
    @DisplayName("脱敏视图: 两件套齐时 configured=true, 脱敏字段承载原文, 更新时间回落创建时间")
    void getMasked_shouldMapConfiguredRow() {
        MockPayCredential row = new MockPayCredential()
                .setChannelMchNo(CHANNEL_MCH_NO)
                .setGatewayBaseUrl("http://10.0.0.8:10400")
                .setMerchantPrivateKey("MCH-PRIVATE")
                .setPlatformPublicKey("MCH-PLATFORM-PUB");
        OffsetDateTime createTime = OffsetDateTime.of(2026, 10, 28, 12, 0, 0, 0, ZoneOffset.UTC);
        row.setCreateTime(createTime);
        when(mockDirectCredentialManager.findByChannelMchNo(CHANNEL_MCH_NO)).thenReturn(row);

        var result = newService().getMasked(CHANNEL_MCH_NO);

        assertEquals(true, result.getConfigured());
        assertEquals("http://10.0.0.8:10400", result.getGatewayBaseUrl());
        // 脱敏字段承载原文, 序列化时由 @SensitiveInfo 统一脱敏
        assertEquals("MCH-PRIVATE", result.getMerchantPrivateKeyMasked());
        assertEquals("MCH-PLATFORM-PUB", result.getPlatformPublicKeyMasked());
        assertNotNull(result.getUpdateTime());
        assertEquals(createTime, result.getUpdateTime());
    }

    @Test
    @DisplayName("装配链入口: 两件套齐的行整行返回; 两件套不齐或 channelMchNo 空白返回 null")
    void findUsable_shouldGateByPemPair() {
        MockDirectCredentialService service = newService();
        // channelMchNo 空白不查库直接 null
        assertNull(service.findUsable(null));
        assertNull(service.findUsable(" "));
        verifyNoInteractions(mockDirectCredentialManager);

        // 行存在但两件套不齐(半配置) → null
        when(mockDirectCredentialManager.findByChannelMchNo(CHANNEL_MCH_NO)).thenReturn(
                new MockPayCredential().setChannelMchNo(CHANNEL_MCH_NO).setMerchantPrivateKey("PRIVATE-ONLY"));
        assertNull(service.findUsable(CHANNEL_MCH_NO));

        // 两件套齐 → 原行返回
        MockPayCredential usable = new MockPayCredential()
                .setChannelMchNo(CHANNEL_MCH_NO)
                .setMerchantPrivateKey("MCH-PRIVATE")
                .setPlatformPublicKey("MCH-PLATFORM-PUB");
        when(mockDirectCredentialManager.findByChannelMchNo(CHANNEL_MCH_NO)).thenReturn(usable);
        assertSame(usable, service.findUsable(CHANNEL_MCH_NO));
    }
}
