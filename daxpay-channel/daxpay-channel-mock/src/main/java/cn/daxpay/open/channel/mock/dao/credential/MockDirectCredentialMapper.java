package cn.daxpay.open.channel.mock.dao.credential;

import cn.daxpay.open.channel.mock.entity.credential.MockPayCredential;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # Mock 模拟通道直连商户接入密钥
///
@Mapper
public interface MockDirectCredentialMapper extends MPJBaseMapper<MockPayCredential> {
}
