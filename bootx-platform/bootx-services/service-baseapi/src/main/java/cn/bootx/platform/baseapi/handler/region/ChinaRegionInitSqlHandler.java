package cn.bootx.platform.baseapi.handler.region;

import com.baomidou.mybatisplus.extension.ddl.SimpleDdl;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 行政区域初始化
 *
 * @author xxm
 * @since 2023/2/7
 */
@Component
public class ChinaRegionInitSqlHandler extends SimpleDdl {

    /**
     * 速度太慢, 还是走手动SQL导入的方式
     */
    @Override
    public List<String> getSqlFiles() {
        return Arrays.asList(
        // "db/region/base_province.sql",
        // "db/region/base_city.sql",
        // "db/region/base_area.sql",
        // "db/region/base_street.sql"
        );
    }

}
