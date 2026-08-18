package cn.daxpay.open.channel.alipay.dao.direct;

import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectAllocReceiver;
import cn.daxpay.open.channel.alipay.param.direct.AlipayDirectAllocReceiverQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

/// # 支付宝直连分账接收方管理
///
@Repository
public class AlipayDirectAllocReceiverManager extends BaseManager<AlipayDirectAllocReceiverMapper, AlipayDirectAllocReceiver> {

    /// 分页
    public Page<AlipayDirectAllocReceiver> page(PageParam pageParam, AlipayDirectAllocReceiverQuery query) {
        Page<AlipayDirectAllocReceiver> mpPage = MpUtil.getMpPage(pageParam, AlipayDirectAllocReceiver.class);
        QueryWrapper<AlipayDirectAllocReceiver> generator = QueryGenerator.generator(query);
        return this.page(mpPage, generator);
    }

    /// 校验同一通道商户下同类型同账号接收方不重复(按明文哈希等值匹配)
    public boolean existsByChannelMchNoAndTypeAndHash(String channelMchNo, String receiverType, String accountHash) {
        return lambdaQuery()
                .eq(AlipayDirectAllocReceiver::getChannelMchNo, channelMchNo)
                .eq(AlipayDirectAllocReceiver::getReceiverType, receiverType)
                .eq(AlipayDirectAllocReceiver::getAccountHash, accountHash)
                .exists();
    }

    /// 指定应用(alipay_direct_app 主键)是否被接收方记录引用(directAppRefId, 应用删除前校验)
    public boolean existsByDirectAppRefId(Long directAppRefId) {
        return lambdaQuery()
                .eq(AlipayDirectAllocReceiver::getDirectAppRefId, directAppRefId)
                .exists();
    }
}
