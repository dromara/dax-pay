package cn.daxpay.open.platform.system.service.config;

import cn.daxpay.open.platform.system.convert.PlatformWechatNotifyConfigConvert;
import cn.daxpay.open.platform.system.entity.config.platform.PlatformWechatNotifyConfig;
import cn.daxpay.open.platform.system.enums.PlatformConfigTypeEnum;
import cn.daxpay.open.platform.system.param.config.PlatformWechatNotifyConfigParam;
import cn.daxpay.open.platform.system.result.config.platform.PlatformWechatNotifyConfigResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信消息通知模板配置服务
///
/// 仅管理场景模板 Id(trade/operate), 存于 `system_platform_config`(非加密).
/// 公众号 AppId/AppSecret 统一从三方平台
/// [PlatformWechatMpAuthConfigService] 读取.
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformWechatNotifyConfigService {

    /// 场景编码: 交易通知
    public static final String SCENE_TRADE = "trade";

    /// 场景编码: 操作通知
    public static final String SCENE_OPERATE = "operate";

    private final SystemPlatformConfigService systemConfigService;

    /// 获取配置(原始实体, 供内部发送链路使用)
    public PlatformWechatNotifyConfig getConfig() {
        return systemConfigService.getOrCreateConfig(PlatformConfigTypeEnum.WECHAT_NOTIFY,
                PlatformWechatNotifyConfig.class,
                new PlatformWechatNotifyConfig());
    }

    /// 获取配置结果(供前端回显)
    public PlatformWechatNotifyConfigResult findConfig() {
        return PlatformWechatNotifyConfigConvert.CONVERT.toResult(this.getConfig());
    }

    /// 更新配置
    public void updateConfig(PlatformWechatNotifyConfigParam param) {
        PlatformWechatNotifyConfig data = this.getConfig();
        PlatformWechatNotifyConfigConvert.CONVERT.copy(param, data);
        systemConfigService.updateConfig(PlatformConfigTypeEnum.WECHAT_NOTIFY, data);
    }

    /// 按场景获取模板Id(trade -> tradeTemplateId, operate -> operateTemplateId)
    public String getTemplateIdByScene(String scene) {
        PlatformWechatNotifyConfig config = getConfig();
        if (SCENE_TRADE.equals(scene)) {
            return config.getTradeTemplateId();
        }
        if (SCENE_OPERATE.equals(scene)) {
            return config.getOperateTemplateId();
        }
        return null;
    }
}
