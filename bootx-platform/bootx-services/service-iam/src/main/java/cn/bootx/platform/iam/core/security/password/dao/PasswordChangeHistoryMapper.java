package cn.bootx.platform.iam.core.security.password.dao;

import cn.bootx.platform.iam.core.security.password.entity.PasswordChangeHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 密码历史
 * @author xxm
 * @since 2023-09-19
 */
@Mapper
public interface PasswordChangeHistoryMapper extends BaseMapper<PasswordChangeHistory> {
}
