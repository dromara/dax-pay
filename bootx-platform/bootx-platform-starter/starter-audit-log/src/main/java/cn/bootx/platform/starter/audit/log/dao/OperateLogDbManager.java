package cn.bootx.platform.starter.audit.log.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.starter.audit.log.entity.OperateLogDb;
import cn.bootx.platform.starter.audit.log.param.OperateLogParam;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 操作日志
 *
 * @author xxm
 * @since 2021/8/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OperateLogDbManager extends BaseManager<OperateLogDbMapper, OperateLogDb> {

    public Page<OperateLogDb> page(PageParam pageParam, OperateLogParam operateLogParam) {
        Page<OperateLogDb> mpPage = MpUtil.getMpPage(pageParam);
        return lambdaQuery()
            .like(StrUtil.isNotBlank(operateLogParam.getAccount()), OperateLogDb::getAccount,
                    operateLogParam.getAccount())
            .like(StrUtil.isNotBlank(operateLogParam.getTitle()), OperateLogDb::getTitle, operateLogParam.getTitle())
            .eq(Objects.nonNull(operateLogParam.getBusinessType()), OperateLogDb::getBusinessType,
                    operateLogParam.getBusinessType())
            .orderByDesc(OperateLogDb::getOperateTime)
            .page(mpPage);
    }

    /**
     * 删除 小于指定日期的日志
     */
    public void deleteByOffset(LocalDateTime offset){
        lambdaUpdate()
                .le(OperateLogDb::getOperateTime, offset)
                .remove();

    }

}
