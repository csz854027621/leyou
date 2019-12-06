package com.leyou.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.client.BrandClient;
import com.leyou.client.CategoryClient;
import com.leyou.client.GoodsClient;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.pojo.Goods;
import com.leyou.pojo.SearchPageResult;
import com.leyou.pojo.SearchRequest;
import com.leyou.repository.GoodsRepository;
import com.leyou.service.GoodsService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filters.FiltersAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.*;

@Service
public class GoodsServiceImpl implements GoodsService {


    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private GoodsRepository goodsRepository;

    @Override
    public Goods buildGoodsBySpuBo(SpuBo spuBo) throws IOException {

        Goods goods = new Goods();
        goods.setId(spuBo.getId());
        goods.setCid1(spuBo.getCid1());
        goods.setCid2(spuBo.getCid2());
        goods.setCid3(spuBo.getCid3());
        goods.setBrandId(spuBo.getBrandId());
        goods.setCreateTime(spuBo.getCreateTime());
        goods.setSubTitle(spuBo.getSubTitle());

        List<String> categoryNames =
                categoryClient.findCategoryNameByCids(Arrays.asList(spuBo.getCid1(), spuBo.getCid2(), spuBo.getCid3()));
        Brand brand = brandClient.findBrandByBid(spuBo.getBrandId());
        goods.setAll(spuBo.getTitle() + " " + StringUtils.join(categoryNames, " ") + " " + brand.getName()); //标题，类型名，品牌名

        List<Sku> skus = goodsClient.findAllSkusBySpuId(spuBo.getId());
        List<Long> price = new ArrayList<>();  //价格 spu 的 list
        List<Map<String, Object>> skuViewNames = new ArrayList<>();   //skus
        skus.forEach(sku -> {
            price.add(sku.getPrice());
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("image", sku.getImages() == null ? "" : sku.getImages().split(",")[0]);
            map.put("price", sku.getPrice());
            skuViewNames.add(map);
        });
        try {
            goods.setSkus(MAPPER.writeValueAsString(skuViewNames));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        goods.setPrice(price);


        List<SpecParam> specParams =
                goodsClient.findAllSpecParamByCondition(spuBo.getCid3(), true);
        SpuDetail spuDetail = goodsClient.findAllSpuDetailBySpuId(spuBo.getId());
        Map<String, Object> genericSpec = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<String, Object>>() {
        });
        Map<String, List<Object>> specialSpec = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<String, List<Object>>>() {
        });
        Map<String, Object> specMap = new HashMap<>();
        specParams.forEach(specParam -> {
            String key = specParam.getName();//规格参数key
            //规格参数值
            if (specParam.getGeneric()) {
                Object value = genericSpec.get(specParam.getId().toString());
                if (specParam.getNumeric()) {
                    value = chooseSegement(genericSpec.get(specParam.getId().toString()).toString(), specParam);
                }
                specMap.put(key, value);
            } else {
                List<Object> value = specialSpec.get(specParam.getId().toString());
                specMap.put(key, value);
            }
        });
        goods.setSpecs(specMap);

