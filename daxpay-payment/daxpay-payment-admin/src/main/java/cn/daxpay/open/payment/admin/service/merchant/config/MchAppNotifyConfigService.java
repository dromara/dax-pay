package cn.daxpay.open.payment.admin.service.merchant.config;

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
/// 应用级通用事件通知配置, 与支付订单级回调并行。
/// 配置由 [cn.daxpay.open.payment.trade.notice.service.NoticeDispatcher] 读取:
/// notifyWay=http 走 HTTP 回调(notifyUrl), notifyWay=mq 走 MQ 推送(发布到 daxpay.notice.<appId> Topic),
/// 按订阅事件(subscribedEvents)前缀匹配触发, 发送/重试/记录由 NoticeSendEngine 统一负责
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
