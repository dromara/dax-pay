package cn.daxpay.open.platform.capability.audit.log.dao;

import cn.daxpay.open.platform.capability.audit.log.entity.LoginLogDb;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.OffsetDateTime;

/// # 登录日志
///
@Mapper
public interface LoginLogDbMapper extends MPJBaseMapper<LoginLogDb> {

    /// 按登录时间物理删除过期日志
    ///
    /// 实体的 `deleted` 字段标了 @TableLogic, 只影响 MP 内置方法(lambdaUpdate().remove() 会退化为逻辑删除 UPDATE);
    /// 日志表需要按天物理清理回收空间, 因此物理删除走自定义 @Delete, MP 不会改写该 SQL。
    ///
    /// @param offset 时间偏移量(早于该时间的日志将被删除)
    /// @return 删除条数
    @Delete("DELETE FROM starter_audit_login_log WHERE login_time <= #{offset}")
    int deleteByLoginTimeBefore(@Param("offset") OffsetDateTime offset);
}
