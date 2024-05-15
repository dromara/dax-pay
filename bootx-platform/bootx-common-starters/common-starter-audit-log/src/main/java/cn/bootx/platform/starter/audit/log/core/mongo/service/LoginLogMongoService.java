package cn.bootx.platform.starter.audit.log.core.mongo.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.starter.audit.log.param.LoginLogParam;
import cn.bootx.platform.starter.audit.log.service.LoginLogService;
import cn.bootx.platform.common.core.code.CommonCode;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.starter.audit.log.core.mongo.convert.LogConvert;
import cn.bootx.platform.starter.audit.log.core.mongo.dao.LoginLogMongoRepository;
import cn.bootx.platform.starter.audit.log.core.mongo.entity.LoginLogMongo;
import cn.bootx.platform.starter.audit.log.dto.LoginLogDto;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MongoDB存储实现
 *
 * @author xxm
 * @since 2021/12/2
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "bootx.starter.audit-log", value = "store", havingValue = "mongo")
@RequiredArgsConstructor
public class LoginLogMongoService implements LoginLogService {

    private final LoginLogMongoRepository repository;

    @Override
    public void add(LoginLogParam loginLog) {
        LoginLogMongo loginLogMongo = LogConvert.CONVERT.convert(loginLog);
        loginLogMongo.setId(IdUtil.getSnowflakeNextId());
        repository.save(loginLogMongo);
    }

    @Override
    public LoginLogDto findById(Long id) {
        return repository.findById(id).map(LoginLogMongo::toDto).orElseThrow(DataNotExistException::new);
    }

    @Override
    public PageResult<LoginLogDto> page(PageParam pageParam, LoginLogParam loginLogParam) {
        // 查询条件
        ExampleMatcher matching = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<LoginLogMongo> example = Example.of(LogConvert.CONVERT.convert(loginLogParam), matching);
        // 设置分页条件 (第几页，每页大小，排序)
        Sort sort = Sort.by(Sort.Order.desc(CommonCode.ID));
        Pageable pageable = PageRequest.of(pageParam.getCurrent() - 1, pageParam.getSize(), sort);

        Page<LoginLogMongo> page = repository.findAll(example, pageable);
        List<LoginLogDto> records = page.getContent().stream().map(LoginLogMongo::toDto).collect(Collectors.toList());

        return new PageResult<LoginLogDto>().setCurrent(pageParam.getCurrent())
            .setSize(pageParam.getSize())
            .setRecords(records)
            .setTotal(page.getTotalElements());
    }

    @Override
    public void deleteByDay(int deleteDay) {
        LocalDateTime offset = LocalDateTimeUtil.offset(LocalDateTime.now(), -deleteDay, ChronoUnit.DAYS);
        repository.deleteByLoginTimeBefore(offset);
    }

}
