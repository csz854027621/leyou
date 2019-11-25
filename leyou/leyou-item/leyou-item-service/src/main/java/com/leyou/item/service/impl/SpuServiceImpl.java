package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.Spu;
import com.leyou.item.service.BrandService;
import com.leyou.item.service.CategoryService;
import com.leyou.item.service.SpuService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper sm;

    @Autowired
    private BrandMapper bm;

    @Autowired
    private CategoryMapper cm;

    @Autowired
    private CategoryService cs;

    @Override
    public PageResult<SpuBo> findAllByCondition(String key, Boolean saleable, Integer page, Integer rows) {

        //1.添加条件查询；
        Example  example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }
        if (saleable!=null){
            criteria.andEqualTo("saleable",saleable);
        }
        //2.分页；
        PageHelper.startPage(page,rows);
        List<Spu> spus = sm.selectByExample(example);
        PageInfo<Spu> pageInfo=new PageInfo<>(spus);

        //3.根据条件再查询，并信息封装；
        List<SpuBo> spuBos= spus.stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu,spuBo);
            Brand brand = bm.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBname(brand.getName());
            List<String> nameByCids = cs.findNameByCids(Arrays.asList(spuBo.getCid1(),
                    spuBo.getCid2(), spuBo.getCid3()));
            String cname = StringUtils.join(nameByCids, "-");
            spuBo.setCname(cname);
            return spuBo;

        }).collect(Collectors.toList());


      /*
      获取spuBos
      方法二：
      spus.forEach(spu -> {
            SpuBo sb=new SpuBo();
            BeanUtils.copyProperties(spu,sb);
            //获取商品类别名
            List<Category> names = cm.findNameByCids(spu.getCid1(), spu.getCid2(), spu.getCid3());


            sb.setCname(names.get(0).getName()+"-"+
                    names.get(1).getName()+"-"+
                    names.get(2).getName());


            Brand brand = bm.selectByPrimaryKey(spu.getBrandId());
            sb.setBname(brand.getName());
            spuBos.add(sb);
        });
        */

        PageResult<SpuBo> result=new PageResult<>();
        result.setItems(spuBos);
        result.setTotal(pageInfo.getTotal());


        return result;
    }
}
