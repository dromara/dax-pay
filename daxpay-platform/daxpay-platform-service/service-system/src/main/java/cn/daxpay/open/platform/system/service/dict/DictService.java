package cn.daxpay.open.platform.system.service.dict;

import cn.daxpay.open.platform.system.convert.dict.DictConvert;
import cn.daxpay.open.platform.system.dao.dict.DictItemManager;
import cn.daxpay.open.platform.system.dao.dict.DictManager;
import cn.daxpay.open.platform.system.entity.dict.Dict;
import cn.daxpay.open.platform.system.param.dict.DictParam;
import cn.daxpay.open.platform.system.result.dict.DictResult;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import cn.daxpay.open.platform.core.code.CommonCode;

/// # 数据字典服务类
///
@Service
@AllArgsConstructor
public class DictService {

    private final DictManager dictManager;

    private final DictItemManager dictItemManager;

    /// 添加字典
    @Transactional(rollbackFor = Exception.class)
    public DictResult add(DictParam param) {
        if (dictManager.existsByCode(param.getCode())) {
            // 系统: 字典编码已存在
            throw new BizException(CommonCode.FAIL_CODE, "error.system.dict.codeExists");
        }
        Dict dict = DictConvert.CONVERT.convert(param);
        dictManager.save(dict);
        return dict.toResult();
    }

    /// 删除字典
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {

        if (!dictManager.existedById(id)) {
            // 系统: 字典不存在
            throw new BizException(CommonCode.FAIL_CODE, "error.system.dict.notExist");
        }
        if (dictItemManager.existsByDictId(id)) {
            // 系统: 字典下有字典项,不能删除
            throw new BizException(CommonCode.FAIL_CODE, "error.system.dict.hasItems");
        }
        dictManager.deleteById(id);
    }

    /// 更新
    @Transactional(rollbackFor = Exception.class)
    public DictResult update(DictParam param) {

        Dict dict = dictManager.findById(param.getId())
                // 系统: 字典不存在
                .orElseThrow(()->new DataNotExistException("error.system.dict.notExist"));

        // 判断字典是否重名
        if (dictManager.existsByCode(param.getCode(), param.getId())) {
            // 系统: 字典编码已存在
            throw new BizException(CommonCode.FAIL_CODE, "error.system.dict.codeExists");
        }
        // 更新字典项
        DictConvert.CONVERT.copy(param, dict);
        dictItemManager.updateDictCode(dict.getId(), dict.getCode());
        dictManager.updateById(dict);
        return dict.toResult();
    }

    /// 判断编码是否存在
    public boolean existsByCode(String code) {
        return dictManager.existsByCode(code);
    }

    /// 判断编码是否存在
    public boolean existsByCode(String code, Long id) {
        return dictManager.existsByCode(code, id);
    }

    /// 查询指定字典
    public DictResult findById(Long id) {
        return dictManager.findById(id).map(Dict:: toResult).orElseThrow(DataNotExistException::new);
    }

    /// 查询所有字典
    public List<DictResult> findAll() {
        List<Dict> dicts = dictManager.findAll();
        return dicts.stream().map(Dict::toResult).collect(Collectors.toList());
    }

    /// 查询所有字典
    public PageResult<DictResult> page(PageParam pageParam, DictParam param) {
        return MpUtil.toPageResult(dictManager.page(pageParam, param));
    }

}
