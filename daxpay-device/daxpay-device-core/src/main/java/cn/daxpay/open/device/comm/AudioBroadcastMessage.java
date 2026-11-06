package cn.daxpay.open.device.comm;

import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 音箱播报消息
///
/// 支付成功后由消费链路分发给虚拟设备 client 的播报载荷。
/// 本消息序列化后经 AES-256-GCM(每设备 secret)加密,
/// 以信封 v1(cmd=pay.success)发布到 core 地址 daxpay.device.audio.{deviceNo}
/// (设备侧订阅斜杠主题 daxpay/device/audio/{deviceNo}, 由 broker 映射到同一多段地址);
/// 事件范围 V1 仅支付成功(退款/关闭不播)。
@Data
@Accessors(chain = true)
public class AudioBroadcastMessage {

    /// 消息ID(信封 msgId, 终端回执关联与重复投递去重锚点)
    private String msgId;

    /// 事件类型(当前恒 pay.success)
    private String event;

    /// 支付单号
    private String bizNo;

    /// 支付渠道(wechat/alipay/douyin/union/..., 终端映射渠道语音前缀)
    private String channel;

    /// 金额(分)
    private Long amount;

    /// 商户号
    private String mchNo;

    /// 商户名称(小票票头)
    private String mchName;

    /// 支付完成时间(epoch millis)
    private Long ts;

    /// 平台渲染小票(设备启用打印时携带; 空=APP 无打印数据, 仅播报)
    private PrintPushData printData;

    /// 支付成功消息工厂(msgId=tradeNo:deviceNo 确定性去重)
    public static AudioBroadcastMessage paySuccess(PayTrade trade, String mchNo, String mchName, String deviceNo) {
        return new AudioBroadcastMessage()
                .setMsgId(trade.getTradeNo() + ":" + deviceNo)
                .setEvent("pay.success")
                .setBizNo(trade.getTradeNo())
                .setChannel(broadcastChannel(trade))
                .setAmount(trade.getAmount())
                .setMchNo(mchNo)
                .setMchName(mchName)
                .setTs(System.currentTimeMillis());
    }

    /// 播报渠道码(provider 优先取 C 端钱包, 聚合通道/provider 缺失时回落接入通道)
    ///
    /// APP 词元表用 union/wechat/alipay/douyin, 后端 union_pay 归一化为 union; 未知值由 APP 兜底"收款到账"。
    private static String broadcastChannel(PayTrade trade) {
        String code = StrUtil.blankToDefault(trade.getProvider(), trade.getChannel());
        if (StrUtil.isBlank(code)) {
            return null;
        }
        String normalized = code.trim().toLowerCase();
        return "union_pay".equals(normalized) ? "union" : normalized;
    }
}
