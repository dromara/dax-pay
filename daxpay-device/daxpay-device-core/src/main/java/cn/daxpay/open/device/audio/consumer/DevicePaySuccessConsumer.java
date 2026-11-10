package cn.daxpay.open.device.audio.consumer;

import cn.daxpay.open.device.audio.service.AudioBroadcastService;
import cn.daxpay.open.device.comm.DeviceArtemisConstants;
import cn.daxpay.open.device.comm.DeviceCommand;
import cn.daxpay.open.device.comm.DeviceNotifyPayload;
import cn.daxpay.open.device.comm.DevicePaySuccessMessage;
import cn.daxpay.open.device.linkage.dao.MchAppDeviceConfigManager;
import cn.daxpay.open.device.linkage.entity.DeviceMchAppConfig;
import cn.daxpay.open.device.print.service.PrintPushService;
import cn.daxpay.open.device.virtual.client.service.VirtualDevicePushClient;
import cn.daxpay.open.device.virtual.dao.VirtualDeviceManager;
import cn.daxpay.open.device.virtual.entity.DeviceVirtual;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeSourceEnum;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.dao.store.MchStoreInfoManager;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/// # 支付成功设备通知消费者(简版)
///
/// 消费 `daxpay.pay.device.success`(消息由 [AudioBroadcastService] 投递):
/// 回查资金凭证(忽略租户) → 读应用级设备联动开关 → 线上单静音判断 → 查商户名下启用虚拟设备
/// → 逐台经 [VirtualDevicePushClient] 推送(播报打印一体)。
///
/// 开源版裁剪(对照 plus `DevicePaySuccessConsumer`):
/// - 无厂商云音箱/云打印台账与 [DevicePushDispatcher] 多厂商分发, 只推虚拟设备一条线;
/// - 无联动规则表/码牌条件惰性回查(开源版 NormalPayOrder 无 qrCode 字段), 开关只有应用级两布尔+版式代码;
/// - 线上单静音保留一行来源判断, 映射见 [ONLINE_MUTE_SOURCES]。
///
/// 开关求值: 读 `device_mch_app_config`(行不存在=播+印默认开, 查询异常降级默认全开);
/// broadcastEnable=false 该笔整条静默(不播不印), printEnable=false 只静默不印(消息不携带 printData)。
/// 一次支付只下发一条 pay.success: 播报字段与平台渲染的 printData 同消息携带
/// (独立打印消息会被 APP 当成二次播报)。
/// 失败不上抛(与商户通知消费者同款, 避免 JMS 重试风暴): 单条消费失败记 ERROR 结束;
/// broker 故障期间消息在队列堆积、恢复后自动补推(消费幂等由 APP 端 msgId 去重承接)。
/// 消费线程无请求/商户上下文, 全部查询走忽略租户口径。
@Slf4j
@Component
@RequiredArgsConstructor
public class DevicePaySuccessConsumer {

    /// 线上单静音来源集合(客户不在店内付款的订单来源; 收银台/聚合扫码/码牌归店内)
    ///
    /// plus 原匹配 `mch_api`/`bill`/`easy_pay`; 开源版 [TradeSourceEnum] 无 `bill`(收款单特性未移植),
    /// `mch_api`(商户 API 下单)与 `easy_pay`(易支付协议)两值在开源版同义保留静默;
    /// `user`/`cashier`/`cashier_code`/`aggress_pay` 四值与 plus 同口径**不静默**(全播)。
    // 待核: `cashier`(网关收银台)按 plus 同口径保留播报; 若商户把收银台链接远程发给客户付款的场景
    // 需要静默, 与门店级/来源级精细化一起另立项(拍板 4: 映射不清先保守全播)。
    private static final Set<String> ONLINE_MUTE_SOURCES = Set.of(
            TradeSourceEnum.MCH_API.getCode(), TradeSourceEnum.EASY_PAY.getCode());

    private final PayTradeManager payTradeManager;
    private final MchAppInfoManager mchAppInfoManager;
    private final MerchantInfoManager merchantInfoManager;
    private final MchStoreInfoManager mchStoreInfoManager;
    private final VirtualDeviceManager virtualDeviceManager;
    private final MchAppDeviceConfigManager mchAppDeviceConfigManager;
    private final VirtualDevicePushClient virtualDevicePushClient;
    private final PrintPushService printPushService;

    @JmsListener(destination = DeviceArtemisConstants.PAY_SUCCESS_QUEUE)
    public void onMessage(String json) {
        DevicePaySuccessMessage message;
        try {
            message = JacksonUtil.toBean(json, DevicePaySuccessMessage.class);
        } catch (Exception e) {
            log.warn("设备通知消息解析失败, 丢弃: json={}, error={}", json, e.getMessage());
            return;
        }
        String tradeNo = message.getTradeNo();
        if (StrUtil.isBlank(tradeNo)) {
            log.warn("设备通知消息缺少 tradeNo, 丢弃: json={}", json);
            return;
        }
        try {
            handlePaySuccess(tradeNo);
        } catch (Exception e) {
            // 设备: 消费失败不上抛(避免 JMS 重试风暴), 单条丢弃
            log.error("设备通知消费失败: tradeNo={}", tradeNo, e);
        }
    }

