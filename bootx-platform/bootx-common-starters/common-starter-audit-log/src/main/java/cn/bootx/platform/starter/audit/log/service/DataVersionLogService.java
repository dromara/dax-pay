package cn.bootx.platform.starter.audit.log.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.starter.audit.log.dto.DataVersionLogDto;
import cn.bootx.platform.starter.audit.log.param.DataVersionLogParam;
import org.springframework.scheduling.annotation.Async;

/**
 * 数据版本日志
 *
 * @author xxm
 * @since 2022/1/10
 */
public interface DataVersionLogService {

    /**
     * 添加
     */
    @Async("asyncExecutor")
    void add(DataVersionLogParam param);

    /**
     * 获取
     */
    DataVersionLogDto findById(Long id);

    /**
     * 分页
     */
    PageResult<DataVersionLogDto> page(PageParam pageParam, DataVersionLogParam param);

    /**
     * 删除
     */
    default void delete(Long id) {
        throw new BizException("日志不允许删除");
    }

}
