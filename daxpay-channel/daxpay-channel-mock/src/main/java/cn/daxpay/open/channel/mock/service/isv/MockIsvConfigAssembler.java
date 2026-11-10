package cn.daxpay.open.channel.mock.service.isv;

import cn.daxpay.open.channel.mock.client.credential.MockSdkCredential;
import cn.daxpay.open.channel.mock.entity.isv.MockIsvInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Mock 服务商通道凭证组装器
///
/// 服务商产品(mock_isv)无业务凭证, 组装时仅回填全局唯一模拟服务商的 isvNo + sandbox=true。
/// 多服务商模式取消后不再按通道商户号反查归属绑定表(绑定表仍保留建档留痕与删除清理用途),
/// isvNo 恒取单例([MockIsvInfoService#getSingleton])。
///
/// 供服务商产品(mock_isv)支付/退款/同步/关闭/分账/回调链路组装通道调用凭证
/// (经 [cn.daxpay.open.channel.mock.service.MockCredentialSupport] 按 product 分发)。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockIsvConfigAssembler {

    private final MockIsvInfoService mockIsvInfoService;

    /// 组装服务商模式的通道调用凭证(下发给子应用)
    ///
    /// @return mock SDK 凭证(服务商模式, isvNo 已填充全局唯一模拟服务商编号, sandbox 恒 true)
    public MockSdkCredential buildConfig() {
        MockIsvInfo isvInfo = mockIsvInfoService.getSingleton();
        MockSdkCredential credential = new MockSdkCredential();
        // isv_no 为 mock 网关协议字段(网关按它匹配商户档案), 单例固定编号
        credential.setIsvNo(isvInfo.getIsvNo());
        // mock 恒为沙箱语义(业务数据标记)
        credential.setSandbox(true);
        return credential;
    }
}
