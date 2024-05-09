package cn.bootx.platform.starter.audit.log.service;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.starter.audit.log.dto.LoginLogDto;
import cn.bootx.platform.starter.audit.log.param.LoginLogParam;
import org.springframework.scheduling.annotation.Async;

/**
 * 登陆日志
 *
 * @author xxm
 * @since 2021/12/2
 */
public interface LoginLogService {

    /**
     * 添加
     */
    @Async("asyncExecutor")
    void add(LoginLogParam loginLog);

    /**
     * 获取
     */
    LoginLogDto findById(Long id);

    /**
     * 分页
     */
    PageResult<LoginLogDto> page(PageParam pageParam, LoginLogParam loginLogParam);

    /**
     * 删除
     */
    void deleteByDay(int deleteDay);

}
