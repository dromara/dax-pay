package cn.bootx.platform.starter.audit.log.core.mongo.service;

import cn.bootx.platform.starter.audit.log.param.DataVersionLogParam;
import cn.bootx.platform.starter.audit.log.service.DataVersionLogService;
import cn.bootx.platform.common.core.code.CommonCode;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.jackson.util.JacksonUtil;
import cn.bootx.platform.starter.audit.log.core.mongo.dao.DataVersionLogMongoRepository;
import cn.bootx.platform.starter.audit.log.core.mongo.entity.DataVersionLogMongo;
import cn.bootx.platform.starter.audit.log.dto.DataVersionLogDto;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author xxm
 * @since 2022/1/10
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "bootx.starter.audit-log", value = "store", havingValue = "mongo")
@RequiredArgsConstructor
public class DataVersionLogMongoService implements DataVersionLogService {

    private final DataVersionLogMongoRepository repository;

    private final MongoTemplate mongoTemplate;

    /**
     * 添加
     */
    @Override
    public void add(DataVersionLogParam param) {
        // 查询数据最新版本
        Criteria criteria = Criteria.where(DataVersionLogMongo.Fields.tableName)
            .is(param.getDataName())
            .and(DataVersionLogMongo.Fields.dataId)
            .is(param.getDataId());
        Sort sort = Sort.by(Sort.Order.desc(DataVersionLogMongo.Fields.version));
        Query query = new Query().addCriteria(criteria).with(sort).limit(1);
        DataVersionLogMongo one = mongoTemplate.findOne(query, DataVersionLogMongo.class);
        Integer maxVersion = Optional.ofNullable(one).map(DataVersionLogMongo::getVersion).orElse(0);

        DataVersionLogMongo dataVersionLog = new DataVersionLogMongo().setTableName(param.getTableName())
            .setDataName(param.getDataName())
            .setDataId(param.getDataId())
            .setCreator(SecurityUtil.getUserIdOrDefaultId())
            .setCreateTime(LocalDateTime.now())
            .setVersion(maxVersion + 1);
        if (param.getDataContent() instanceof String) {
            dataVersionLog.setDataContent((String) param.getDataContent());
        }
        else {
            dataVersionLog.setDataContent(JacksonUtil.toJson(param.getDataContent()));
        }
        if (param.getChangeContent() instanceof String) {
            dataVersionLog.setChangeContent(param.getChangeContent());
        }
        else {
            if (Objects.nonNull(param.getChangeContent())) {
                dataVersionLog.setChangeContent(JacksonUtil.toJson(param.getChangeContent()));
            }
        }
        dataVersionLog.setId(IdUtil.getSnowflakeNextId());
        repository.save(dataVersionLog);
    }

    @Override
    public DataVersionLogDto findById(Long id) {
        return repository.findById(id).map(DataVersionLogMongo::toDto).orElseThrow(DataNotExistException::new);
    }

    @Override
    public PageResult<DataVersionLogDto> page(PageParam pageParam, DataVersionLogParam param) {
        DataVersionLogMongo dataVersionLogMongo = new DataVersionLogMongo().setDataId(param.getDataId())
            .setVersion(param.getVersion())
            .setTableName(param.getTableName())
            .setDataName(param.getDataName());
        // 查询条件
        ExampleMatcher matching = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<DataVersionLogMongo> example = Example.of(dataVersionLogMongo, matching);
        // 设置分页条件 (第几页，每页大小，排序)
        Sort sort = Sort.by(Sort.Order.desc(CommonCode.ID));
        Pageable pageable = PageRequest.of(pageParam.getCurrent() - 1, pageParam.getSize(), sort);

        Page<DataVersionLogMongo> page = repository.findAll(example, pageable);
        List<DataVersionLogDto> records = page.getContent()
            .stream()
            .map(DataVersionLogMongo::toDto)
            .collect(Collectors.toList());

        return new PageResult<DataVersionLogDto>().setCurrent(pageParam.getCurrent())
            .setSize(pageParam.getSize())
            .setRecords(records)
            .setTotal(page.getTotalElements());
    }

}
