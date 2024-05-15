package cn.bootx.platform.iam.core.user.dao;

import cn.bootx.platform.iam.core.user.entity.UserDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户部门关系
 *
 * @author xxm
 * @since 2021/9/29
 */
@Mapper
public interface UserDeptMapper extends BaseMapper<UserDept> {

    void saveAll(@Param("userDepots") List<UserDept> userDepots);

}
