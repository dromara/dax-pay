package cn.daxpay.open.payment.common.context;

import cn.daxpay.open.payment.common.access.MchAppInfoAccessInfo;
import cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.payment.merchant.service.access.MerchantAccessQueryService;
import cn.daxpay.open.platform.core.enums.merchant.MchAppStatusEnum;
import cn.daxpay.open.platform.core.enums.merchant.StoreStatusEnum;
import cn.daxpay.open.platform.core.exception.config.ConfigNotEnableException;
import cn.daxpay.open.platform.iam.service.client.ClientCodeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/// # 商户上下文装载器应用解析测试(门店感知三参重载)
///
/// 锁住内部链路(码牌支付/码牌授权/抖音授权路由)的应用解析契约:
/// 显式应用 > 门店默认应用 > 商户默认应用; 门店相关分支一律宽容(门店不存在/停用/跨商户/未配置应用、
/// 门店应用不存在/停用/跨商户)回落商户默认应用且不抛门店错误; 未传门店时不得触碰门店查询。
@ExtendWith(MockitoExtension.class)
class MerchantContextLoaderTest {

    private static final String MCH_NO = "M1000001";

    private static final String STORE_NO = "S1000001";

    private static final String DEFAULT_APP_ID = "A1000001";

    private static final String STORE_APP_ID = "A1000002";

    private static final String EXPLICIT_APP_ID = "A1000003";

    @Mock
    private ClientCodeService clientCodeService;

    @Mock
    private MerchantAccessQueryService merchantAccessQueryService;

    @Mock
    private PaymentContext paymentContext;

    @Mock
    private MchStoreInfoManager mchStoreInfoManager;

    @InjectMocks
    private MerchantContextLoader loader;

    @Test
    @DisplayName("显式 appId: 直接使用, 门店不参与解析")
    void testExplicitAppIdWins() {
        when(merchantAccessQueryService.getAppByAppId(EXPLICIT_APP_ID))
                .thenReturn(this.app(EXPLICIT_APP_ID, MCH_NO, MchAppStatusEnum.ENABLE.getCode()));

        MchAppInfoAccessInfo result = loader.resolveApp(MCH_NO, EXPLICIT_APP_ID, STORE_NO);

        Assertions.assertEquals(EXPLICIT_APP_ID, result.getAppId());
        // 显式应用优先: 门店与默认应用均不查询
        verifyNoInteractions(mchStoreInfoManager);
        verify(merchantAccessQueryService, never()).getDefaultAppByMchNo(MCH_NO);
    }

    @Test
    @DisplayName("appId 与 storeNo 均为空: 取商户默认应用, 不查询门店")
    void testBlankAppAndBlankStoreUseMerchantDefault() {
        when(merchantAccessQueryService.getDefaultAppByMchNo(MCH_NO))
                .thenReturn(this.app(DEFAULT_APP_ID, MCH_NO, MchAppStatusEnum.ENABLE.getCode()));

        MchAppInfoAccessInfo result = loader.resolveApp(MCH_NO, null, "  ");

        Assertions.assertEquals(DEFAULT_APP_ID, result.getAppId());
        verifyNoInteractions(mchStoreInfoManager);
    }

    @Test
    @DisplayName("码牌 storeNo 有值且 appId 为空: 使用门店默认应用")
    void testStoreAppUsedWhenAppIdBlank() {
        when(mchStoreInfoManager.findByStoreNo(STORE_NO))
                .thenReturn(Optional.of(this.store(MCH_NO, StoreStatusEnum.ENABLE.getCode(), STORE_APP_ID)));
        when(merchantAccessQueryService.getAppByAppId(STORE_APP_ID))
                .thenReturn(this.app(STORE_APP_ID, MCH_NO, MchAppStatusEnum.ENABLE.getCode()));

        MchAppInfoAccessInfo result = loader.resolveApp(MCH_NO, null, STORE_NO);

        Assertions.assertEquals(STORE_APP_ID, result.getAppId());
        // 门店应用有效: 不回落到商户默认应用
        verify(merchantAccessQueryService, never()).getDefaultAppByMchNo(MCH_NO);
    }

