package cn.daxpay.open.device.virtual.client.service;

import cn.daxpay.open.device.linkage.dao.MchAppDeviceConfigManager;
import cn.daxpay.open.device.linkage.entity.DeviceMchAppConfig;
import cn.daxpay.open.device.print.driver.escpos.EscPosCompiler;
import cn.daxpay.open.device.print.service.PrintPushService;
import cn.daxpay.open.device.print.support.TestPrintPreviewResult;
import cn.daxpay.open.device.print.template.engine.model.PrintGoodsContext;
import cn.daxpay.open.device.print.template.engine.model.RenderResult;
import cn.daxpay.open.device.comm.DeviceNotifyPayload;
import cn.daxpay.open.device.comm.PrintPushData;
import cn.daxpay.open.device.support.PeriodCounterService;
import cn.daxpay.open.device.virtual.client.param.VirtualDeviceAckParam;
import cn.daxpay.open.device.virtual.client.param.VirtualDeviceActivateParam;
import cn.daxpay.open.device.virtual.client.param.VirtualDevicePrintPreviewParam;
import cn.daxpay.open.device.virtual.client.param.VirtualDeviceReprintParam;
import cn.daxpay.open.device.virtual.client.result.VirtualDeviceAckResult;
import cn.daxpay.open.device.virtual.client.result.VirtualDeviceActivateResult;
import cn.daxpay.open.device.virtual.client.result.VirtualDevicePrintPreviewResult;
import cn.daxpay.open.device.virtual.client.result.VirtualDeviceReprintResult;
import cn.daxpay.open.device.virtual.client.enums.DeviceClientStatusEnum;
import cn.daxpay.open.device.virtual.dao.VirtualDeviceManager;
import cn.daxpay.open.device.virtual.entity.DeviceVirtual;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformMqttConfig;
import cn.daxpay.open.platform.system.service.config.infra.PlatformMqttConfigService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

/// # 虚拟设备自助服务(激活/回执心跳)
///
/// 服务 APP 免登录公共端点(`/client/device/audio/**`):
/// 虚拟设备为独立台账(device_virtual), 激活/回执按设备号单表定位。
///
/// 开源版单因子激活(拍板: 砍激活码体系): APP 凭「设备码(SN)」激活(服务器地址由 APP 编译期内置),
/// 下发凭证六件套; 设备号唯一准入凭据已接受(拍板 12),
/// 缓解 = 21 亿随机空间 + 激活限流(IP 10/min + SN 20/min) + 可禁用 + 重复激活轮换 secret。
/// 回执/心跳以 HMAC-SHA256(设备 secret) 验签防伪造, ts 窗口+nonce SETNX 防重放,
/// 回写回执并返回设备状态供终端校准; 验签通过即刷新 Redis 在线 ZSET(15 分钟窗口)
/// 并按 30 分钟节流落库 last_online_time。
///
/// B 波(2026-10-24)已补齐: `reprint`(补打取数)与 `previewPrint`(试打取数)走
/// [PrintPushService] 内置版式渲染, 版式来源 = 交易应用配置版式 > 内置 standard(恒命中, 无落空报错)。
@Slf4j
@RequiredArgsConstructor
@Service
public class VirtualDeviceClientService {

    /// 播报密钥长度(ChannelAesGcmEncryptor 要求恰好 32 字符)
    public static final int SECRET_LENGTH = 32;

    /// 激活 IP 维度限流(次/分钟)
    ///
    /// 60 而非更低: 同一门店/办公点通常共用一个出口 IP, 凭据轮换后设备会集中重新激活,
    /// 阈值过紧会把合法批量刷新挡在门外(终端侧另有 0-30s 抖动分散);
    /// IP 是本接口唯一的防枚举维度(SN 维度限流对"枚举不同设备号"无效), 故不取消。
    private static final int ACTIVATE_IP_LIMIT = 60;

    /// 激活 SN 维度限流(次/分钟)
    private static final int ACTIVATE_SN_LIMIT = 20;

