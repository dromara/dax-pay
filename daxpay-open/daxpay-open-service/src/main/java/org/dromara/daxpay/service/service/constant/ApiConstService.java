package org.dromara.daxpay.service.service.constant;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.service.dao.constant.ApiConstManager;
import org.dromara.daxpay.service.param.constant.ApiConstQuery;
import org.dromara.daxpay.service.result.constant.ApiConstResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付接口信息
 * @author xxm
 * @since 2024/7/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiConstService {
    private final ApiConstManager apiConstManager;

    /**
     * 分页
     */
    public PageResult<ApiConstResult> page(PageParam pageParam, ApiConstQuery query) {
        return MpUtil.toPageResult(apiConstManager.page(pageParam, query));
    }
}
