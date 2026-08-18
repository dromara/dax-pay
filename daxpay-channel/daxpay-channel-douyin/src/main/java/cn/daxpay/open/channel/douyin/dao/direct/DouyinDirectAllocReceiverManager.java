package cn.daxpay.open.channel.douyin.dao.direct;

import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectAllocReceiver;
import cn.daxpay.open.channel.douyin.param.direct.DouyinDirectAllocReceiverQuery;
import cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverStatusEnum;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 抖音直连分账接收方管理
///
@Repository
public class DouyinDirectAllocReceiverManager extends BaseManager<DouyinDirectAllocReceiverMapper, DouyinDirectAllocReceiver> {

    /// 分页
    public Page<DouyinDirectAllocReceiver> page(PageParam pageParam, DouyinDirectAllocReceiverQuery query) {
        Page<DouyinDirectAllocReceiver> mpPage = MpUtil.getMpPage(pageParam, DouyinDirectAllocReceiver.class);
        QueryWrapper<DouyinDirectAllocReceiver> generator = QueryGenerator.generator(query);
        return this.page(mpPage, generator);
    }

    /// 校验同一通道商户下同类型同账号接收方不重复(按明文哈希等值匹配)
    public boolean existsByChannelMchNoAndTypeAndHash(String channelMchNo, String receiverType, String accountHash) {
        return lambdaQuery()
                .eq(DouyinDirectAllocReceiver::getChannelMchNo, channelMchNo)
                .eq(DouyinDirectAllocReceiver::getReceiverType, receiverType)
                .eq(DouyinDirectAllocReceiver::getAccountHash, accountHash)
                .exists();
    }

    /// 同商户下指定应用(douyinAppId)是否被接收方记录引用(channelAppId, 应用删除前校验)
    public boolean existsByMchNoAndChannelAppId(String mchNo, String channelAppId) {
        return lambdaQuery()
                .eq(DouyinDirectAllocReceiver::getMchNo, mchNo)
                .eq(DouyinDirectAllocReceiver::getChannelAppId, channelAppId)
                .exists();
    }

    /// 查询已绑定状态的接收方档案(分账发起时校验 openid 与发起应用一致性)
    public Optional<DouyinDirectAllocReceiver> findBoundByChannelMchNoAndTypeAndHash(
            String channelMchNo, String receiverType, String accountHash) {
        return lambdaQuery()
                .eq(DouyinDirectAllocReceiver::getChannelMchNo, channelMchNo)
                .eq(DouyinDirectAllocReceiver::getReceiverType, receiverType)
                .eq(DouyinDirectAllocReceiver::getAccountHash, accountHash)
                .eq(DouyinDirectAllocReceiver::getStatus, AllocReceiverStatusEnum.BOUND.getCode())
                .oneOpt();
    }
}
