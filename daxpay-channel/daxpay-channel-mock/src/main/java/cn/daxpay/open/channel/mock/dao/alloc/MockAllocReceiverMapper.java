package cn.daxpay.open.channel.mock.dao.alloc;

import cn.daxpay.open.channel.mock.entity.alloc.MockAllocReceiver;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # Mock 通道分账接收方 Mapper
///
@Mapper
public interface MockAllocReceiverMapper extends MPJBaseMapper<MockAllocReceiver> {
}