    @Test
    @DisplayName("门店应用已停用: 回落商户默认应用(告警日志)且不抛门店错误")
    void testStoreAppDisabledFallbackToDefault() {
        when(mchStoreInfoManager.findByStoreNo(STORE_NO))
                .thenReturn(Optional.of(this.store(MCH_NO, StoreStatusEnum.ENABLE.getCode(), STORE_APP_ID)));
        when(merchantAccessQueryService.getAppByAppId(STORE_APP_ID))
                .thenReturn(this.app(STORE_APP_ID, MCH_NO, MchAppStatusEnum.DISABLED.getCode()));
        when(merchantAccessQueryService.getDefaultAppByMchNo(MCH_NO))
                .thenReturn(this.app(DEFAULT_APP_ID, MCH_NO, MchAppStatusEnum.ENABLE.getCode()));

        MchAppInfoAccessInfo result = loader.resolveApp(MCH_NO, null, STORE_NO);

        Assertions.assertEquals(DEFAULT_APP_ID, result.getAppId());
    }

    @Test
    @DisplayName("门店应用不存在: 回落商户默认应用")
    void testStoreAppNotExistFallbackToDefault() {
        when(mchStoreInfoManager.findByStoreNo(STORE_NO))
                .thenReturn(Optional.of(this.store(MCH_NO, StoreStatusEnum.ENABLE.getCode(), STORE_APP_ID)));
        when(merchantAccessQueryService.getAppByAppId(STORE_APP_ID)).thenReturn(null);
        when(merchantAccessQueryService.getDefaultAppByMchNo(MCH_NO))
                .thenReturn(this.app(DEFAULT_APP_ID, MCH_NO, MchAppStatusEnum.ENABLE.getCode()));

        MchAppInfoAccessInfo result = loader.resolveApp(MCH_NO, null, STORE_NO);

        Assertions.assertEquals(DEFAULT_APP_ID, result.getAppId());
    }

    @Test
    @DisplayName("门店应用不属于当前商户: 回落商户默认应用")
    void testStoreAppOfOtherMerchantFallbackToDefault() {
        when(mchStoreInfoManager.findByStoreNo(STORE_NO))
                .thenReturn(Optional.of(this.store(MCH_NO, StoreStatusEnum.ENABLE.getCode(), STORE_APP_ID)));
        when(merchantAccessQueryService.getAppByAppId(STORE_APP_ID))
                .thenReturn(this.app(STORE_APP_ID, "M9999999", MchAppStatusEnum.ENABLE.getCode()));
        when(merchantAccessQueryService.getDefaultAppByMchNo(MCH_NO))
                .thenReturn(this.app(DEFAULT_APP_ID, MCH_NO, MchAppStatusEnum.ENABLE.getCode()));

        MchAppInfoAccessInfo result = loader.resolveApp(MCH_NO, null, STORE_NO);

        Assertions.assertEquals(DEFAULT_APP_ID, result.getAppId());
    }

    @Test
    @DisplayName("门店不存在: 回落商户默认应用且不抛错")
    void testStoreNotExistFallbackToDefault() {
        when(mchStoreInfoManager.findByStoreNo(STORE_NO)).thenReturn(Optional.empty());
        when(merchantAccessQueryService.getDefaultAppByMchNo(MCH_NO))
                .thenReturn(this.app(DEFAULT_APP_ID, MCH_NO, MchAppStatusEnum.ENABLE.getCode()));

        MchAppInfoAccessInfo result = loader.resolveApp(MCH_NO, null, STORE_NO);

        Assertions.assertEquals(DEFAULT_APP_ID, result.getAppId());
    }

