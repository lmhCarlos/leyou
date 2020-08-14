package com.leyou.page.web;


import com.leyou.page.service.GoodsPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("page")
public class PageController {

    private final GoodsPageService goodsPageService;

    public PageController(GoodsPageService goodsPageService) {
        this.goodsPageService = goodsPageService;

    }

    @GetMapping("/spu/{id}")
    public ResponseEntity<String> querySpuPageData(@PathVariable("id") Long spuId){
        return ResponseEntity.ok(goodsPageService.loadSpuData(spuId));
    }

    @GetMapping("/sku/{id}")
    public ResponseEntity<String> querySkuPageData(@PathVariable("id") Long spuId){
        return ResponseEntity.ok(goodsPageService.loadSkuListData(spuId));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<String> queryDetailPageData(@PathVariable("id") Long spuId){
        return ResponseEntity.ok(goodsPageService.loadSpuDetailData(spuId));
    }

    @GetMapping("/categories")
    public ResponseEntity<String> queryCategoriesPageData(@RequestParam("ids") List<Long> ids){
        return ResponseEntity.ok(goodsPageService.loadCategoriesData(ids));
    }


    @GetMapping("/brand/{id}")
    public ResponseEntity<String> queryBrandPageData(@PathVariable("id") Long id){
        return ResponseEntity.ok(goodsPageService.loadBrandData(id));
    }


    @GetMapping("/spec/{id}")
    public ResponseEntity<String> queryGoodsPageData(@PathVariable("id") Long categoryId){
        return ResponseEntity.ok(goodsPageService.loadSpecData(categoryId));
    }

}
