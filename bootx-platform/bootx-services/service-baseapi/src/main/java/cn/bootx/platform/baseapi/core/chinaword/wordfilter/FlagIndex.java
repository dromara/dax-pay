package cn.bootx.platform.baseapi.core.chinaword.wordfilter;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 敏感词标记
 *
 * @author minghu.zhang
 */
@Getter
@Setter
public class FlagIndex {

    /**
     * 标记结果
     */
    private boolean flag;
    /**
     * 是否黑名单词汇
     */
    private boolean isWhiteWord;
    /**
     * 标记索引
     */
    private List<Integer> index;

}
