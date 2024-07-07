package cn.bootx.platform.starter.audit.log.core.db.service;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.starter.audit.log.core.db.convert.LogConvert;
import cn.bootx.platform.starter.audit.log.core.db.dao.LoginLogDbManager;
import cn.bootx.platform.starter.audit.log.core.db.entity.LoginLogDb;
import cn.bootx.platform.starter.audit.log.dto.LoginLogDto;
import cn.bootx.platform.starter.audit.log.param.LoginLogParam;
import cn.bootx.platform.starter.audit.log.service.LoginLogService;
import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 登陆日志
 *
 * @author xxm
 * @since 2021/8/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginLogDbService implements LoginLogService {

    private final LoginLogDbManager loginLogManager;

    /**
     * 添加
     */
    @Override
    public void add(LoginLogParam loginLog) {
        loginLogManager.save(LogConvert.CONVERT.convert(loginLog));
    }

    /**
     * 获取
     */
    @Override
    public LoginLogDto findById(Long id) {
        return loginLogManager.findById(id).map(LoginLogDb::toResult).orElseThrow(DataNotExistException::new);
    }

    /**
     * 分页
     */
    @Override
    public PageResult<LoginLogDto> page(PageParam pageParam, LoginLogParam loginLogParam) {
        return MpUtil.toPageResult(loginLogManager.page(pageParam, loginLogParam));
    }

    /**
     * 删除
     */
    @Override
    public void deleteByDay(int deleteDay) {
        LocalDateTime offset = LocalDateTimeUtil.offset(LocalDateTime.now(), -deleteDay, ChronoUnit.DAYS);
        loginLogManager.deleteByOffset(offset);
    }

}
