package cn.daxpay.open.device.virtual.client.service;

import cn.daxpay.open.device.comm.AudioBroadcastMessage;
import cn.daxpay.open.device.comm.DeviceCommand;
import cn.daxpay.open.device.comm.DeviceNotifyPayload;
import cn.daxpay.open.device.comm.PrintPushData;
import cn.daxpay.open.device.print.service.PrintPushService;
import cn.daxpay.open.device.print.template.engine.model.PrintGoodsContext;
import cn.daxpay.open.platform.common.artemis.service.ArtemisTemplateService;
import cn.daxpay.open.platform.common.config.encrypt.ChannelAesGcmEncryptor;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/// # 虚拟设备推送 client(平台 MQTT 直连)
///
/// vendor=VIRTUAL 的推送实现, 承接虚拟设备域下发的播报消息:
/// 播报消息组装/信封加密/MQTT 发布全部收敛在本实现内,
/// 调用侧只描述"对哪台设备下什么指令、业务事实是什么", 不感知虚拟设备协议细节。
///
/// ## 消息链路
///
/// 1. 播报载荷序列化为 JSON, 以 AES-256-GCM(每设备 secret)加密为信封 v1 的 data
/// 2. 设备订阅斜杠主题 `daxpay/device/audio/{deviceNo}`(激活凭证下发), broker 按 MQTT 语义
///    映射为多段 core 地址 `daxpay.device.audio.{deviceNo}`; 平台以 JMS Topic 发布到同一多段
///    地址, 由 broker 直接路由到订阅队列, 投递时主题名由 Artemis 还原为斜杠形式
///    (ARTEMIS-4532, Artemis 2.33.0 起)
/// 3. 主题名含 audio 系命名历史, 承载虚拟设备全部形态(播报/打印一体)
///
/// ## 信封结构(明文壳)
///
/// - `v/cmd/msgId/ts/enc` 为明文, msgId 供终端去重与回执关联
/// - `data` 为密文 `Base64(IV(12)||cipher+tag)`, 复用 [ChannelAesGcmEncryptor] 同款算法参数,
///   终端侧 Android javax.crypto 对称实现
///
/// ## 线上契约
///
/// - 信封与各 cmd 载荷的 record 定义见本类底部「线上契约」区, 与 APP 端 `CommandEnvelope.kt`
///   的 data class 逐一镜像; 改字段名/增删字段须双侧同步(APP 解析容忍新增字段, 不容忍改名)
/// - 序列化走独立 [PROTOCOL_MAPPER](NON_NULL, null 字段不出现), 不复用 Spring 全局
///   ObjectMapper, 防全局日期等配置渗入设备协议
///
/// ## 编排接入(B 波 2026-10-24 已接)
///
/// - `dispatchPaySuccess`(支付成功播报打印一体)为统一指令入口, 由
///   [cn.daxpay.open.device.audio.consumer.DevicePaySuccessConsumer] 在支付成功旁路逐台调用;
///   开源版仅虚拟设备一条产品线, 不引入厂商 SPI/DevicePushDispatcher(plus 多厂商分发不移植),
///   试播试打/纯打印/踢出继续走本类既有方法。
@RequiredArgsConstructor
@Slf4j
@Component
public class VirtualDevicePushClient {

    /// 设备端 MQTT 订阅主题前缀(斜杠形式, 激活下发与设备订阅使用)
    ///
    /// Artemis 2.5x(ARTEMIS-4532, 2.33.0 起)按 MQTT 官方语义处理主题: `/` 是层级分隔符,
    /// `.` 属字面字符并被转义为 `\.`; 设备主题必须用斜杠, 订阅后落到多段 core 地址
    /// `daxpay.device.audio.{deviceNo}`, 与平台发布地址 [CORE_ADDRESS_PREFIX] 完全一致。
    /// 运维面板「MQTT 接入」页展示的 `daxpay.device.` 是 core 地址前缀(ACL 匹配用),
    /// 与本常量是同一地址的两种界面形式(daxpay/device/... ↔ daxpay.device....), 两侧无运行时联动,
    /// 改值须人工同步面板常量与已部署环境的 ACL 段。
    public static final String DEVICE_TOPIC_PREFIX = "daxpay/device/audio/";

