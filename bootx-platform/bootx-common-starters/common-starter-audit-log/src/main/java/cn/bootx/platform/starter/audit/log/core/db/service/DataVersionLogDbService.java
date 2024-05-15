package cn.bootx.platform.starter.audit.log.core.db.service;

import cn.bootx.platform.starter.audit.log.param.DataVersionLogParam;
import cn.bootx.platform.starter.audit.log.service.DataVersionLogService;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.jackson.util.JacksonUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.starter.audit.log.core.db.dao.DataVersionLogDbManager;
import cn.bootx.platform.starter.audit.log.core.db.entity.DataVersionLogDb;
import cn.bootx.platform.starter.audit.log.dto.DataVersionLogDto;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 数据版本日志数据库实现
 *
 * @author xxm
 * @since 2022/1/10
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "bootx.starter.audit-log", value = "store", havingValue = "jdbc", matchIfMissing = true)
@RequiredArgsConstructor
public class DataVersionLogDbService implements DataVersionLogService {

    private final DataVersionLogDbManager manager;

    /**
     * 添加
     */
    @Override
    @Transactional
    public void add(DataVersionLogParam param) {
        int maxVersion = manager.getMaxVersion(param.getTableName(), param.getDataId());
        DataVersionLogDb dataVersionLog = new DataVersionLogDb().setTableName(param.getTableName())
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
        manager.save(dataVersionLog);
    }

    /**
     * 获取
     */
    @Override
    public DataVersionLogDto findById(Long id) {
        return manager.findById(id).map(DataVersionLogDb::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 分页
     */
    @Override
    public PageResult<DataVersionLogDto> page(PageParam pageParam, DataVersionLogParam param) {
        return MpUtil.convert2DtoPageResult(manager.page(pageParam, param));
    }

}
