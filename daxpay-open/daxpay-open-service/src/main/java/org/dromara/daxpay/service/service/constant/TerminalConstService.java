package org.dromara.daxpay.service.service.constant;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.service.dao.assist.TerminalConstManager;
import org.dromara.daxpay.service.param.constant.TerminalConstQuery;
import org.dromara.daxpay.service.result.constant.TerminalConstResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 通道终端报送类型
 * @author xxm
 * @since 2025/3/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TerminalConstService {
    private final TerminalConstManager terminalConstManager;

    /**
     * 分页
     */
    public PageResult<TerminalConstResult> page(PageParam pageParam, TerminalConstQuery query) {
        return MpUtil.toPageResult(terminalConstManager.page(pageParam, query));
    }

}