    /// 播报与打印主体
    private void handlePaySuccess(String tradeNo) {
        PayTrade trade = payTradeManager.findByTradeNoNotTenant(tradeNo).orElse(null);
        if (trade == null) {
            log.warn("设备通知回查交易不存在, 跳过: tradeNo={}", tradeNo);
            return;
        }
        // appId -> 商户号(虚拟设备按商户维度绑定)
        MchAppInfo app = mchAppInfoManager.findByAppIdNotTenant(trade.getAppId()).orElse(null);
        if (app == null) {
            return;
        }
        // 线上单静音(拍板 4): 开放接口 API 下单/易支付类来源不播不印
        if (trade.getSource() != null && ONLINE_MUTE_SOURCES.contains(trade.getSource())) {
            log.debug("线上单静音, 跳过设备联动: appId={}, tradeNo={}, source={}",
                    trade.getAppId(), tradeNo, trade.getSource());
            return;
        }
        // 应用级设备联动配置(行不存在=默认全开; 查询异常降级默认全开, 不因配置读取失败丢播报)
        DeviceMchAppConfig config = findConfigQuietly(trade.getAppId());
        boolean broadcastEnable = config == null || !Boolean.FALSE.equals(config.getBroadcastEnable());
        boolean printEnable = config == null || !Boolean.FALSE.equals(config.getPrintEnable());
        String templateCode = config == null ? null : config.getTemplateCode();
        if (!broadcastEnable) {
            // 设备: 应用播报开关关闭, 该应用支付成功整条静默
            log.debug("应用播报联动静默, 跳过设备联动: appId={}, tradeNo={}", trade.getAppId(), tradeNo);
            return;
        }
        // 商户名下启用且已激活的虚拟设备(appId 过滤 + 门店匹配语义见 Manager 注释);
        // 门店严格匹配开(true)=门店订单只推绑定该门店的设备, 应用无配置行时取默认 false(并集)
        boolean storeStrict = config != null && Boolean.TRUE.equals(config.getStoreStrictMatch());
        List<DeviceVirtual> devices = virtualDeviceManager.findAllEnabledByMchNo(
                app.getMchNo(), trade.getAppId(), trade.getStoreNo(), storeStrict);
        if (devices.isEmpty()) {
            return;
        }
        String mchName = merchantInfoManager.findByMchNoNotTenant(app.getMchNo())
                .map(MerchantInfo::getMchName)
                .orElse("");
        // 商户订单号取业务容器, 一次装配供全部设备复用
        String outBizNo = printPushService.resolveOutBizNo(trade);
        // 播报/打印业务事实一次装配, 全部设备共用
        DeviceNotifyPayload payload = new DeviceNotifyPayload()
                .setTrade(trade)
                .setMchNo(app.getMchNo())
                .setMchName(mchName)
                .setAppName(app.getAppName())
                .setOutBizNo(outBizNo)
                .setStoreName(resolveStoreName(trade.getStoreNo()))
                .setTemplateCode(templateCode);
        for (DeviceVirtual device : devices) {
            // 两层开关 AND: 应用打印开关关 = 消息不携带 printData(只播报不打印)
            DeviceCommand command = DeviceCommand.paySuccess(device, payload);
            if (!printEnable) {
                command.setPrint(false);
            }
            // 支付旁路: 逐台推送, 单台异常吞掉不影响其余设备(异常策略见 VirtualDevicePushClient)
            virtualDevicePushClient.dispatchPaySuccess(command);
        }
    }

    /// 读应用联动配置, 任何异常降级为默认全开(旁路不因配置读取失败丢播报)
    private DeviceMchAppConfig findConfigQuietly(String appId) {
        try {
            return mchAppDeviceConfigManager.findByAppIdNotTenant(appId).orElse(null);
        } catch (Exception e) {
            // 设备: 应用联动配置读取失败, 按默认全开处理
            log.warn("应用联动配置读取失败, 按默认全开处理: appId={}", appId, e);
            return null;
        }
    }

    /// 门店名(版式变量 storeName; 查询失败置空串不阻断播报打印)
    private String resolveStoreName(String storeNo) {
        if (StrUtil.isBlank(storeNo)) {
            return "";
        }
        try {
            return mchStoreInfoManager.findByStoreNoNotTenant(storeNo)
                    .map(MchStoreInfo::getStoreName)
                    .orElse("");
        } catch (Exception e) {
            // 设备: 门店名查询失败, 变量置空串
            log.warn("门店名查询失败: storeNo={}", storeNo, e);
            return "";
        }
    }
}
