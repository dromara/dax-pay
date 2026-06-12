package org.dromara.daxpay.platform.common.mybatisplus.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/// # mybatis plus id实体
///
@Getter
@Setter
@FieldNameConstants(innerTypeName = "Id")
public abstract class MpIdEntity {

    /// 主键, 默认通过降序排序
    @OrderBy
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

}
