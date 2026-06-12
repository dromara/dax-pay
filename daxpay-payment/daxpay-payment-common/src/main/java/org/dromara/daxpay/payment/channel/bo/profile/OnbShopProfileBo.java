package org.dromara.daxpay.payment.channel.bo.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 进件门店信息结果
///
@Data
@Accessors(chain = true)
@Schema(title = "进件门店信息结果")
public class OnbShopProfileBo {

    @Schema(description = "申请ID")
    private Long applyId;

   
    @Schema(description = "门店类型")
    private String type;

    @Schema(description = "结算类型")
    private String settleType;

    @Schema(description = "经营场所名称")
    private String name;

    @Schema(description = "门店地址-省市区编码")
    private List<String> regionCode;

    @Schema(description = "门店详细地址")
    private String address;

    @Schema(description = "门店照片(媒体ID)")
    private String doorPic;

    @Schema(description = "门店照片路径(系统存储)")
    private String doorPicUrl;

    @Schema(description = "门店内景照片(媒体ID)")
    private String insidePic;

    @Schema(description = "门店内景照片路径(系统存储)")
    private String insidePicUrl;

    @Schema(description = "收银台照片(媒体ID)")
    private String cashierPic;

    @Schema(description = "收银台照片路径(系统存储)")
    private String cashierPicUrl;
}
