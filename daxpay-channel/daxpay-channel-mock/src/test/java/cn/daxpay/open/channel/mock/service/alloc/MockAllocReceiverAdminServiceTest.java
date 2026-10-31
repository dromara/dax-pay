package cn.daxpay.open.channel.mock.service.alloc;

import cn.daxpay.open.channel.mock.client.credential.MockSdkCredential;
import cn.daxpay.open.channel.mock.client.req.MockAllocReceiverReq;
import cn.daxpay.open.channel.mock.dao.alloc.MockAllocReceiverManager;
import cn.daxpay.open.channel.mock.entity.alloc.MockAllocReceiver;
import cn.daxpay.open.channel.mock.param.alloc.MockAllocReceiverBindParam;
import cn.daxpay.open.channel.mock.param.alloc.MockAllocReceiverCreateParam;
import cn.daxpay.open.channel.mock.service.MockAllocReceiverService;
import cn.daxpay.open.channel.mock.service.MockCredentialSupport;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverStatusEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/// # Mock 通道分账接收方台账服务测试(运营端)
///
/// 覆盖三类边界: 产品/类型/名称校验, 通道商户归属与查重, 绑定状态机(fail→bound→unbound)与失败留痕。
@ExtendWith(MockitoExtension.class)
class MockAllocReceiverAdminServiceTest {

    private static final String MCH_NO = "M0001";
    private static final String CHANNEL_MCH_NO = "MOCK-CM-001";

    @Mock
    private MockAllocReceiverManager allocReceiverManager;
    @Mock
    private ChannelMerchantManager channelMerchantManager;
    @Mock
    private MockCredentialSupport mockCredentialSupport;
    @Mock
    private MockAllocReceiverService mockAllocReceiverService;

    private MockAllocReceiverAdminService newService() {
        return new MockAllocReceiverAdminService(allocReceiverManager, channelMerchantManager,
                mockCredentialSupport, mockAllocReceiverService);
    }

    private ChannelMerchant channelMerchant(String product) {
        ChannelMerchant channelMerchant = new ChannelMerchant();
        channelMerchant.setMchNo(MCH_NO);
        channelMerchant.setChannelMchNo(CHANNEL_MCH_NO);
        channelMerchant.setProduct(product);
        return channelMerchant;
    }

    private MockAllocReceiverCreateParam createParam(String receiverType, String receiverName) {
        return new MockAllocReceiverCreateParam()
                .setMchNo(MCH_NO)
                .setChannelMchNo(CHANNEL_MCH_NO)
                .setReceiverType(receiverType)
                .setReceiverAccount("ACC-001")
                .setReceiverName(receiverName);
    }

    @Test
    @DisplayName("新增: 通道商户不属于模拟通道时拒绝")
    void create_shouldRejectNonMockProduct() {
        when(channelMerchantManager.findByMchNoAndChannelMchNo(MCH_NO, CHANNEL_MCH_NO))
                .thenReturn(Optional.of(channelMerchant("alipay")));
        assertThrows(BizInfoException.class, () -> newService().create(createParam("MERCHANT_ID", "商户全称")));
        verify(allocReceiverManager, never()).save(any(MockAllocReceiver.class));
    }

    @Test
    @DisplayName("新增: 通道商户不存在时拒绝")
    void create_shouldRejectMissingChannelMerchant() {
        when(channelMerchantManager.findByMchNoAndChannelMchNo(MCH_NO, CHANNEL_MCH_NO))
                .thenReturn(Optional.empty());
        assertThrows(BizInfoException.class, () -> newService().create(createParam("MERCHANT_ID", "商户全称")));
    }

    @Test
    @DisplayName("新增: 不支持的类型(PERSONAL_OPENID)时拒绝")
    void create_shouldRejectUnsupportedType() {
        when(channelMerchantManager.findByMchNoAndChannelMchNo(MCH_NO, CHANNEL_MCH_NO))
                .thenReturn(Optional.of(channelMerchant("mock_pay")));
        assertThrows(BizInfoException.class, () -> newService().create(createParam("PERSONAL_OPENID", null)));
    }