    /// 平台 JMS 发布地址前缀(点号多段 core 地址)
    ///
    /// JMS Topic(pubSubDomain=true, multicast)发送到该地址, 与 MQTT 斜杠订阅创建的 multicast
    /// 队列地址一致, 由 broker 直接路由; 投递给设备时主题名由 Artemis 反向还原为斜杠形式。
    /// 禁止改回点号字面转义形式(daxpay\.device\.audio\.)——那会落到单段转义地址,
    /// 与斜杠订阅队列不匹配, 消息静默无人接收; 且面板 address-setting `daxpay.device.#`
    /// 对转义地址不生效, 离线设备队列不会被自动清理。
    private static final String CORE_ADDRESS_PREFIX = "daxpay.device.audio.";

    /// 命令名: 支付成功播报(含带 printData 的播报打印一体消息)
    public static final String CMD_PAY_SUCCESS = "pay.success";

    /// 命令名: 试播(运营端端到端验证, 不计入日计数)
    public static final String CMD_AUDIO_TEST = "audio.test";

    /// 命令名: 纯打印小票(不播报, 终端直接入打印队列)
    public static final String CMD_PRINT_TICKET = "print.ticket";

    /// 命令名: 主动踢出(运营作废凭证时通知在线设备立即下线, 明文不加密)
    public static final String CMD_KICK = "kick";

