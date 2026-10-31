package cn.daxpay.open.channel.mock.dao.isv;

import cn.daxpay.open.channel.mock.entity.isv.MockIsvInfo;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # Mock 模拟服务商信息
///
@Mapper
public interface MockIsvInfoMapper extends MPJBaseMapper<MockIsvInfo> {
}
