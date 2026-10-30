package cn.daxpay.open.device.comm;

import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 设备播报/打印载荷(厂商中立业务事实)
///
/// 对虚拟设备 client 下发指令时携带的业务事实: 只描述"发生了什么交易、渲染小票需要哪些上下文",
/// 不含任何传输实现语义(如何渲染/加密/传输由推送 client 自行决定)。
/// 试播试打复用本载荷, trade 为样例交易。
@Data
@Accessors(chain = true)
public class DeviceNotifyPayload {

    /// 支付交易(试播试打时为样例交易)
    private PayTrade trade;

    /// 商户号
    private String mchNo;

    /// 商户名称(播报前缀与小票票头)
    private String mchName;

    /// 应用名称(小票渲染变量)
    private String appName;

    /// 商户单号(业务容器, 小票渲染变量)
    private String outBizNo;

    /// 门店名称(小票渲染变量 storeName, 支付单无门店时为空串)
    private String storeName;

    /// 应用打印版式代码(开源版无模板表: 内置版式常量解析, 空=standard 标准小票; 试播为空)
    private String templateCode;
}
