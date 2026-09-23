package cn.daxpay.open.payment.trade.runtime.bo;

import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 普通支付内部执行结果
///
/// 仅供内部流程(易支付等插件)使用: 组合对外响应 [NormalPayResult] 与内部容器 ID,
/// 使内部关联字段不依赖对外契约 DTO。
/// 不要将本类作为 API 出参序列化, 对外一律返回 [NormalPayResult]。
@Data
@Accessors(chain = true)
public class NormalPayExecutionResult {

    /// 对外支付响应(签名 DTO)
    private NormalPayResult result;

    /// 普通支付业务容器主键(NormalPayOrder.id), 供插件回写内部关联字段
    private Long containerId;
}
