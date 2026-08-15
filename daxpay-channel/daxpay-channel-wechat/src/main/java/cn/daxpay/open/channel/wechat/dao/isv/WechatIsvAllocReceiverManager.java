package cn.daxpay.open.channel.wechat.dao.isv;

import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvAllocReceiver;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvAllocReceiverQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

/// # 微信服务商分账接收方管理
///
@Repository
public class WechatIsvAllocReceiverManager extends BaseManager<WechatIsvAllocReceiverMapper, WechatIsvAllocReceiver> {

    /// 分页
    public Page<WechatIsvAllocReceiver> page(PageParam pageParam, WechatIsvAllocReceiverQuery query) {
        Page<WechatIsvAllocReceiver> mpPage = MpUtil.getMpPage(pageParam, WechatIsvAllocReceiver.class);
        QueryWrapper<WechatIsvAllocReceiver> generator = QueryGenerator.generator(query);
        return this.page(mpPage, generator);
    }

    /// 校验同一通道商户下同类型同账号接收方不重复(按明文哈希等值匹配)
    public boolean existsByChannelMchNoAndTypeAndHash(String channelMchNo, String receiverType, String accountHash) {
        return lambdaQuery()
                .eq(WechatIsvAllocReceiver::getChannelMchNo, channelMchNo)
                .eq(WechatIsvAllocReceiver::getReceiverType, receiverType)
                .eq(WechatIsvAllocReceiver::getAccountHash, accountHash)
                .exists();
    }
}
