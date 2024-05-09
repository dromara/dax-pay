package cn.bootx.platform.iam.dto.dept;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author xxm
 * @since 2020/5/10 15:25
 */
@Data
@Accessors(chain = true)
@Schema(title = "部门树")
@NoArgsConstructor
public class DeptTreeResult implements Serializable {

    private static final long serialVersionUID = 9065167687644450513L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "父机构ID")
    private Long parentId;

    @Schema(description = "机构/部门名称")
    private String deptName;

    @Schema(description = "排序")
    private Double sortNo;

    @Schema(description = "机构类别 1组织机构，2岗位")
    private Integer orgCategory;

    @Schema(description = "机构编码")
    private String orgCode;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "传真")
    private String fax;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "部门树")
    private List<DeptTreeResult> children;

}
