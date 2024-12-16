package org.dromara.daxpay.channel.wechat.service.allocation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.service.bo.allocation.AllocStartResultBo;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocDetail;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocOrder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微信分账V2版本接口
 * @author xxm
 * @since 2024/12/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPayAllocationV2Service {

    /**
     * 分账
     */
    public AllocStartResultBo start(AllocOrder order, List<AllocDetail> details, WechatPayConfig config) {
        return null;
    }

    /**
     * 完结
     */
    public void finish(AllocOrder order, List<AllocDetail> details, WechatPayConfig config) {

    }

    /**
     * 同步
     */
    public void sync(AllocOrder order, List<AllocDetail> details, WechatPayConfig config) {

    }
}
