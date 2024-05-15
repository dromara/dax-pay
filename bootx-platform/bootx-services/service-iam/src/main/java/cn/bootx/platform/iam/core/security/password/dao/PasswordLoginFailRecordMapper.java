package cn.bootx.platform.iam.core.security.password.dao;

import cn.bootx.platform.iam.core.security.password.entity.PasswordLoginFailRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 密码登录失败记录
 * @author xxm
 * @since 2023/9/19
 */
@Mapper
public interface PasswordLoginFailRecordMapper extends BaseMapper<PasswordLoginFailRecord> {
}
