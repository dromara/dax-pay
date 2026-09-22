package cn.daxpay.open.payment.unipay.param.assist;

import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 签名自检探针参数
///
/// 对接自检专用参数, 配合 `POST /unipay/ping`(签名自检探针)使用:
/// 持商户私钥的对接方按公共参数构造签名请求, 平台走完整验签链路后回显解析结果,
/// 用于一键判断「商户号/应用/私钥/签名串构造」是否正确、能否发起真实调用。
///
/// 除公共参数(mchNo/appId/reqId/reqTime/nonceStr/sign 等)外无业务字段:
/// - 不建单、不改状态, 零业务副作用;
/// - 参数不含 authCode/openId 等敏感数据, 验签失败时平台可安全回吐服务端待签串供比对。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "签名自检探针参数")
public class UnipayPingParam extends MerchantPaymentCommonParam {

}
