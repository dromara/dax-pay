package cn.daxpay.open.channel.mock.service;

import cn.daxpay.open.channel.mock.client.MockChannelClient;
import cn.daxpay.open.channel.mock.client.credential.MockSdkCredential;
import cn.daxpay.open.channel.mock.client.req.MockAllocReceiverReq;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Mock 通道分账接收方通道服务
///
/// 通过 [MockChannelClient] 调用独立 mock 子应用完成接收方通道侧注册/删除,
/// 接线方式照抄支付宝 [cn.daxpay.open.channel.alipay.service.payment.alloc.AlipayAllocReceiverChannelService]:
/// 绑定/解绑均为同步调用, 失败抛业务异常由编排层落库失败状态。
///
/// 与支付宝的差异: mock 零资料接入, 绑定/解绑在 mock 侧恒成功仅留痕,
/// 失败仅来自传输/子应用不可达等基础设施异常。
///
/// 唯一调用方为运营端台账服务 [cn.daxpay.open.channel.mock.service.alloc.MockAllocReceiverAdminService]
/// (新增即绑定/重绑/解绑), 本类只负责通道调用与错误包装, 不碰台账状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockAllocReceiverService {

    private final MockChannelClient mockChannelClient;

    /// 绑定接收方(mock 侧恒成功仅留痕)
    public void bind(MockAllocReceiverReq req) {
        DaxResult<Void> result = mockChannelClient.allocReceiverBind(req);
        if (result.getCode() != 0) {
            // 模拟通道: 分账接收方绑定失败
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL,
                    "error.channel.mock.allocReceiverBindFailed", result.getMsg());
        }
    }

    /// 解绑接收方(mock 侧恒成功仅留痕)
    public void unbind(MockAllocReceiverReq req) {
        DaxResult<Void> result = mockChannelClient.allocReceiverUnbind(req);
        if (result.getCode() != 0) {
            // 模拟通道: 分账接收方解绑失败
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL,
                    "error.channel.mock.allocReceiverUnbindFailed", result.getMsg());
        }
    }

    /// 组装绑定/解绑请求(凭证须由 MockCredentialSupport 产出, 含网关签名密钥)
    ///
    /// one 侧按凭证内的商户私钥签网关请求, 空壳凭证会在签名阶段失败, 故不再本地 new 空壳。
    public MockAllocReceiverReq buildReq(String account, String name, MockSdkCredential credential) {
        return new MockAllocReceiverReq()
                .setAccount(account)
                .setName(name)
                .setCredential(credential);
    }
}
