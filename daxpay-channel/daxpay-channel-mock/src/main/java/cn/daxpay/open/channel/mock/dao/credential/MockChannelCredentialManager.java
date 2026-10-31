package cn.daxpay.open.channel.mock.dao.credential;

import cn.daxpay.open.channel.mock.entity.credential.MockChannelCredential;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # Mock 模拟通道网关签名密钥配置
///
/// 全局单行配置(id 固定 1)的数据访问管理器。
@Repository
public class MockChannelCredentialManager extends BaseManager<MockChannelCredentialMapper, MockChannelCredential> {

    /// 单行配置固定主键
    public static final long SINGLETON_ID = 1L;

    /// 查询单行配置(无行返回 empty)
    public Optional<MockChannelCredential> findSingleton() {
        return findById(SINGLETON_ID);
    }
}
