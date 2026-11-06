package cn.daxpay.open.payment.elf.service.cashier;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.device.terminal.dao.TerminalDeviceManager;
import cn.daxpay.open.payment.device.terminal.entity.TerminalDevice;
import cn.daxpay.open.payment.elf.result.cashier.ElfCashierConfigOptionsResult;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/// # 精灵收银配置选项查询
///
/// 供 PC 收款精灵首启引导与收银窗切换门店使用: 返回当前商户的启用门店与启用应用清单。
/// 只读查询, 商户身份取自 [PaymentContext](精灵身份域仍为 merchant,
/// 由 [cn.daxpay.open.payment.elf.configuration.ElfWebConfig] 身份域守卫强制);
/// 门店的 appId/appName 为门店默认应用, 为空表示跟随商户默认应用(应用由门店带出, 不向收银员暴露应用切换)。
@Slf4j
@Service
@RequiredArgsConstructor
public class ElfCashierConfigService {

    private final PaymentContext paymentContext;

    private final MerchantInfoManager merchantInfoManager;

    private final MchStoreInfoManager mchStoreInfoManager;

    private final MchAppInfoManager mchAppInfoManager;

    private final TerminalDeviceManager terminalDeviceManager;

    /// 查询收银配置选项(启用门店 + 启用应用)
    ///
    /// 门店按默认门店在前、门店号升序排列, 应用按默认应用在前、应用号升序排列, 保证精灵侧输出稳定;
    /// 门店默认应用名称按应用清单一次性映射回填(禁止 N+1)。
    public ElfCashierConfigOptionsResult configOptions() {
        String mchNo = this.requireMchNo();
        MerchantInfo merchant = merchantInfoManager.findByMchNo(mchNo).orElse(null);
        // 只返回启用应用
        List<MchAppInfo> apps = mchAppInfoManager.findEnableByMchNo(mchNo);
        Map<String, String> appNameMap = apps.stream()
                .collect(Collectors.toMap(MchAppInfo::getAppId, MchAppInfo::getAppName, (a, b) -> a));
        List<ElfCashierConfigOptionsResult.AppOption> appOptions = apps.stream()
                .sorted(Comparator.comparing(MchAppInfo::isDefaultApp)
                        .reversed()
                        .thenComparing(MchAppInfo::getAppId))
                .map(app -> new ElfCashierConfigOptionsResult.AppOption()
                        .setAppId(app.getAppId())
                        .setAppName(app.getAppName())
                        .setDefaultApp(app.isDefaultApp()))
                .toList();
        // 只返回启用门店; 默认门店在前, 其次按门店号升序
        List<ElfCashierConfigOptionsResult.StoreOption> storeOptions = mchStoreInfoManager
                .findEnableByMchNo(mchNo).stream()
                .sorted(Comparator.comparing(MchStoreInfo::isDefaultStore)
                        .reversed()
                        .thenComparing(MchStoreInfo::getStoreNo))
                .map(store -> new ElfCashierConfigOptionsResult.StoreOption()
                        .setStoreNo(store.getStoreNo())
                        .setStoreName(store.getStoreName())
                        // 空串归一为 null(契约: null 表示跟随商户默认应用)
                        .setAppId(StrUtil.blankToDefault(store.getAppId(), null))
                        .setAppName(appNameMap.get(store.getAppId()))
                        .setDefaultStore(store.isDefaultStore()))
                .toList();
        // 只返回启用未删终端(全商户维度, 精灵端按已选门店过滤); 默认终端在前, 其次按终端号升序
        List<ElfCashierConfigOptionsResult.TerminalOption> terminalOptions = terminalDeviceManager
                .findEnabledByMchNo(mchNo).stream()
                .sorted(Comparator.comparing(TerminalDevice::getDefaultTerminal)
                        .reversed()
                        .thenComparing(TerminalDevice::getTerminalNo))
                .map(terminal -> new ElfCashierConfigOptionsResult.TerminalOption()
                        .setTerminalNo(terminal.getTerminalNo())
                        .setName(terminal.getName())
                        .setStoreNo(terminal.getStoreNo())
                        .setDefaultTerminal(terminal.getDefaultTerminal()))
                .toList();
        return new ElfCashierConfigOptionsResult()
                .setMchNo(mchNo)
                .setMchName(merchant != null ? merchant.getMchName() : null)
                .setStores(storeOptions)
                .setApps(appOptions)
                .setTerminals(terminalOptions);
    }

    /// 商户上下文必须已装载(精灵端 ClientCodeGuard 已强制 merchant 域)
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (StrUtil.isBlank(mchNo)) {
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }
}
