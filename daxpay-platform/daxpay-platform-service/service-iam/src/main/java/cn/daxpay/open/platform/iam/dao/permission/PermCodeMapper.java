package cn.daxpay.open.platform.iam.dao.permission;

import cn.daxpay.open.platform.iam.entity.permission.PermCodeData;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/// # 权限码
///
@Mapper
public interface PermCodeMapper extends MPJBaseMapper<PermCodeData> {
}