    @Test
    @DisplayName("门店跨商户: 门店应用不参与, 回落商户默认应用")
    void testStoreOfOtherMerchantFallbackToDefault() {
        when(mchStoreInfoManager.findByStoreNo(STORE_NO))
                .thenReturn(Optional.of(this.store("M9999999", StoreStatusEnum.ENABLE.getCode(), STORE_APP_ID)));
        when(merchantAccessQueryService.getDefaultAppByMchNo(MCH_NO))
                .thenReturn(this.app(DEFAULT_APP_ID, MCH_NO, MchAppStatusEnum.ENABLE.getCode()));

        MchAppInfoAccessInfo result = loader.resolveApp(MCH_NO, null, STORE_NO);

        Assertions.assertEquals(DEFAULT_APP_ID, result.getAppId());
        // 跨商户门店: 不读取其配置的应用
        verify(merchantAccessQueryService, never()).getAppByAppId(STORE_APP_ID);
    }

    @Test
    @DisplayName("门店已停用: 门店应用不参与, 回落商户默认应用")
    void testStoreDisabledFallbackToDefault() {
        when(mchStoreInfoManager.findByStoreNo(STORE_NO))
                .thenReturn(Optional.of(this.store(MCH_NO, StoreStatusEnum.DISABLED.getCode(), STORE_APP_ID)));
        when(merchantAccessQueryService.getDefaultAppByMchNo(MCH_NO))
                .thenReturn(this.app(DEFAULT_APP_ID, MCH_NO, MchAppStatusEnum.ENABLE.getCode()));

        MchAppInfoAccessInfo result = loader.resolveApp(MCH_NO, null, STORE_NO);

        Assertions.assertEquals(DEFAULT_APP_ID, result.getAppId());
        verify(merchantAccessQueryService, never()).getAppByAppId(STORE_APP_ID);
    }

    @Test
    @DisplayName("门店未配置应用: 回落商户默认应用")
    void testStoreAppIdBlankFallbackToDefault() {
        when(mchStoreInfoManager.findByStoreNo(STORE_NO))
                .thenReturn(Optional.of(this.store(MCH_NO, StoreStatusEnum.ENABLE.getCode(), null)));
        when(merchantAccessQueryService.getDefaultAppByMchNo(MCH_NO))
                .thenReturn(this.app(DEFAULT_APP_ID, MCH_NO, MchAppStatusEnum.ENABLE.getCode()));

        MchAppInfoAccessInfo result = loader.resolveApp(MCH_NO, null, STORE_NO);

        Assertions.assertEquals(DEFAULT_APP_ID, result.getAppId());
    }

    @Test
    @DisplayName("门店应用无效且商户无默认应用: 抛既有默认应用异常(不出现门店错误)")
    void testStoreAppInvalidAndNoDefaultAppThrowExistingError() {
        when(mchStoreInfoManager.findByStoreNo(STORE_NO))
                .thenReturn(Optional.of(this.store(MCH_NO, StoreStatusEnum.ENABLE.getCode(), STORE_APP_ID)));
        when(merchantAccessQueryService.getAppByAppId(STORE_APP_ID))
                .thenReturn(this.app(STORE_APP_ID, MCH_NO, MchAppStatusEnum.DISABLED.getCode()));
        when(merchantAccessQueryService.getDefaultAppByMchNo(MCH_NO)).thenReturn(null);

        ConfigNotEnableException exception = Assertions.assertThrows(ConfigNotEnableException.class,
                () -> loader.resolveApp(MCH_NO, null, STORE_NO));

        // open 异常携带 i18n messageKey, 断言 key 而非中文文案
        Assertions.assertEquals("error.payment.merchant.defaultAppConfigNotFound", exception.resolveMessageKey());
    }

    /// 组装应用接入信息
    private MchAppInfoAccessInfo app(String appId, String mchNo, String status) {
        return new MchAppInfoAccessInfo()
                .setAppId(appId)
                .setMchNo(mchNo)
                .setStatus(status);
    }

    /// 组装门店实体(父类链式 setter 返回父类型, 单独赋值)
    private MchStoreInfo store(String mchNo, String status, String appId) {
        MchStoreInfo store = new MchStoreInfo();
        store.setMchNo(mchNo);
        store.setStoreNo(STORE_NO);
        store.setStatus(status);
        store.setAppId(appId);
        return store;
    }
}
