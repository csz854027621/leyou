package com.leyou.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.pojo.Goods;
import com.leyou.pojo.SearchPageResult;
import com.leyou.pojo.SearchRequest;
import org.springframework.stereotype.Service;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.List;

@Service
public interface GoodsService {
    /**
     * 通过spuId构建Goods
     * @param spuBo
     * @return
     */
    public Goods buildGoodsBySpuBo(Long spuId) throws IOException;

    /**
     * 通过关键词匹配查询
     * @param key
     * @return List<Goods>
     */
    public SearchPageResult findGoodsByKey(SearchRequest searchRequest);
}
