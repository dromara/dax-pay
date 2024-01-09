package cn.bootx.platform.daxpay.service.core.record.repair.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.service.core.record.repair.entity.PayRepairRecord;
import cn.bootx.platform.daxpay.service.param.record.PayRepairRecordQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/1/6
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayRepairRecordManager extends BaseManager<PayRepairRecordMapper, PayRepairRecord> {

    /**
     * 分页
     */
    public Page<PayRepairRecord> page(PageParam pageParam, PayRepairRecordQuery param){
        Page<PayRepairRecord> mpPage = MpUtil.getMpPage(pageParam, PayRepairRecord.class);
        return lambdaQuery()
                .orderByDesc(MpIdEntity::getId)
                .page(mpPage);
    }
}
