package com.leyou.item.service;

import com.leyou.item.pojo.SpecGroup;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpecGroupService {
    List<SpecGroup> findAllByCondition(Long gid);
}
