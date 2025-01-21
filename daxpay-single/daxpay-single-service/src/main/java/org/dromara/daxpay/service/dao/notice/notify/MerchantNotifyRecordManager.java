package org.dromara.daxpay.service.dao.notice.notify;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.service.entity.notice.notify.MerchantNotifyRecord;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/7/30
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MerchantNotifyRecordManager extends BaseManager<MerchantNotifyRecordMapper, MerchantNotifyRecord> {

    /**
     * 分页
     */
    public Page<MerchantNotifyRecord> page(PageParam param, Long taskId){
        var mpPage = MpUtil.getMpPage(param, MerchantNotifyRecord.class);
       return lambdaQuery().eq(MerchantNotifyRecord::getTaskId,taskId)
               .page(mpPage);
    }
}