        return goods;
    }

    @Override
    public SearchPageResult findGoodsByKey(SearchRequest searchRequest) {
        NativeSearchQueryBuilder queryBuilder=new NativeSearchQueryBuilder();
        queryBuilder.withPageable(PageRequest.of(searchRequest.getPage()-1,searchRequest.getSize()));
        QueryBuilder basicQuery = QueryBuilders.matchQuery("all", searchRequest.getKey()).operator(Operator.AND);
        queryBuilder.withQuery(basicQuery); //and匹配

        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","subTitle","skus"},null));
        queryBuilder.addAggregation(AggregationBuilders.terms("brands").field("brandId"));
        queryBuilder.addAggregation(AggregationBuilders.terms("categorys").field("cid3"));
        AggregatedPage<Goods> search = (AggregatedPage<Goods>)goodsRepository.search(queryBuilder.build());

        List<Map<String, Object>> categoryListMap = getCategoryListMap((LongTerms) search.getAggregation("categorys"));
        List<Brand> brandList=getBrandList((LongTerms)search.getAggregation("brands"));
        List<Map<String, Object>> specMap=null;
        if(!CollectionUtils.isEmpty(categoryListMap)&&categoryListMap.size()==1){
            specMap = getSpecMap(NumberUtils.toLong((String) categoryListMap.get(0).get("id")),basicQuery);

        }
        SearchPageResult result=new SearchPageResult();
        result.setTotal(search.getTotalElements());
        result.setTotalPage(search.getTotalPages());
        result.setItems(search.getContent());
        result.setPage(searchRequest.getPage());
        result.setCategoryList(categoryListMap);
        result.setBrandList(brandList);
        result.setSpecsList(specMap);
        return result;
    }

    private List<Map<String, Object>> getSpecMap(Long id, QueryBuilder basicQuery) {

        List<Map<String, Object>>  specMapList=new ArrayList<>();
        NativeSearchQueryBuilder queryBuilder=new NativeSearchQueryBuilder();
        queryBuilder.withQuery(basicQuery);
        List<SpecParam> specParams = goodsClient.findAllSpecParamByCondition(id, true);
        specParams.forEach(specParam -> {
            queryBuilder.addAggregation(
                    AggregationBuilders.terms(specParam.getName()
                    ).field("specs."+specParam.getName()+".keyword"));
        });
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{}, null));

        AggregatedPage<Goods> search = (AggregatedPage<Goods>)goodsRepository.search(queryBuilder.build());
        Map<String, Aggregation> aggregationMap = search.getAggregations().asMap();
        Set<Map.Entry<String, Aggregation>> entries = aggregationMap.entrySet();
        for (Map.Entry<String, Aggregation> entry:entries){
            Map<String,Object> map=new HashMap<>();
            map.put("k", entry.getKey());
            StringTerms stringTerms = (StringTerms)entry.getValue();
            List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
            List<String>  list=new ArrayList<>();
            buckets.forEach(bucket -> {
                String keyAsString = bucket.getKeyAsString();
                list.add(keyAsString);
            });
            map.put("options",list);
            specMapList.add(map);
        }


        /*specParams.forEach(specParam -> {
            StringTerms aggregation = (StringTerms)search.getAggregation(specParam.getName());
            List<StringTerms.Bucket> buckets = aggregation.getBuckets();
            buckets.forEach(bucket -> {
                String keyAsString = bucket.getKeyAsString();
                Map<String,Object> map=new HashMap<>();
                map.put("id",specParam.getName());
                map.put("name",keyAsString);
                specMapList.add(map);
            });
        });*/

        return specMapList;
    }

    private List<Brand> getBrandList(LongTerms brands) {

        List<Brand> list=new ArrayList<>();
        List<LongTerms.Bucket> buckets = brands.getBuckets();
        buckets.forEach(bucket -> {
            String keyAsString = bucket.getKeyAsString();
            Brand brandByBid = brandClient.findBrandByBid(NumberUtils.toLong(keyAsString));
            list.add(brandByBid);
        });

        return list;
    }

    public List<Map<String,Object>> getCategoryListMap(LongTerms brandLong){

        List<LongTerms.Bucket> buckets = brandLong.getBuckets();
        List<Map<String,Object>> categoryListMap=new ArrayList<>();
        buckets.forEach(bucket -> {
            String keyAsString = bucket.getKeyAsString();
            List<String> categoryNameByCids = categoryClient.findCategoryNameByCids(Arrays.asList(NumberUtils.toLong(keyAsString)));
            Map<String ,Object> map=new HashMap<>();
            map.put("id",keyAsString);
            map.put("name",categoryNameByCids.get(0));
            categoryListMap.add(map);
        });

        return categoryListMap;
    }


    private Object chooseSegement(String o, SpecParam specParam) {

        Double v = NumberUtils.toDouble(o);

        for (String segment : specParam.getSegments().split(",")) {
            String[] split = segment.split("-");
            Double begin = NumberUtils.toDouble(split[0]);
            Double end = Double.MAX_VALUE;
            if (split.length == 2) {
                end = NumberUtils.toDouble(split[1]);
                if (begin < v && v < end) {
                    return begin + "-" + end + specParam.getUnit();
                } else {
                    return begin + specParam.getUnit() + "以下";
                }
            } else {
                return begin + specParam.getUnit() + "以上";
            }

        }
        return "其他";
    }


}
