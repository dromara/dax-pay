package org.dromara.daxpay.service.dao.notice.callback;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.service.entity.notice.callback.MerchantCallbackRecord;
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
public class MerchantCallbackRecordManager extends BaseManager<MerchantCallbackRecordMapper, MerchantCallbackRecord> {

    /**
     * 分页
     */
    public Page<MerchantCallbackRecord> page(PageParam param, Long taskId){
        var mpPage = MpUtil.getMpPage(param, MerchantCallbackRecord.class);
        return lambdaQuery().eq(MerchantCallbackRecord::getTaskId,taskId)
                .page(mpPage);
    }
}