    /// 回执时间窗口(±5 分钟, 防重放)
    private static final long ACK_TS_WINDOW_MILLIS = 5 * 60 * 1000L;

    /// 补打取数设备维度限流(次/分钟; 覆盖一次清积压最多 20 张 + 手动补打余量)
    private static final int REPRINT_DEVICE_LIMIT = 60;

    /// 试打取数设备维度限流(次/分钟; 用户手动测试打印, 留足连点与重试余量)
    private static final int PREVIEW_DEVICE_LIMIT = 30;

    private final VirtualDeviceManager virtualDeviceManager;
    private final PlatformMqttConfigService platformMqttConfigService;
    private final PeriodCounterService periodCounterService;
    private final VirtualOnlineTracker virtualOnlineTracker;
    private final VirtualTestAckTracker virtualTestAckTracker;
    private final VirtualDeviceStatService virtualDeviceStatService;
    private final PayTradeManager payTradeManager;
    private final MchAppInfoManager mchAppInfoManager;
    private final MerchantInfoManager merchantInfoManager;
    private final MchAppDeviceConfigManager mchAppDeviceConfigManager;
    private final PrintPushService printPushService;

    /// 生成播报密钥(AES-256 密钥, 32 字符)
    public static String generateSecret() {
        return RandomUtil.randomString(SECRET_LENGTH);
    }