    @Test
    @DisplayName("新增: 商户号类型缺商户全称时拒绝")
    void create_shouldRejectMerchantTypeWithoutName() {
        when(channelMerchantManager.findByMchNoAndChannelMchNo(MCH_NO, CHANNEL_MCH_NO))
                .thenReturn(Optional.of(channelMerchant("mock_pay")));
        assertThrows(BizInfoException.class, () -> newService().create(createParam("MERCHANT_ID", " ")));
    }

    @Test
    @DisplayName("新增: 同通道商户同类型同账号重复时拒绝")
    void create_shouldRejectDuplicateAccount() {
        when(channelMerchantManager.findByMchNoAndChannelMchNo(MCH_NO, CHANNEL_MCH_NO))
                .thenReturn(Optional.of(channelMerchant("mock_isv")));
        when(allocReceiverManager.existsByChannelMchNoAndTypeAndHash(anyString(), anyString(), anyString()))
                .thenReturn(true);
        assertThrows(BizInfoException.class, () -> newService().create(createParam("USER_ID", null)));
        verify(allocReceiverManager, never()).save(any(MockAllocReceiver.class));
    }

    @Test
    @DisplayName("新增: 校验通过后落库并绑定, 成功回写 bound + 绑定时间")
    void create_shouldBindAndMarkBound() {
        when(channelMerchantManager.findByMchNoAndChannelMchNo(MCH_NO, CHANNEL_MCH_NO))
                .thenReturn(Optional.of(channelMerchant("mock_pay")));
        when(allocReceiverManager.existsByChannelMchNoAndTypeAndHash(anyString(), anyString(), anyString()))
                .thenReturn(false);
        when(mockCredentialSupport.build("mock_pay", CHANNEL_MCH_NO)).thenReturn(new MockSdkCredential());
        when(mockAllocReceiverService.buildReq(anyString(), any(), any())).thenReturn(new MockAllocReceiverReq());

        newService().create(createParam("USER_ID", null));

        ArgumentCaptor<MockAllocReceiver> captor = ArgumentCaptor.forClass(MockAllocReceiver.class);
        verify(allocReceiverManager).save(captor.capture());
        MockAllocReceiver saved = captor.getValue();
        assertEquals("mock_pay", saved.getProduct());
        assertEquals(MCH_NO, saved.getMchNo());
        assertNotNull(saved.getAccountHash());
        // 落库后绑定成功 → 状态 bound + 绑定时间, 错误信息清空
        assertEquals(AllocReceiverStatusEnum.BOUND.getCode(), saved.getStatus());
        assertNotNull(saved.getBindTime());
        assertNull(saved.getErrorMsg());
        verify(mockAllocReceiverService).bind(any(MockAllocReceiverReq.class));
    }

    @Test
    @DisplayName("新增: 通道调用失败时状态置 fail 并留痕, 异常上抛")
    void create_shouldRecordFailWhenBindThrows() {
        when(channelMerchantManager.findByMchNoAndChannelMchNo(MCH_NO, CHANNEL_MCH_NO))
                .thenReturn(Optional.of(channelMerchant("mock_pay")));
        when(allocReceiverManager.existsByChannelMchNoAndTypeAndHash(anyString(), anyString(), anyString()))
                .thenReturn(false);
        when(mockCredentialSupport.build("mock_pay", CHANNEL_MCH_NO)).thenReturn(new MockSdkCredential());
        when(mockAllocReceiverService.buildReq(anyString(), any(), any())).thenReturn(new MockAllocReceiverReq());
        doThrow(new IllegalStateException("子应用不可达"))
                .when(mockAllocReceiverService).bind(any(MockAllocReceiverReq.class));

        ArgumentCaptor<MockAllocReceiver> captor = ArgumentCaptor.forClass(MockAllocReceiver.class);
        assertThrows(RuntimeException.class, () -> newService().create(createParam("USER_ID", null)));
        verify(allocReceiverManager).save(captor.capture());
        MockAllocReceiver saved = captor.getValue();
        assertEquals(AllocReceiverStatusEnum.FAIL.getCode(), saved.getStatus());
        assertNotNull(saved.getErrorMsg());
    }

