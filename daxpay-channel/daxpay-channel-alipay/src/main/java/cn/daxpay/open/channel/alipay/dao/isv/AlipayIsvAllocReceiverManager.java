package cn.daxpay.open.channel.alipay.dao.isv;

import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvAllocReceiver;
import cn.daxpay.open.channel.alipay.param.isv.AlipayIsvAllocReceiverQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

/// # 支付宝服务商分账接收方管理
///
@Repository
public class AlipayIsvAllocReceiverManager extends BaseManager<AlipayIsvAllocReceiverMapper, AlipayIsvAllocReceiver> {

    /// 分页
    public Page<AlipayIsvAllocReceiver> page(PageParam pageParam, AlipayIsvAllocReceiverQuery query) {
        Page<AlipayIsvAllocReceiver> mpPage = MpUtil.getMpPage(pageParam, AlipayIsvAllocReceiver.class);
        QueryWrapper<AlipayIsvAllocReceiver> generator = QueryGenerator.generator(query);
        return this.page(mpPage, generator);
    }

    /// 校验同一通道商户下同类型同账号接收方不重复(按明文哈希等值匹配)
    public boolean existsByChannelMchNoAndTypeAndHash(String channelMchNo, String receiverType, String accountHash) {
        return lambdaQuery()
                .eq(AlipayIsvAllocReceiver::getChannelMchNo, channelMchNo)
                .eq(AlipayIsvAllocReceiver::getReceiverType, receiverType)
                .eq(AlipayIsvAllocReceiver::getAccountHash, accountHash)
                .exists();
    }
}
