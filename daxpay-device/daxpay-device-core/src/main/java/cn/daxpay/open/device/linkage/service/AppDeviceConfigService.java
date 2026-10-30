package cn.daxpay.open.device.linkage.service;

import cn.daxpay.open.device.linkage.dao.MchAppDeviceConfigManager;
import cn.daxpay.open.device.linkage.entity.MchAppDeviceConfig;
import cn.daxpay.open.device.linkage.param.AppSimpleConfigParam;
import cn.daxpay.open.device.linkage.result.AppSimpleConfigResult;
import cn.daxpay.open.device.print.template.PrintTemplateConstants;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/// # 应用设备联动配置服务(极简版)
///
/// 两端(运营/商户)共用一份编排: 读聚合(默认值派生) + 保存三件套;
/// 消费端求值见 [cn.daxpay.open.device.audio.consumer.DevicePaySuccessConsumer]。
/// 开源版无联动规则/线上静音托管/默认模板 ID(拍板 3 裁剪), 版式代码校验对内置常量表。
@Slf4j
@Service
@RequiredArgsConstructor
public class AppDeviceConfigService {

    private final MchAppInfoManager mchAppInfoManager;
    private final MchAppDeviceConfigManager mchAppDeviceConfigManager;

    /// 聚合查询(行不存在=播报开/打印开/standard 默认派生, 不落库)
    public AppSimpleConfigResult getSimpleConfig(String appId) {
        MchAppInfo app = requireApp(appId);
        MchAppDeviceConfig config = mchAppDeviceConfigManager.findByAppIdNotTenant(appId).orElse(null);
        return new AppSimpleConfigResult()
                .setAppId(appId)
                .setMchNo(app.getMchNo())
                .setAppName(app.getAppName())
                .setBroadcastEnable(config == null || !Boolean.FALSE.equals(config.getBroadcastEnable()))
                .setPrintEnable(config == null || !Boolean.FALSE.equals(config.getPrintEnable()))
                .setStoreStrictMatch(config != null && Boolean.TRUE.equals(config.getStoreStrictMatch()))
                .setTemplateCode(config == null || StrUtil.isBlank(config.getTemplateCode())
                        ? PrintTemplateConstants.CODE_STANDARD
                        : config.getTemplateCode());
    }

    /// 保存(播报/打印/版式三件套 + 门店严格匹配可选开关; 存在则全量覆盖, 不存在则新建)
    ///
    /// 运营端与商户端共用: 运营端无商户上下文, mchNo 由应用归属反查显式写入(MchBaseEntity
    /// insert 必须显式 setMchNo 且单独赋值不链式, 见实体注释)。
    /// storeStrictMatch 为可选开关: 空/null 一律落 false(未绑门店设备视为商户通用), 存量行为零变化。
    @Transactional(rollbackFor = Exception.class)
    public void saveSimpleConfig(String appId, Boolean broadcastEnable, Boolean printEnable, String templateCode,
                                 Boolean storeStrictMatch) {
        MchAppInfo app = requireApp(appId);
        String code = StrUtil.isBlank(templateCode) ? PrintTemplateConstants.CODE_STANDARD : templateCode;
        if (!PrintTemplateConstants.exists(code)) {
            // 打印: 打印版式不存在: {0}
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.print.templateNotFound", code);
        }
        MchAppDeviceConfig config = new MchAppDeviceConfig()
                .setAppId(appId)
                .setBroadcastEnable(broadcastEnable)
                .setPrintEnable(printEnable)
                .setStoreStrictMatch(Boolean.TRUE.equals(storeStrictMatch))
                .setTemplateCode(code);
        // MchBaseEntity 的 setMchNo 返回父类型, 单独赋值不链式(运营端 insert 必须显式写 mchNo)
        config.setMchNo(app.getMchNo());
        mchAppDeviceConfigManager.saveConfig(config);
    }

    /// 保存(运营端参数)
    @Transactional(rollbackFor = Exception.class)
    public void saveSimpleConfig(AppSimpleConfigParam param) {
        saveSimpleConfig(param.getAppId(), param.getBroadcastEnable(), param.getPrintEnable(),
                param.getTemplateCode(), param.getStoreStrictMatch());
    }

    /// 应用必存在(运营端上下文无商户租户, 走忽略租户口径)
    private MchAppInfo requireApp(String appId) {
        return mchAppInfoManager.findByAppIdNotTenant(appId)
                .orElseThrow(() -> new DataNotExistException("error.device.virtual.appNotFound"));
    }

    /// 应用归属校验(商户端自助入口): 应用必须存在且归属指定商户
    public MchAppInfo requireAppForMch(String appId, String mchNo) {
        MchAppInfo app = requireApp(appId);
        if (!Objects.equals(app.getMchNo(), mchNo)) {
            // 归属不符与不存在同口径, 不暴露应用是否存在
            throw new DataNotExistException("error.device.virtual.appNotFound");
        }
        return app;
    }
}
