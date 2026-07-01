package cn.daxpay.open.payment.merchant.service.config;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.payment.merchant.convert.config.MchAppNotifyConfigConvert;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.dao.config.MchAppNotifyConfigManager;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.payment.merchant.entity.config.MchAppNotifyConfig;
import cn.daxpay.open.payment.merchant.param.config.MchAppNotifyConfigParam;
import cn.daxpay.open.payment.merchant.result.config.MchAppNotifyConfigResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 商户应用事件通知配置服务
///
/// 应用级通用事件通知配置, 与支付订单级回调并行, 当前版本仅维护配置数据,
/// 发送链路(任务/重试/记录)后续阶段实现
@Slf4j
@Service
@RequiredArgsConstructor
public class MchAppNotifyConfigService {

    private final MchAppNotifyConfigManager notifyConfigManager;

    private final MchAppInfoManager mchAppInfoManager;

    /// 根据应用ID查询通知配置, 无记录时返回默认空对象(不落库)
    public MchAppNotifyConfigResult findByAppId(String appId) {
        return notifyConfigManager.findByAppId(appId)
                .map(MchAppNotifyConfig::toResult)
                .orElseGet(() -> new MchAppNotifyConfigResult()
                        .setAppId(appId)
                        .setNotifyWay("http")
                        .setStatus(false));
    }

    /// 保存或更新(按应用ID upsert)
    public void saveOrUpdate(MchAppNotifyConfigParam param) {
        // 校验应用存在并获取商户号(通知配置冗余商户号, 便于按商户过滤与鉴权)
        MchAppInfo mchApp = mchAppInfoManager.findByAppId(param.getAppId())
                // 商户: 商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.mchAppNotFound"));
        var existing = notifyConfigManager.findByAppId(param.getAppId());
        if (existing.isPresent()) {
            MchAppNotifyConfig entity = existing.get();
            MchAppNotifyConfigConvert.CONVERT.copy(param, entity);
            notifyConfigManager.updateById(entity);
        } else {
            MchAppNotifyConfig entity = MchAppNotifyConfigConvert.CONVERT.toEntity(param);
            entity.setMchNo(mchApp.getMchNo());
            notifyConfigManager.save(entity);
        }
    }
}
