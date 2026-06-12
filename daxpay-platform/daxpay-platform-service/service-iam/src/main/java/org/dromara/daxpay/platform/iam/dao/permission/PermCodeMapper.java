package org.dromara.daxpay.platform.iam.dao.permission;

import org.dromara.daxpay.platform.iam.entity.permission.PermCodeData;
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

