package cn.daxpay.multi.channel.wechat.service.sync.pay;

import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.service.bo.sync.PaySyncResultBo;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.daxpay.multi.service.enums.PaySyncResultEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 微信支付同步服务
 *
 * @author xxm
 * @since 2021/6/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPaySyncV2Service {

    /**
     * 支付信息查询
     */
    public PaySyncResultBo syncPayStatus(PayOrder order, WechatPayConfig weChatPayConfig) {
        PaySyncResultBo syncResult = new PaySyncResultBo().setSyncStatus(PaySyncResultEnum.FAIL);
        return syncResult;
    }

}
