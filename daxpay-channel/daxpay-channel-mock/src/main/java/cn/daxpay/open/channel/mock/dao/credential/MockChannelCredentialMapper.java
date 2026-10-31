package cn.daxpay.open.channel.mock.dao.credential;

import cn.daxpay.open.channel.mock.entity.credential.MockChannelCredential;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # Mock 模拟通道网关签名密钥配置
///
@Mapper
public interface MockChannelCredentialMapper extends MPJBaseMapper<MockChannelCredential> {
}
