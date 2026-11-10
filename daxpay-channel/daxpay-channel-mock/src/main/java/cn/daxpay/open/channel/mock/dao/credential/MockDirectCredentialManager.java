package cn.daxpay.open.channel.mock.dao.credential;

import cn.daxpay.open.channel.mock.entity.credential.MockPayCredential;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

/// # Mock 模拟通道直连商户接入密钥
///
/// 商户级拟真接入密钥(每通道商户一行)的数据访问管理器;
/// 「已配置」口径=商户私钥/平台公钥均非空(装配可用),
/// 口径判定住 [cn.daxpay.open.channel.mock.service.credential.MockDirectCredentialService]。
@Repository
public class MockDirectCredentialManager extends BaseManager<MockDirectCredentialMapper, MockPayCredential> {

    /// 根据通道商户号查询(一通道商户一行, 保存/查询/装配链路用)
    public MockPayCredential findByChannelMchNo(String channelMchNo) {
        return findByField(MockPayCredential::getChannelMchNo, channelMchNo).orElse(null);
    }
}
