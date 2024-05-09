package cn.bootx.platform.baseapi.core.dynamicsource.service;

import cn.bootx.platform.baseapi.dto.dynamicsource.DynamicDataSourceDto;
import cn.bootx.platform.baseapi.param.dynamicsource.DynamicDataSourceParam;
import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.dto.KeyValue;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.baseapi.core.dynamicsource.dao.DynamicDataSourceManager;
import cn.bootx.platform.baseapi.core.dynamicsource.entity.DynamicDataSource;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.db.ds.simple.SimpleDataSource;
import cn.hutool.db.handler.EntityHandler;
import cn.hutool.db.sql.SqlExecutor;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 动态数据源管理
 *
 * @author xxm
 * @since 2022/9/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DynamicDataSourceService {

    private final DynamicDataSourceManager dynamicDataSourceManager;

    private final DataSourceCreator hikariDataSourceCreator;

    private final DynamicRoutingDataSource dynamicRoutingDataSource;

    private final DynamicDataSourceProperties dynamicDataSourceProperties;

    /**
     * 添加
     */
    public void add(DynamicDataSourceParam param) {
        DynamicDataSource dynamicDataSource = DynamicDataSource.init(param);
        if (Objects.equals(dynamicDataSourceProperties.getPrimary(), param.getCode())) {
            throw new BizException("名称不可为 " + dynamicDataSourceProperties.getPrimary());
        }

        if (dynamicDataSourceManager.existsByCode(param.getCode())) {
            throw new BizException("编码重复");
        }
        dynamicDataSourceManager.save(dynamicDataSource);
    }

    /**
     * 修改
     */
    public void update(DynamicDataSourceParam param) {
        DynamicDataSource dynamicDataSource = dynamicDataSourceManager.findById(param.getId())
            .orElseThrow(DataNotExistException::new);
        if (Objects.equals(dynamicDataSourceProperties.getPrimary(), param.getCode())) {
            throw new BizException("名称不可为 " + dynamicDataSourceProperties.getPrimary());
        }
        if (dynamicDataSourceManager.existsByCode(param.getCode(), param.getId())) {
            throw new BizException("编码重复");
        }
        BeanUtil.copyProperties(param, dynamicDataSource, CopyOptions.create().ignoreNullValue());
        dynamicDataSourceManager.updateById(dynamicDataSource);
    }

    /**
     * 分页
     */
    public PageResult<DynamicDataSourceDto> page(PageParam pageParam, DynamicDataSourceParam dynamicDataSourceParam) {
        return MpUtil.convert2DtoPageResult(dynamicDataSourceManager.page(pageParam, dynamicDataSourceParam));
    }

    /**
     * 获取单条
     */
    public DynamicDataSourceDto findById(Long id) {
        return dynamicDataSourceManager.findById(id)
            .map(DynamicDataSource::toDto)
            .orElseThrow(DataNotExistException::new);
    }

    /**
     * 编码查重
     */
    public boolean existsByCode(String code) {
        if (Objects.equals(dynamicDataSourceProperties.getPrimary(), code)) {
            return true;
        }
        return dynamicDataSourceManager.existsByCode(code);
    }

    /**
     * 编码查重 (不包含id)
     */
    public boolean existsByCode(String code, Long id) {
        if (Objects.equals(dynamicDataSourceProperties.getPrimary(), code)) {
            return true;
        }
        return dynamicDataSourceManager.existsByCode(code, id);
    }

    /**
     * 获取全部
     */
    public List<DynamicDataSourceDto> findAll() {
        return ResultConvertUtil.dtoListConvert(dynamicDataSourceManager.findAll());
    }

    /**
     * 测试连接 根据参数
     */
    public String testConnection(DynamicDataSourceParam param) {
        try (SimpleDataSource ds = new SimpleDataSource(param.getDbUrl(), param.getDbUsername(), param.getDbPassword(),
                param.getDbDriver()); Connection connection = ds.getConnection();) {
            SqlExecutor.query(connection, "SELECT 1", new EntityHandler());
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return null;
    }

    /**
     * 根据数据源id测试连接
     */
    public String testConnection(Long id) {
        DynamicDataSource dataSource = dynamicDataSourceManager.findById(id).orElseThrow(DataNotExistException::new);
        DynamicDataSourceParam sourceParam = new DynamicDataSourceParam();
        BeanUtil.copyProperties(dataSource, sourceParam);
        return this.testConnection(sourceParam);
    }

    /**
     * 删除
     */
    public void delete(Long id) {
        dynamicDataSourceManager.deleteById(id);
    }

    /**
     * 添加数据源到多数据集合中
     */
    public void addDynamicDataSource(DynamicDataSource dynamicDataSource) {
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        dataSourceProperty.setUrl(dynamicDataSource.getDbUrl());
        dataSourceProperty.setPassword(dynamicDataSource.getDbPassword());
        dataSourceProperty.setDriverClassName(dynamicDataSource.getDbDriver());
        dataSourceProperty.setUsername(dynamicDataSource.getDbUsername());
        DataSource dataSource = hikariDataSourceCreator.createDataSource(dataSourceProperty);
        dynamicRoutingDataSource.addDataSource(dynamicDataSource.getCode(), dataSource);
    }

    /**
     * 根据id进行添加
     */
    public void addDynamicDataSourceById(Long id) {
        DynamicDataSource dataSource = dynamicDataSourceManager.findById(id).orElseThrow(DataNotExistException::new);
        this.addDynamicDataSource(dataSource);
    }

    /**
     * 查询当前数据源列表
     */
    public List<KeyValue> findAllDataSource() {
        Map<String, DataSource> dataSources = dynamicRoutingDataSource.getDataSources();
        return dataSources.keySet().stream().map(s -> new KeyValue().setKey(s)).collect(Collectors.toList());
    }

    /**
     * 判断该数据源当前是否已经添加
     */
    public boolean existsByDataSourceKey(String key) {
        Map<String, DataSource> dataSources = dynamicRoutingDataSource.getDataSources();
        return dataSources.containsKey(key);
    }

    /**
     * 删除数据源里的数据
     */
    public void removeDataSourceByKey(String key) {
        if (Objects.equals(dynamicDataSourceProperties.getPrimary(), key)) {
            throw new BizException(key + " 数据源不可被删除");
        }
        dynamicRoutingDataSource.removeDataSource(key);
    }

    /**
     * 启动时初始化加载
     */
    public void initLoad(){
        for (DynamicDataSource dynamicDataSource : dynamicDataSourceManager.findAllByAutoLoad()) {
            try {
                addDynamicDataSource(dynamicDataSource);
            } catch (Exception e){
                String errorMsg = dynamicDataSource.getName()+"数据源加载失败";
                log.error(errorMsg,e);
            }
        }
    }

}
