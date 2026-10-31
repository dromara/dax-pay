package cn.daxpay.open.channel.mock.dao.isv;

import cn.daxpay.open.channel.mock.entity.isv.MockIsvChannelMerchant;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # Mock 模拟服务商通道商户绑定
///
@Mapper
public interface MockIsvChannelMerchantMapper extends MPJBaseMapper<MockIsvChannelMerchant> {
}
