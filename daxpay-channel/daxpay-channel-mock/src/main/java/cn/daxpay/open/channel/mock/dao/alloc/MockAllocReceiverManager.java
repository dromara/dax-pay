package cn.daxpay.open.channel.mock.dao.alloc;

import cn.daxpay.open.channel.mock.entity.alloc.MockAllocReceiver;
import cn.daxpay.open.channel.mock.param.alloc.MockAllocReceiverQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

/// # Mock 通道分账接收方管理
///
/// 台账读写收口: 分页、同通道商户同类型同账号查重(按明文哈希等值)、按主键定位。
@Repository
public class MockAllocReceiverManager extends BaseManager<MockAllocReceiverMapper, MockAllocReceiver> {

    /// 分页
    public Page<MockAllocReceiver> page(PageParam pageParam, MockAllocReceiverQuery query) {
        Page<MockAllocReceiver> mpPage = MpUtil.getMpPage(pageParam, MockAllocReceiver.class);
        QueryWrapper<MockAllocReceiver> generator = QueryGenerator.generator(query);
        return this.page(mpPage, generator);
    }

    /// 校验同一通道商户下同类型同账号接收方不重复(按明文哈希等值匹配)
    public boolean existsByChannelMchNoAndTypeAndHash(String channelMchNo, String receiverType, String accountHash) {
        return lambdaQuery()
                .eq(MockAllocReceiver::getChannelMchNo, channelMchNo)
                .eq(MockAllocReceiver::getReceiverType, receiverType)
                .eq(MockAllocReceiver::getAccountHash, accountHash)
                .exists();
    }
}
