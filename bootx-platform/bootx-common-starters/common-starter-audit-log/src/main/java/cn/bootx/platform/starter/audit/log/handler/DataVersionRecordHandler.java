package cn.bootx.platform.starter.audit.log.handler;

import cn.bootx.platform.starter.audit.log.service.DataVersionLogService;
import cn.bootx.platform.common.core.annotation.DataVersionLog;
import cn.bootx.platform.common.mybatisplus.extension.DataChangeRecorderInnerInterceptor;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.starter.audit.log.param.DataVersionLogParam;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 数据变动记录插件
 *
 * @author xxm
 * @since 2023/1/8
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataVersionRecordHandler extends DataChangeRecorderInnerInterceptor {

    // 必须延迟加载, 不然无法启动
    @Lazy
    private final DataVersionLogService dataVersionLogService;

    /**
     * 处理
     */
    @Override
    protected void dealOperationResult(OperationResult operationResult) {
        DataVersionLog annotation = operationResult.getAnnotation();
        List<DataChangedRecord> changedRecords = operationResult.getChangedRecords();
        // 遍历条信息
        for (DataChangedRecord changedRecord : changedRecords) {
            // 原始数据
            List<DataColumnChangeResult> originalColumns = Optional.ofNullable(changedRecord.getOriginalColumns())
                .orElse(new ArrayList<>(0));
            Map<String, Object> dataRecord = new HashMap<>();
            Map<String, Object> updateRecord = new HashMap<>();
            // 遍历原始数据的所有字段
            for (DataColumnChangeResult originalColumn : originalColumns) {
                dataRecord.put(originalColumn.getColumnName(), originalColumn.getOriginalValue());
            }
            // 用新数据进行替换
            List<DataColumnChangeResult> updatedColumns = Optional.ofNullable(changedRecord.getUpdatedColumns())
                .orElse(new ArrayList<>(0));
            for (DataColumnChangeResult updatedColumn : updatedColumns) {
                // 更新记录列表记录变更前的数据
                updateRecord.put(updatedColumn.getColumnName(), dataRecord.get(updatedColumn.getColumnName()));
                dataRecord.put(updatedColumn.getColumnName(), updatedColumn.getUpdateValue());
            }
            Object pkColumnVal = changedRecord.getPkColumnVal();
            // insert手动获取下主键值
            if ("insert".equals(operationResult.getOperation())) {
                String keyProperty = Optional.ofNullable(MpUtil.getTableInfo(operationResult.getTableName()))
                    .map(TableInfo::getKeyProperty)
                    .map(String::toUpperCase)
                    .orElse(null);
                pkColumnVal = dataRecord.get(keyProperty);
            }
            // 保存记录
            DataVersionLogParam dataVersionLogParam = new DataVersionLogParam().setDataId(pkColumnVal.toString())
                .setDataName(annotation.title())
                .setDataContent(dataRecord)
                .setChangeContent(updateRecord)
                .setTableName(operationResult.getTableName());
            dataVersionLogService.add(dataVersionLogParam);
        }
    }

}
