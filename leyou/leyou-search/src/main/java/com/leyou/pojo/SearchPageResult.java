package com.leyou.pojo;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;

import java.util.List;
import java.util.Map;

public class SearchPageResult extends PageResult<Goods> {

    private Integer page;  //当前页
    private List<Brand> brandList;
    private List<Map<String,Object>> categoryList;  //String 是 id , Objcet 是类型名
    private List<Map<String, Object>> specsList;


    public List<Map<String, Object>> getSpecsList() {
        return specsList;
}

    public void setSpecsList(List<Map<String, Object>> specsList) {
        this.specsList = specsList;
    }

    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }

    public List<Map<String, Object>> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Map<String, Object>> categoryList) {
        this.categoryList = categoryList;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
