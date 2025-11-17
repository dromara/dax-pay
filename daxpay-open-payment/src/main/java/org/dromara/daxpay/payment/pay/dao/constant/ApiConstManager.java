package org.dromara.daxpay.payment.pay.dao.constant;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.pay.entity.constant.ApiConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author xxm
 * @since 2024/7/14
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ApiConstManager extends BaseManager<ApiConstMapper, ApiConst> {

    /**
     * 查询启用的列表
     */
    public List<ApiConst> findAllByEnable() {
        return lambdaQuery()
                .eq(ApiConst::isEnable, true)
                .orderByAsc(MpIdEntity::getId)
                .list();
    }

}
