package cn.daxpay.open.platform.system.entity.region;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/// # 城市接壤关系
///
/// 纯关系载体, 供地理围栏 balanced 策略邻市容错查询。
/// 该表为机器生成的纯导入型字典表(约2000行静态数据, 双向存储), id 用 [IdType#AUTO] 走 DB 自增(bigserial),
/// 不走应用层雪花; 无审计列。
@Data
@TableName("base_city_adjacent")
public class CityAdjacent {

    /// 主键ID(DB 自增, 纯关系表数据导入专用)
    @TableId(type = IdType.AUTO)
    private Long id;

    /// 城市编码
    private String cityCode;

    /// 相邻城市编码
    private String adjacentCityCode;
}