    /// 激活虚拟设备(免登录, 设备号单因子)
    ///
    /// 限流通过后进入激活语义: 设备存在/启用/已绑商户 → 轮换 secret(旧密文/旧回执签名即时作废
    /// = 换机语义), 下发接入凭证六件套(MQTT 接入参数取平台配置「MQTT接入」);
    /// 激活成败排障走应用日志(拍板 11: 不建激活审计表)。
    public VirtualDeviceActivateResult activate(VirtualDeviceActivateParam param, String clientIp) {
        // IP/SN 两维限流(分钟窗口, 防设备号撞库)
        checkActivateRateLimit("ip:" + clientIp, ACTIVATE_IP_LIMIT);
        checkActivateRateLimit("sn:" + param.getSn(), ACTIVATE_SN_LIMIT);
        DeviceVirtual device = virtualDeviceManager.findByDeviceNo(param.getSn())
                .orElseThrow(VirtualDeviceClientService::activateFail);
        if (!Boolean.TRUE.equals(device.getEnable())) {
            // 设备: 设备已停用, 请联系平台运营
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.deviceDisabled");
        }
        // 归属绑定在运营端完成, 未绑定商户的设备不可激活
        if (StrUtil.isBlank(device.getMchNo())) {
            throw activateFail();
        }
        // 接入参数取平台配置「MQTT接入」(纯 DB 存储, 保存即时生效; 已激活设备重新激活后才取到新值);
        // 缺失必须在轮换 secret 之前拦截——否则旧凭证被作废却下发空凭证, 终端重激活后无法连接
        PlatformMqttConfig mqttConfig = platformMqttConfigService.getMqttConfig();
        if (StrUtil.isBlank(mqttConfig.getBrokerWssUrl())
                || StrUtil.isBlank(mqttConfig.getMqttUsername())
                || StrUtil.isBlank(mqttConfig.getMqttPassword())) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.mqttNotConfigured");
        }
        // 轮换 secret(旧密文/旧回执签名即时作废 = 换机语义)
        String secret = generateSecret();
        virtualDeviceManager.rotateSecret(device.getId(), secret);
        log.info("虚拟设备激活: deviceNo={}, ip={}", device.getDeviceNo(), clientIp);
        return new VirtualDeviceActivateResult()
                .setDeviceNo(device.getDeviceNo())
                .setSecret(secret)
                .setBrokerUrl(mqttConfig.getBrokerWssUrl())
                .setTopic(VirtualDevicePushClient.topic(device.getDeviceNo()))
                .setMqttUsername(mqttConfig.getMqttUsername())
                .setMqttPassword(mqttConfig.getMqttPassword())
                // 凭据版本随激活下发, 终端持久化后随心跳上报; 凭据轮换时据此自动重新激活
                .setCredentialVersion(PlatformMqttConfigService.credentialVersion(mqttConfig));
    }

    /// 回执/心跳(HMAC 验签 -> nonce 防重放 -> 刷新在线 -> 回写收款回执 -> 返回设备状态供终端校准)
    ///
    /// 设备不存在/未激活/验签失败统一返回 revoked(凭证失效), 终端据此清凭证回激活页;
    /// 解绑会清空 secret, 删除后设备行不可见, 旧凭证下一次心跳即落到该分支。
    /// 时间窗超限抛错(提示校准设备时间); nonce 重复抛错(重放拒绝, 不返回 revoked 防误踢真设备)。
    public VirtualDeviceAckResult ack(VirtualDeviceAckParam param) {
        DeviceVirtual device = virtualDeviceManager.findByDeviceNo(param.getDeviceNo()).orElse(null);
        if (device == null || StrUtil.isBlank(device.getSecret())) {
            return revoked();
        }
        // 时间窗口校验(防重放)
        long now = System.currentTimeMillis();
        if (Math.abs(now - param.getTs()) > ACK_TS_WINDOW_MILLIS) {
            // 设备: 回执已过期, 请校准设备时间后重试
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.ackExpired");
        }
        // HMAC-SHA256(deviceNo + ts + nonce) 验签(签名不通过的请求不消耗 nonce 空间, 防恶意占位)
        String expected = hmacSha256(device.getSecret(), param.getDeviceNo() + param.getTs() + param.getNonce());
        if (!expected.equalsIgnoreCase(param.getSign())) {
            return revoked();
        }
        // nonce 一次性消费(重复即重放, 抛错拒绝; APP 心跳下轮自愈, 不返回 revoked 防误踢)
        virtualOnlineTracker.consumeAckNonce(param.getNonce());
        // 验签通过即在线: 刷新 Redis ZSET + 30 分钟节流落库(DB 列非实时, 只做兜底)
        virtualOnlineTracker.refreshOnline(param.getDeviceNo());
        if (virtualOnlineTracker.tryBeginOnlineWrite(param.getDeviceNo())) {
            virtualDeviceManager.updateLastOnlineTime(device.getId());
        }
        // 收款回执落库(仅 pay.success, 周期心跳不覆盖)
        if (StrUtil.isNotBlank(param.getMsgId()) && VirtualDevicePushClient.CMD_PAY_SUCCESS.equals(param.getCmd())) {
            virtualDeviceManager.updateLastPayAck(device.getId(), param.getMsgId(), param.getOk());
            // 回执成功日计数(推送→回执成功率口径)
            if (Boolean.TRUE.equals(param.getOk())) {
                virtualDeviceStatService.onAckOk(param.getDeviceNo());
            }
        }
        // 试播/试打/纯打印回执: 不落台账(收款回执三列语义专属 pay.success), 存 Redis 排障快照
        if (StrUtil.isNotBlank(param.getMsgId())
                && (VirtualDevicePushClient.CMD_AUDIO_TEST.equals(param.getCmd())
                        || VirtualDevicePushClient.CMD_PRINT_TICKET.equals(param.getCmd()))) {
            virtualTestAckTracker.record(param.getDeviceNo(), param.getMsgId(), param.getCmd(), param.getOk());
        }
        // 回执记日志
        if (StrUtil.isNotBlank(param.getMsgId())) {
            log.info("虚拟设备回执: deviceNo={}, msgId={}, cmd={}, ok={}",
                    param.getDeviceNo(), param.getMsgId(), param.getCmd(), param.getOk());
        }
        boolean bound = StrUtil.isNotBlank(device.getMchNo());
        boolean enabled = Boolean.TRUE.equals(device.getEnable());
        // 凭据版本比对: 终端携版本时才查配置(老 APP 不传, 保持原有行为且不增加查询);
        // 不一致只标记需重新激活, 不返回 revoked——播报密钥仍然有效, 清凭证会误伤已绑定设备
        String credentialVersion = null;
        boolean needReactivate = false;
        if (StrUtil.isNotBlank(param.getCredentialVersion())) {
            PlatformMqttConfig mqttConfig = platformMqttConfigService.getMqttConfig();
            credentialVersion = PlatformMqttConfigService.credentialVersion(mqttConfig);
            needReactivate = !credentialVersion.equals(param.getCredentialVersion());
        }
        return new VirtualDeviceAckResult()
                .setValid(true)
                .setStatus(!bound
                        ? DeviceClientStatusEnum.UNBOUND.getCode()
                        : (enabled ? DeviceClientStatusEnum.ACTIVE.getCode() : DeviceClientStatusEnum.DISABLED.getCode()))
                .setEnable(enabled)
                .setBound(bound)
                .setCredentialVersion(credentialVersion)
                .setNeedReactivate(needReactivate);
    }

    /// 凭证失效响应(设备不存在/未激活/验签失败统一口径, 不暴露具体差异)
    private VirtualDeviceAckResult revoked() {
        return new VirtualDeviceAckResult()
                .setValid(false)
                .setStatus(DeviceClientStatusEnum.REVOKED.getCode())
                .setEnable(false)
                .setBound(false);
    }

    /// 补打取数(终端待打印队列只存元数据, 补打时按支付单号实时取平台渲染字节流)
    ///
    /// 验签/防重放与 [ack] 同口径(签名原料多拼 bizNo 防业务参数篡改); 验签通过后:
    /// 订单按支付单号忽略租户查询(免登录无租户上下文), 归属商户须与设备一致(防跨商户枚举);
    /// 渲染走 [PrintPushService] 与 pay.success 推送同一管线, 版式为最新内置版式
    /// (58/80 双流下发, 终端按本地纸宽设置挑流, 与推送报文同构)。
    /// 验签失败统一模糊文案(不暴露设备是否存在), 设备维度限流兜底防刷。
    public VirtualDeviceReprintResult reprint(VirtualDeviceReprintParam param) {
        checkReprintRateLimit(param.getDeviceNo());
        DeviceVirtual device = virtualDeviceManager.findByDeviceNo(param.getDeviceNo()).orElse(null);
        if (device == null || StrUtil.isBlank(device.getSecret())) {
            throw reprintFail();
        }
        // 时间窗口校验(防重放, 文案与回执同口径)
        long now = System.currentTimeMillis();
        if (Math.abs(now - param.getTs()) > ACK_TS_WINDOW_MILLIS) {
            // 设备: 请求已过期, 请校准设备时间后重试
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.requestExpired");
        }
        // HMAC-SHA256(deviceNo + ts + nonce + bizNo) 验签(比回执多拼 bizNo, 防取数键被篡改)
        String expected = hmacSha256(device.getSecret(),
                param.getDeviceNo() + param.getTs() + param.getNonce() + param.getBizNo());
        if (!expected.equalsIgnoreCase(param.getSign())) {
            throw reprintFail();
        }
        virtualOnlineTracker.consumeAckNonce(param.getNonce());
        // HTTP 到达即在线, 与回执同口径刷新在线窗口
        virtualOnlineTracker.refreshOnline(param.getDeviceNo());
        // 归属校验: 订单所属商户须与设备绑定商户一致(免登录无租户上下文, 忽略租户查询)
        PayTrade trade = payTradeManager.findByTradeNoNotTenant(param.getBizNo())
                .orElseThrow(this::reprintFail);
        MchAppInfo app = mchAppInfoManager.findByAppIdNotTenant(trade.getAppId()).orElse(null);
        if (app == null || !StrUtil.equals(app.getMchNo(), device.getMchNo())) {
            throw reprintFail();
        }
        String mchName = merchantInfoManager.findByMchNoNotTenant(app.getMchNo())
                .map(MerchantInfo::getMchName)
                .orElse("");
        String outBizNo = printPushService.resolveOutBizNo(trade);
        // 版式取交易应用的联动配置代码(读取失败按未配置处理, 消费侧回落内置 standard)
        String templateCode = findTemplateCodeQuietly(app.getAppId());
        DeviceNotifyPayload payload = new DeviceNotifyPayload()
                .setTrade(trade)
                .setMchNo(app.getMchNo())
                .setMchName(mchName)
                .setAppName(app.getAppName())
                .setOutBizNo(outBizNo)
                .setTemplateCode(templateCode);
        PrintPushData printData = printPushService.render(payload, device, PrintGoodsContext.empty());
        if (printData == null) {
            // 设备: 补打渲染失败(统一模糊文案, 不暴露配置细节)
            log.warn("虚拟设备补打渲染失败: deviceNo={}, bizNo={}, appId={}",
                    param.getDeviceNo(), param.getBizNo(), app.getAppId());
            throw reprintFail();
        }
        log.info("虚拟设备补打取数: deviceNo={}, bizNo={}", param.getDeviceNo(), param.getBizNo());
        return new VirtualDeviceReprintResult()
                .setEscposBase64(printData.escposBase64())
                .setEscposBase64W80(printData.escposBase64W80())
                .setPaperWidth(printData.paperWidth());
    }

    /// 试打取数(终端"测试打印"用: 取当前生效版式渲染的样例小票, 与线上真实收款小票同一版式)
    ///
    /// APP 的测试打印由平台渲染(设备纸宽与应用配置版式为平台权威, 未配置回落内置 standard),
    /// 终端仅负责把字节流交给打印机(哑管道语义不破)。
    /// 验签/防重放与回执同口径(签名原料多拼 paperWidth), 验签失败统一模糊文案。
    public VirtualDevicePrintPreviewResult previewPrint(VirtualDevicePrintPreviewParam param) {
        checkPreviewRateLimit(param.getDeviceNo());
        DeviceVirtual device = virtualDeviceManager.findByDeviceNo(param.getDeviceNo()).orElse(null);
        if (device == null || StrUtil.isBlank(device.getSecret())) {
            throw previewFail();
        }
        long now = System.currentTimeMillis();
        if (Math.abs(now - param.getTs()) > ACK_TS_WINDOW_MILLIS) {
            // 设备: 请求已过期, 请校准设备时间后重试
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.requestExpired");
        }
        String expected = hmacSha256(device.getSecret(),
                param.getDeviceNo() + param.getTs() + param.getNonce() + previewPaperWidth(param));
        if (!expected.equalsIgnoreCase(param.getSign())) {
            throw previewFail();
        }
        virtualOnlineTracker.consumeAckNonce(param.getNonce());
        virtualOnlineTracker.refreshOnline(param.getDeviceNo());
        String mchName = StrUtil.isBlank(device.getMchNo()) ? ""
                : merchantInfoManager.findByMchNoNotTenant(device.getMchNo()).map(MerchantInfo::getMchName).orElse("");
        String appName = StrUtil.isBlank(device.getAppId()) ? ""
                : mchAppInfoManager.findByAppIdNotTenant(device.getAppId()).map(MchAppInfo::getAppName).orElse("");
        // 版式取设备绑定应用的联动配置代码(读取失败按未配置处理, 渲染侧回落内置 standard)
        String templateCode = findTemplateCodeQuietly(device.getAppId());
        // 样例单与运营端试打同源(12.50/微信/带商品明细), 纸宽默认 58mm, 允许终端本次覆盖
        PrintPushService.TestSample sample = new PrintPushService.TestSample(
                1250L, "wechat", "", templateCode, Boolean.TRUE, previewPaperWidth(param));
        TestPrintPreviewResult preview = printPushService.previewTest(device, mchName, appName, sample);
        if (preview == null) {
            throw previewFail();
        }
        log.info("虚拟设备试打取数: deviceNo={}, template={}", param.getDeviceNo(), preview.templateCode());
        // APP 试打要真字节: 预览出参不携带指令流, 在此(编排层)对同一行模型编译,
        // 与推送打印同一条编译路径, 载荷字节不变
        return new VirtualDevicePrintPreviewResult()
                .setEscposBase64(RenderResult.encodeBase64(EscPosCompiler.compile(preview.lineModel())))
                .setPaperWidth(preview.paperWidth())
                .setTemplateCode(preview.templateCode())
                .setTemplateName(preview.templateName());
    }

    /// 应用联动配置版式代码静默读取(免登录无租户上下文; 查询失败/未配置返回 null 由渲染侧回落默认)
    private String findTemplateCodeQuietly(String appId) {
        if (StrUtil.isBlank(appId)) {
            return null;
        }
        try {
            return mchAppDeviceConfigManager.findByAppIdNotTenant(appId)
                    .map(DeviceMchAppConfig::getTemplateCode)
                    .orElse(null);
        } catch (Exception e) {
            // 设备: 应用联动配置查询失败, 按未配置处理(回落内置版式)
            log.warn("应用联动配置查询失败, 按未配置处理: appId={}", appId, e);
            return null;
        }
    }

    /// 试打取数纸宽(空=0, 与终端签名原料拼接口径一致; 0 表示 58mm 默认)
    private static int previewPaperWidth(VirtualDevicePrintPreviewParam param) {
        return param.getPaperWidth() == null ? 0 : param.getPaperWidth();
    }

    /// 试打取数失败异常(设备不存在/验签失败/渲染失败统一口径, 不暴露差异防枚举)
    private OperationFailException previewFail() {
        // 设备: 试打数据获取失败, 请检查设备状态
        return new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.previewFail");
    }

    /// 试打取数限流(设备维度分钟窗口)
    private void checkPreviewRateLimit(String deviceNo) {
        String minuteLabel = DateUtil.format(DateUtil.date(), "yyyyMMddHHmm");
        String key = "daxpay:device:audio:preview:" + minuteLabel + ":device:" + deviceNo;
        long count = periodCounterService.increment(key, Duration.ofMinutes(2));
        if (count > PREVIEW_DEVICE_LIMIT) {
            // 设备: 操作过于频繁, 请稍后再试
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.rateLimited");
        }
    }

    /// 补打失败异常(设备/订单不存在、验签失败、归属不符统一口径, 不暴露差异防枚举)
    private OperationFailException reprintFail() {
        // 设备: 补打数据获取失败
        return new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.reprintFail");
    }

    /// 补打取数限流(设备维度分钟窗口, 覆盖自动清积压与手动补打)
    private void checkReprintRateLimit(String deviceNo) {
        String minuteLabel = DateUtil.format(DateUtil.date(), "yyyyMMddHHmm");
        String key = "daxpay:device:audio:reprint:" + minuteLabel + ":device:" + deviceNo;
        long count = periodCounterService.increment(key, Duration.ofMinutes(2));
        if (count > REPRINT_DEVICE_LIMIT) {
            // 设备: 操作过于频繁, 请稍后再试
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.rateLimited");
        }
    }

    /// HMAC-SHA256 签名(十六进制小写, 终端同参计算)
    public static String hmacSha256(String secret, String data) {
        return SecureUtil.hmacSha256(secret.getBytes(StandardCharsets.UTF_8))
                .digestHex(data);
    }

    /// 激活限流检查(分钟窗口自增, 超限抛错)
    private void checkActivateRateLimit(String dimension, int limit) {
        String minuteLabel = DateUtil.format(DateUtil.date(), "yyyyMMddHHmm");
        String key = "daxpay:device:audio:activate:" + minuteLabel + ":" + dimension;
        long count = periodCounterService.increment(key, Duration.ofMinutes(2));
        if (count > limit) {
            // 设备: 操作过于频繁, 请稍后再试
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.rateLimited");
        }
    }

    /// 激活失败异常(设备不存在/未绑定统一口径, 不暴露设备是否存在)
    private static OperationFailException activateFail() {
        // 设备: 设备码不正确
        return new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.activateFailed");
    }
}
