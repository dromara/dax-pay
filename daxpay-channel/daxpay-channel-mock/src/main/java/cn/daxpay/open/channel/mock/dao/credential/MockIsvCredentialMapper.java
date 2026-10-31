package cn.daxpay.open.channel.mock.dao.credential;

import cn.daxpay.open.channel.mock.entity.credential.MockIsvCredential;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # Mock 模拟服务商接入密钥
///
@Mapper
public interface MockIsvCredentialMapper extends MPJBaseMapper<MockIsvCredential> {
}