    /// 线上协议序列化器(NON_NULL: null 字段不出现, 与 hutool JSONObject 默认忽略 null 等价)
    ///
    /// 独立实例不复用 Spring 全局 ObjectMapper, 防全局日期等配置渗入设备协议。
    private static final ObjectMapper PROTOCOL_MAPPER = new ObjectMapper().setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);

    private final ArtemisTemplateService artemisTemplateService;
    private final VirtualDeviceStatService virtualDeviceStatService;
    private final PrintPushService printPushService;

    /// 统一指令入口: 支付成功播报打印一体(由 [cn.daxpay.open.device.audio.consumer.DevicePaySuccessConsumer] 逐台调用)
    ///
    /// 载荷组装(A-wave 设备引用 + 业务事实) → 渲染(指令要求附带小票时经
    /// [PrintPushService] 按内置版式渲染 58/80 双流 printData) → 组装
    /// [AudioBroadcastMessage] → 走 [#broadcast] 加密发布。
    /// 支付旁路语义: 任何异常(渲染失败/发布失败)只记 ERROR 不上抛——旁路不反压消费链路,
    /// 单台失败不影响同商户其余设备; 播报与打印为同一条消息, 渲染失败时降级为只播报。
    public void dispatchPaySuccess(DeviceCommand command) {
        DeviceNotifyPayload payload = command.getPayload();
        try {
            AudioBroadcastMessage message = AudioBroadcastMessage.paySuccess(
                    payload.getTrade(), payload.getMchNo(), payload.getMchName(), command.getDeviceNo());
            if (command.isPrint()) {
                // 打印开关由消费侧两层求值后落在指令 print 标记上(应用开关 AND 指令工厂默认 true)
                message.setPrintData(printPushService.render(payload, command.getTerminal(), PrintGoodsContext.empty()));
            }
            broadcast(command.getDeviceNo(), command.getSecret(), message);
        } catch (Exception e) {
            // 设备: 支付成功推送失败(旁路, 记日志不反压)
            log.error("支付成功播报推送失败: deviceNo={}, tradeNo={}",
                    command.getDeviceNo(), payload.getTrade().getTradeNo(), e);
        }
    }

    /// 下发支付成功播报(凭设备号+密钥)
    public void broadcast(String deviceNo, String secret, AudioBroadcastMessage message) {
        publish(deviceNo, secret, CMD_PAY_SUCCESS, message.getMsgId(), toWire(message));
    }

    /// 下发试打消息(播报+打印一体, 但携带 test 标记 → 终端只播报/打印, 不记账不弹浮层)
    ///
    /// 与 [broadcast] 的唯一差异是线上载荷多一个 `test:true`; 若不标记, 试打会被终端当成真实收款
    /// 写入本机流水与今日汇总(历史缺陷: 试一张 12.50, 今日收款虚增 12.50)。
    /// 返回 msgId 供调用方回执轮询比对。
    public String broadcastTest(String deviceNo, String secret, AudioBroadcastMessage message) {
        publish(deviceNo, secret, CMD_PAY_SUCCESS, message.getMsgId(), toWire(message, true));
        return message.getMsgId();
    }

    /// 领域消息映射为线上载荷(msgId 上移信封不入加密 data; printData 缺失=本次只播报不打印)
    static PaySuccessData toWire(AudioBroadcastMessage message) {
        return toWire(message, null);
    }

    /// 领域消息映射为线上载荷(test 非空才出现, 正常收款消息字节形状保持不变)
    static PaySuccessData toWire(AudioBroadcastMessage message, Boolean test) {
        return new PaySuccessData(message.getEvent(), message.getBizNo(), message.getChannel(),
                message.getAmount(), message.getMchNo(), message.getMchName(), message.getTs(),
                message.getPrintData(), test);
    }

    /// 下发试播(运营端/商户端端到端验证; 金额与收款方式可指定, 渠道先归一化为终端词元键)
    ///
    /// 缺省为 1 元/其他渠道(由调用方规整)。
    /// 返回 msgId 供调用方回执轮询比对。
    public String testBroadcast(String deviceNo, String secret, long amountFen, String channel) {
        String msgId = IdUtil.fastSimpleUUID();
        publish(deviceNo, secret, CMD_AUDIO_TEST, msgId,
                new AudioTestData(amountFen, StrUtil.blankToDefault(channel, "other")));
        return msgId;
    }

    /// 下发试播(旧签名: 固定 1 元/其他渠道, 保留给未接参数的调用方)
    public String testBroadcast(String deviceNo, String secret) {
        return testBroadcast(deviceNo, secret, 100L, "other");
    }

    /// 下发纯打印小票(凭设备号+密钥, cmd=print.ticket 不播报; 终端直接入打印队列)
    ///
    /// 与 [broadcast] 同信封同加密, data 仅含平台渲染的 printData(ESC/POS 哑管道);
    /// 终端回执走同一条 ack 通道, cmd 非 pay.success 不回写收款回执三列。
    public void printTicket(String deviceNo, String secret, String msgId, PrintPushData printData) {
        printTicket(deviceNo, secret, msgId, printData, false);
    }

    /// 下发纯打印小票(manual=true 表示人工试打: 终端不校验"自动打印"开关, 只校验是否已选打印机)
    public void printTicket(String deviceNo, String secret, String msgId, PrintPushData printData, boolean manual) {
        publish(deviceNo, secret, CMD_PRINT_TICKET, msgId, new PrintOnlyData(printData, manual ? Boolean.TRUE : null));
    }

    /// 下发主动踢出(运营作废凭证时通知在线设备立即下线)
    ///
    /// 与业务推送的差异(kick 的四个特殊约定):
    /// - **明文信封**(enc=none, data 为含中文 reason 的明文 JSON): 踢出与凭证作废同时发生,
    ///   设备 secret 已清/将清, 终端无密可解; 安全性由 broker 主题发布权限保证
    ///   (设备主题仅平台账号可发布, 终端只读订阅)
    /// - **不回 ack**(单向通知, 无回执语义): 终端收到后立即停止心跳并下线;
    ///   离线设备收不到, 由既有 ack 自愈兜底(最差下一次心跳发现凭证失效)
    /// - **不计入日推送统计**: todayPushCount 只统计 pay.success/print.ticket 业务推送,
    ///   故不走带统计的 [publish] 路径, 单独组装发送
    /// - **发送异常只记日志不抛**: kick 是尽力而为, 失败同样由 ack 自愈兜底, 不阻断运营操作事务
    public void kick(String deviceNo, String reason) {
        // msgId 携带设备号+时间戳保证唯一, 防终端按 msgId 去重时误杀连续两次踢出
        CommandEnvelope envelope = CommandEnvelope.plain(
                "kick:" + deviceNo + ":" + System.currentTimeMillis(), new KickData(reason));
        try {
            artemisTemplateService.sendTopic(publishAddress(deviceNo), toJson(envelope));
        } catch (Exception e) {
            log.warn("虚拟设备踢出消息发送失败, deviceNo={}", deviceNo, e);
        }
    }

    /// 设备订阅主题名(激活时随凭证下发, 设备端原样订阅斜杠形式)
    public static String topic(String deviceNo) {
        return DEVICE_TOPIC_PREFIX + deviceNo;
    }

    /// 平台发布地址(点号多段 core 地址, 与 MQTT 斜杠订阅队列同址, 仅平台发送侧使用)
    static String publishAddress(String deviceNo) {
        return CORE_ADDRESS_PREFIX + deviceNo;
    }

    /// 组装加密信封并发布(data 先序列化再加密)
    private void publish(String deviceNo, String secret, String cmd, String msgId, Object data) {
        CommandEnvelope envelope = CommandEnvelope.encrypted(cmd, msgId, encrypt(secret, toJson(data)));
        artemisTemplateService.sendTopic(publishAddress(deviceNo), toJson(envelope));
        // 发布成功计日推送笔数(pay.success 含带 printData 的打印单、print.ticket 纯打印单都算;
        // audio.test 试播不计入)
        if (!CMD_AUDIO_TEST.equals(cmd)) {
            virtualDeviceStatService.onPush(deviceNo);
        }
    }

    /// AES-256-GCM 加密载荷(secret 须为激活时生成的 32 字符密钥)
    private String encrypt(String secret, String data) {
        return new ChannelAesGcmEncryptor(secret).encrypt(data);
    }

    /// 序列化线上协议对象(载荷为纯 String/Long/record, 序列化失败属编码 bug, 直接中断)
    static String toJson(Object value) {
        try {
            return PROTOCOL_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("虚拟设备协议序列化失败: " + value.getClass().getSimpleName(), e);
        }
    }

    // ==================== 线上契约(与 APP 端 CommandEnvelope.kt 镜像, 语义见类头) ====================

    /// 命令信封(数据面 MQTT 消息外壳, 明文; 结构语义见类头「信封结构」)
    ///
    /// enc=aes-gcm 时 data 为 Base64 密文串, enc=none 时 data 为明文对象(仅 kick)。
    record CommandEnvelope(int v, String cmd, String msgId, long ts, String enc, Object data) {

        /// 加密标记: AES-256-GCM(与 APP 端 CommandEnvelope 的 ENC_AES_GCM 常量对齐)
        static final String ENC_AES_GCM = "aes-gcm";

        /// 明文标记(仅 kick, 见 [#kick])
        static final String ENC_NONE = "none";

        /// 加密信封工厂(data 为密文串, 加密由 [publish] 完成)
        static CommandEnvelope encrypted(String cmd, String msgId, String encryptedData) {
            return new CommandEnvelope(1, cmd, msgId, System.currentTimeMillis(), ENC_AES_GCM, encryptedData);
        }

        /// 明文信封工厂(仅 kick)
        static CommandEnvelope plain(String msgId, Object plainData) {
            return new CommandEnvelope(1, CMD_KICK, msgId, System.currentTimeMillis(), ENC_NONE, plainData);
        }
    }

    /// pay.success 解密后载荷(对应 APP PaySuccessData; channel 可空, 空时 key 不出现, APP 兜底"收款到账")
    ///
    /// test=true 表示本条为试播/试打样例消息(APP 只播报与打印, 不记流水/不计今日汇总/不弹浮层);
    /// 正常收款消息该字段为 null, 序列化后不出现, 与旧版终端字节形状完全一致。
    record PaySuccessData(String event, String bizNo, String channel, Long amount,
            String mchNo, String mchName, Long ts, PrintPushData printData, Boolean test) {
    }

    /// audio.test 试播载荷(对应 APP AudioTestData, amount 单位分; channel 为终端词元键五值)
    record AudioTestData(long amount, String channel) {
    }

    /// print.ticket 纯打印载荷(对应 APP PrintOnlyData; manual=true 表示人工试打, 终端忽略自动打印开关)
    record PrintOnlyData(PrintPushData printData, Boolean manual) {
    }

    /// kick 明文载荷(reason 为中文原因, 终端展示后立即下线)
    record KickData(String reason) {
    }

    /// 设备未激活时抛出的统一异常(试播/试打同步运营操作用; 支付旁路自行判空跳过)
    public static OperationFailException notActivatedException() {
        // 设备: 设备尚未激活, 请先在 APP 完成激活
        return new OperationFailException(CommonCode.FAIL_CODE, "error.device.virtual.notActivated");
    }
}
