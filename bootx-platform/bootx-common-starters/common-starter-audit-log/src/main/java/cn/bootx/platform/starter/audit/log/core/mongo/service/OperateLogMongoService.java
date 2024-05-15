package cn.bootx.platform.starter.audit.log.core.mongo.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.starter.audit.log.param.OperateLogParam;
import cn.bootx.platform.starter.audit.log.service.OperateLogService;
import cn.bootx.platform.common.core.code.CommonCode;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.starter.audit.log.core.mongo.convert.LogConvert;
import cn.bootx.platform.starter.audit.log.core.mongo.dao.OperateLogMongoRepository;
import cn.bootx.platform.starter.audit.log.core.mongo.entity.OperateLogMongo;
import cn.bootx.platform.starter.audit.log.dto.OperateLogDto;
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
public class OperateLogMongoService implements OperateLogService {

    private final OperateLogMongoRepository repository;

    @Override
    public void add(OperateLogParam operateLog) {
        OperateLogMongo operateLogMongo = LogConvert.CONVERT.convert(operateLog);
        operateLogMongo.setId(IdUtil.getSnowflakeNextId());
        repository.save(operateLogMongo);
    }

    @Override
    public OperateLogDto findById(Long id) {
        return repository.findById(id).map(OperateLogMongo::toDto).orElseThrow(DataNotExistException::new);
    }

    @Override
    public PageResult<OperateLogDto> page(PageParam pageParam, OperateLogParam operateLogParam) {

        // 查询条件
        ExampleMatcher matching = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<OperateLogMongo> example = Example.of(LogConvert.CONVERT.convert(operateLogParam), matching);

        // 设置分页条件 (第几页，每页大小，排序)
        Sort sort = Sort.by(Sort.Order.desc(CommonCode.ID));
        Pageable pageable = PageRequest.of(pageParam.getCurrent() - 1, pageParam.getSize(), sort);

        Page<OperateLogMongo> page = repository.findAll(example, pageable);
        List<OperateLogDto> records = page.getContent()
            .stream()
            .map(OperateLogMongo::toDto)
            .collect(Collectors.toList());

        return new PageResult<OperateLogDto>().setCurrent(pageParam.getCurrent())
            .setSize(pageParam.getSize())
            .setRecords(records)
            .setTotal(page.getTotalElements());
    }

    @Override
    public void deleteByDay(int deleteDay) {
        LocalDateTime offset = LocalDateTimeUtil.offset(LocalDateTime.now(), -deleteDay, ChronoUnit.DAYS);
        repository.deleteByOperateTimeBefore(offset);
    }

}
