package cn.daxpay.open.channel.wechat.dao.direct;

import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectAllocReceiver;
import cn.daxpay.open.payment.trade.alloc.enums.AllocReceiverStatusEnum;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.channel.wechat.param.direct.WechatDirectAllocReceiverQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 微信直连分账接收方管理
///
@Repository
public class WechatDirectAllocReceiverManager extends BaseManager<WechatDirectAllocReceiverMapper, WechatDirectAllocReceiver> {

    /// 分页
    public Page<WechatDirectAllocReceiver> page(PageParam pageParam, WechatDirectAllocReceiverQuery query) {
        Page<WechatDirectAllocReceiver> mpPage = MpUtil.getMpPage(pageParam, WechatDirectAllocReceiver.class);
        QueryWrapper<WechatDirectAllocReceiver> generator = QueryGenerator.generator(query);
        return this.page(mpPage, generator);
    }

    /// 校验同一通道商户下同类型同账号接收方不重复(按明文哈希等值匹配)
    public boolean existsByChannelMchNoAndTypeAndHash(String channelMchNo, String receiverType, String accountHash) {
        return lambdaQuery()
                .eq(WechatDirectAllocReceiver::getChannelMchNo, channelMchNo)
                .eq(WechatDirectAllocReceiver::getReceiverType, receiverType)
                .eq(WechatDirectAllocReceiver::getAccountHash, accountHash)
                .exists();
    }

    /// 同商户下指定应用(wxAppId)是否被接收方记录引用(channelAppId, 应用删除前校验)
    public boolean existsByMchNoAndChannelAppId(String mchNo, String channelAppId) {
        return lambdaQuery()
                .eq(WechatDirectAllocReceiver::getMchNo, mchNo)
                .eq(WechatDirectAllocReceiver::getChannelAppId, channelAppId)
                .exists();
    }

    /// 查询已绑定状态的接收方档案(分账发起时校验 openid 与发起应用一致性)
    public Optional<WechatDirectAllocReceiver> findBoundByChannelMchNoAndTypeAndHash(
            String channelMchNo, String receiverType, String accountHash) {
        return lambdaQuery()
                .eq(WechatDirectAllocReceiver::getChannelMchNo, channelMchNo)
                .eq(WechatDirectAllocReceiver::getReceiverType, receiverType)
                .eq(WechatDirectAllocReceiver::getAccountHash, accountHash)
                .eq(WechatDirectAllocReceiver::getStatus, AllocReceiverStatusEnum.BOUND.getCode())
                .oneOpt();
    }
}
