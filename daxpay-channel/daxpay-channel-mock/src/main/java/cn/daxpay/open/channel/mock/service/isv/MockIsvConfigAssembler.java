package cn.daxpay.open.channel.mock.service.isv;

import cn.daxpay.open.channel.mock.client.credential.MockSdkCredential;
import cn.daxpay.open.channel.mock.dao.isv.MockIsvChannelMerchantManager;
import cn.daxpay.open.channel.mock.dao.isv.MockIsvInfoManager;
import cn.daxpay.open.channel.mock.entity.isv.MockIsvChannelMerchant;
import cn.daxpay.open.channel.mock.entity.isv.MockIsvInfo;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Mock 服务商通道凭证组装器
///
/// 从服务商归属绑定([MockIsvChannelMerchant], 按通道商户号反解) + 服务商信息([MockIsvInfo])
/// 组装服务商模式的通道调用凭证(mock 零资料无业务凭证, 仅回填 isvNo/sandbox)。
/// 字段映射: isvNo ← [MockIsvInfo#getIsvNo] / sandbox 恒 true。
///
/// 供服务商产品(mock_isv)支付/退款/同步/关闭/分账/回调链路组装通道调用凭证
/// (经 [cn.daxpay.open.channel.mock.service.MockCredentialSupport] 按 product 分发)。
/// open 版通用主表无 isv_no 列, 归属只能经私有绑定表 isv_id 反解服务商 Id 再取编号。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockIsvConfigAssembler {

    private final MockIsvChannelMerchantManager mockIsvChannelMerchantManager;
    private final MockIsvInfoManager mockIsvInfoManager;

    /// 组装服务商模式的通道调用凭证(下发给子应用)
    ///
    /// @param channelMchNo 通道商户号(服务商归属绑定定位主键)
    /// @return mock SDK 凭证(服务商模式, isvNo 已填充, sandbox 恒 true)
    public MockSdkCredential buildConfig(String channelMchNo) {
        // 服务商归属绑定(取 isvId), 查不到绑定时 fail-fast
        MockIsvChannelMerchant channelMerchant = mockIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                // mock: 模拟服务商通道商户绑定不存在, 请检查通道商户配置
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.mock.isvBindingNotExist", channelMchNo));
        // 归属模拟服务商(取 isvNo)
        MockIsvInfo isvInfo = mockIsvInfoManager.findById(channelMerchant.getIsvId())
                // mock: 通道商户归属的模拟服务商不存在, 请检查服务商配置
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.channel.mock.isvOfChannelMerchantNotExist", channelMerchant.getIsvId()));
        MockSdkCredential credential = new MockSdkCredential();
        credential.setIsvNo(isvInfo.getIsvNo());
        // mock 恒为沙箱语义(业务数据标记)
        credential.setSandbox(true);
        return credential;
    }
}