    @Test
    @DisplayName("重新绑定: 已绑定状态拒绝重复绑定")
    void bind_shouldRejectWhenAlreadyBound() {
        MockAllocReceiver entity = new MockAllocReceiver()
                .setChannelMchNo(CHANNEL_MCH_NO)
                .setProduct("mock_pay")
                .setStatus(AllocReceiverStatusEnum.BOUND.getCode());
        when(allocReceiverManager.findById(1L)).thenReturn(Optional.of(entity));
        MockAllocReceiverBindParam param = new MockAllocReceiverBindParam().setId(1L);
        assertThrows(BizInfoException.class, () -> newService().bind(param));
        verify(mockAllocReceiverService, never()).bind(any(MockAllocReceiverReq.class));
    }

    @Test
    @DisplayName("解绑: 非已绑定状态拒绝解绑")
    void unbind_shouldRejectWhenNotBound() {
        MockAllocReceiver entity = new MockAllocReceiver()
                .setChannelMchNo(CHANNEL_MCH_NO)
                .setProduct("mock_pay")
                .setStatus(AllocReceiverStatusEnum.FAIL.getCode());
        when(allocReceiverManager.findById(2L)).thenReturn(Optional.of(entity));
        assertThrows(BizInfoException.class, () -> newService().unbind(2L));
        verify(mockAllocReceiverService, never()).unbind(any(MockAllocReceiverReq.class));
    }

    @Test
    @DisplayName("解绑: 成功置 unbound + 解绑时间, 清失败原因")
    void unbind_shouldMarkUnbound() {
        MockAllocReceiver entity = new MockAllocReceiver()
                .setChannelMchNo(CHANNEL_MCH_NO)
                .setProduct("mock_pay")
                .setReceiverAccount("ACC-001")
                .setStatus(AllocReceiverStatusEnum.BOUND.getCode());
        when(allocReceiverManager.findById(3L)).thenReturn(Optional.of(entity));
        when(mockCredentialSupport.build("mock_pay", CHANNEL_MCH_NO)).thenReturn(new MockSdkCredential());
        when(mockAllocReceiverService.buildReq(anyString(), any(), any())).thenReturn(new MockAllocReceiverReq());

        newService().unbind(3L);

        assertEquals(AllocReceiverStatusEnum.UNBOUND.getCode(), entity.getStatus());
        assertNotNull(entity.getUnbindTime());
        assertNull(entity.getErrorMsg());
        verify(allocReceiverManager).updateById(entity);
    }

    @Test
    @DisplayName("删除: 已绑定状态拒绝删除")
    void delete_shouldRejectWhenBound() {
        MockAllocReceiver entity = new MockAllocReceiver()
                .setChannelMchNo(CHANNEL_MCH_NO)
                .setStatus(AllocReceiverStatusEnum.BOUND.getCode());
        when(allocReceiverManager.findById(4L)).thenReturn(Optional.of(entity));
        assertThrows(BizInfoException.class, () -> newService().delete(4L));
        verify(allocReceiverManager, never()).deleteById(4L);
    }

    @Test
    @DisplayName("别名: 空白串归一为 null(清空), 任意状态可改")
    void updateAlias_shouldTrimToNull() {
        MockAllocReceiver entity = new MockAllocReceiver()
                .setChannelMchNo(CHANNEL_MCH_NO)
                .setAlias("旧别名")
                .setStatus(AllocReceiverStatusEnum.BOUND.getCode());
        when(allocReceiverManager.findById(5L)).thenReturn(Optional.of(entity));

        newService().updateAlias(5L, "   ");

        assertNull(entity.getAlias());
        verify(allocReceiverManager).updateById(entity);
    }
}
