package cn.bootx.platform.iam.entity.upms;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 角色路径权限表
 *
 * @author xxm
 * @since 2020/5/11 22:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("iam_role_path")
public class RolePath extends MpIdEntity {

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 终端编码
     */
    private String clientCode;

    /**
     * 请求权限id
     */
    private Long pathId;


}
