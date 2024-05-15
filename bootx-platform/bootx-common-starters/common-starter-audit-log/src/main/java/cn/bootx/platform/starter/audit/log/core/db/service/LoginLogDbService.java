package cn.bootx.platform.starter.audit.log.core.db.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.starter.audit.log.param.LoginLogParam;
import cn.bootx.platform.starter.audit.log.service.LoginLogService;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.starter.audit.log.core.db.convert.LogConvert;
import cn.bootx.platform.starter.audit.log.core.db.dao.LoginLogDbManager;
import cn.bootx.platform.starter.audit.log.core.db.entity.LoginLogDb;
import cn.bootx.platform.starter.audit.log.dto.LoginLogDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(prefix = "bootx.starter.audit-log", value = "store", havingValue = "jdbc", matchIfMissing = true)
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
        return loginLogManager.findById(id).map(LoginLogDb::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 分页
     */
    @Override
    public PageResult<LoginLogDto> page(PageParam pageParam, LoginLogParam loginLogParam) {
        return MpUtil.convert2DtoPageResult(loginLogManager.page(pageParam, loginLogParam));
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
