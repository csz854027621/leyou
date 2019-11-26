package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import com.leyou.item.service.CategoryService;
import com.leyou.item.service.GoodsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsService {


    @Autowired
    private  SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper sp;
    @Autowired
    private SpuDetailMapper sd;
    @Autowired
    private SkuMapper sk;

    @Autowired
    private SpuMapper sm;

    @Autowired
    private BrandMapper bm;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private CategoryMapper cm;

    @Autowired
    private CategoryService cs;

    @Override
    public List<SpecGroup> findAllSpecGroupByCondition(Long cid) {
        SpecGroup specGroup=new SpecGroup();
        specGroup.setCid(cid);
        return specGroupMapper.select(specGroup);
    }


    @Override
    public PageResult<SpuBo> findAllSpuBoByCondition(String key, Boolean saleable, Integer page, Integer rows) {

        //1.添加条件查询；
        Example example=new Example(Spu.class);
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

    @Override
    public List<SpecParam> findAllSpecParamByCondition(Long cid) {
        SpecParam spb=new SpecParam();
        spb.setCid(cid);
        List<SpecParam> select = sp.select(spb);
        return select;
    }

    @Transactional
    @Override
    public void save(SpuBo sb) {
        //保存spu
        sb.setId(null);
        sb.setValid(true);
        sb.setSaleable(true);
        sb.setCreateTime(new Date());
        sb.setLastUpdateTime(sb.getCreateTime());
        sm.insertSelective(sb);
        //保存spuDetail
        sb.getSpuDetail().setSpuId(sb.getId());
        sd.insertSelective(sb.getSpuDetail());
        //保存sku
        sb.getSkus().forEach(sku -> {
            sku.setSpu_id(sb.getId());
            sku.setCreateTime(sb.getCreateTime());
            sku.setLastUpdateTime(sb.getLastUpdateTime());
            sku.setEnable(true);
            sk.insertSelective(sku);
            //保存库存
            Stock stock=new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insertSelective(stock);
        });
        System.out.println("dd");
    }


}
