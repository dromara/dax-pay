package cn.daxpay.open.channel.mock.dao.credential;

import cn.daxpay.open.channel.mock.entity.credential.MockIsvCredential;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/// # Mock 模拟服务商接入密钥
///
/// 服务商级独立接入密钥(每服务商一行)的数据访问管理器;
/// 「已配置」口径=商户私钥/平台公钥均非空(装配可用),
/// 口径判定住 [cn.daxpay.open.channel.mock.service.credential.MockIsvCredentialService]。
@Repository
public class MockIsvCredentialManager extends BaseManager<MockIsvCredentialMapper, MockIsvCredential> {

    /// 根据服务商编号查询(一服务商一行, 保存/查询/装配链路用; 空白不查库, 防 eq 条件被跳过误命中任意行)
    public MockIsvCredential findByIsvNo(String isvNo) {
        if (StrUtil.isBlank(isvNo)) {
            return null;
        }
        return findByField(MockIsvCredential::getIsvNo, isvNo).orElse(null);
    }

    /// 批量查询已配置接入密钥(商户私钥+平台公钥均非空)的服务商编号集合
    /// (服务商列表页 credentialConfigured 标记用, 一次 IN 查询避免 N+1)
    public Set<String> findConfiguredIsvNos(Collection<String> isvNos) {
        if (isvNos == null || isvNos.isEmpty()) {
            return Set.of();
        }
        return lambdaQuery()
                .in(MockIsvCredential::getIsvNo, isvNos)
                .isNotNull(MockIsvCredential::getMerchantPrivateKey)
                .ne(MockIsvCredential::getMerchantPrivateKey, "")
                .isNotNull(MockIsvCredential::getPlatformPublicKey)
                .ne(MockIsvCredential::getPlatformPublicKey, "")
                .list().stream()
                .map(MockIsvCredential::getIsvNo)
                .collect(Collectors.toSet());
    }

    /// 服务商是否已配置接入密钥(=商户私钥+平台公钥均非空, 详情页标记用)
    public boolean existConfiguredByIsvNo(String isvNo) {
        return lambdaQuery()
                .eq(MockIsvCredential::getIsvNo, isvNo)
                .isNotNull(MockIsvCredential::getMerchantPrivateKey)
                .ne(MockIsvCredential::getMerchantPrivateKey, "")
                .isNotNull(MockIsvCredential::getPlatformPublicKey)
                .ne(MockIsvCredential::getPlatformPublicKey, "")
                .exists();
    }
}
