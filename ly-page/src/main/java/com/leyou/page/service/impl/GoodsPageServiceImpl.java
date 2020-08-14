package com.leyou.page.service.impl;

import com.leyou.common.utils.BeanHelper;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.client.ItemClient;
import com.leyou.item.dto.*;
import com.leyou.page.dto.SpecGroupNameDTO;
import com.leyou.page.dto.SpecParamNameDTO;
import com.leyou.page.service.GoodsPageService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GoodsPageServiceImpl implements GoodsPageService {
    private final ItemClient itemClient;
    private final StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX_SPU = "page:spu:id:";
    private static final String KEY_PREFIX_SKU = "page:sku:id:";
    private static final String KEY_PREFIX_DETAIL = "page:detail:id:";
    private static final String KEY_PREFIX_CATEGORY = "page:category:id:";
    private static final String KEY_PREFIX_BRAND = "page:brand:id:";
    private static final String KEY_PREFIX_SPEC = "page:spec:id:";

    public GoodsPageServiceImpl(ItemClient itemClient, StringRedisTemplate redisTemplate) {
        this.itemClient = itemClient;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String loadSpuData(Long spuId) {
        /* spuDTO属性
            private Long id;
            private Long brandId;
            private Long cid1;// 1级类目
            private Long cid2;// 2级类目
            private Long cid3;// 3级类目
            private String name;// 名称，如iphoneX
            private String title;// 标题
            private Boolean saleable;// 是否上架
            private String categoryName; // 商品分类名称拼接
            private String brandName;// 品牌名称,如手机
            private SpuDetailDTO spuDetail;  //商品详情（规格参数等）
            private List<SkuDTO> skus;spu下的sku的集合
        */
        SpuDTO spuDTO = itemClient.querySpuById(spuId);
        //组织数据
        Map<String,Object> map=new HashMap<>();
        map.put("id",spuDTO.getId());   //商品的编号
        map.put("name",spuDTO.getName());  //商品的名字 如 iPhone 11
        map.put("categoryIds",spuDTO.getCategoryIds()); //商品对应的类ids  手机通信/手机/手机
        map.put("brandId",spuDTO.getBrandId()); //品牌Id
        String json = JsonUtils.toJson(map); //把数据转成json存储到redis
        redisTemplate.opsForValue().set(KEY_PREFIX_SPU+spuId,json);
        return json;
    }

    @Override
    public String loadSpuDetailData(Long spuId) {
        /*
         private Long spuId;// 对应的SPU的id
         private String description;// 商品描述
         private String packingList;// 包装清单
         private String afterService;// 售后服务
         private String specification;// 规格参数
       */

        SpuDetailDTO spuDetail= itemClient.querySpuDetailById(spuId);
        String json = JsonUtils.toJson(spuDetail);
        redisTemplate.opsForValue().set(KEY_PREFIX_DETAIL+spuId,json);
        return json;
    }

    @Override
    public String loadSkuListData(Long spuId) {
        /*
        *   private Long id;
            private Long spuId;
            private String title;
            private String images;
            private Long price;
            private String specialSpec;// 商品特殊规格的键值对
            private String indexes;// 商品特殊规格的下标
            private Boolean saleable;// 是否有效，逻辑删除用
            private Integer stock; // 库存
            private Long sold; // 销量
        * */
        List<SkuDTO> skuDTOS = itemClient.querySkuBySpuId(spuId);
        String json = JsonUtils.toJson(skuDTOS);
        redisTemplate.opsForValue().set(KEY_PREFIX_SKU +spuId,json);
        return json;
    }

    @Override
    public String loadCategoriesData(List<Long> ids) {
        /* categoryDTOS属性
         private Long id;
         private String name;
         private Long parentId;
         private Boolean isParent;
         private Integer sort;
        * */
        List<CategoryDTO> list = itemClient.queryCategoryByIds(ids);
        List<Map<String, Object>> categoryList = list.stream().map(categoryDTO -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", categoryDTO.getId());
            map.put("name", categoryDTO.getName());
            return map;
        }).collect(Collectors.toList());

        String json = JsonUtils.toJson(categoryList);
        redisTemplate.opsForValue().set(KEY_PREFIX_CATEGORY+ids.get(2),json);
        return json;
    }

    @Override
    public String loadBrandData(Long brandId) {
        /* BrandDTO 属性
        private Long id;
        private String image;
        private String name;
        private Character letter;  //品牌首字母
        private List<Long> categoryIds;
        * */
        BrandDTO brandDTO = itemClient.queryBrandById(brandId);
        Map<String,Object> map= new HashMap<>();
        map.put("id",brandDTO.getId());
       // map.put("image",brandDTO.getImage());
        map.put("name",brandDTO.getName());
       // map.put("letter",brandDTO.getLetter());
        String json = JsonUtils.toJson(map);
        redisTemplate.opsForValue().set(KEY_PREFIX_BRAND+brandId,json);
        return json;
    }

    @Override
    public String loadSpecData(Long categoryId) {
        List<SpecGroupDTO> list = itemClient.querySpecList(categoryId);
        List<SpecGroupNameDTO> groupList=new ArrayList<>();
        for (SpecGroupDTO groupDTO : list) {
            SpecGroupNameDTO nameDTO = new SpecGroupNameDTO();
            nameDTO.setName(groupDTO.getName());
            nameDTO.setParams(BeanHelper.copyWithCollection(groupDTO.getParams(), SpecParamNameDTO.class));
            groupList.add(nameDTO);
        }

        String json = JsonUtils.toJson(groupList);
        redisTemplate.opsForValue().set(KEY_PREFIX_SPEC+categoryId,json);
        return json;
    }
}
