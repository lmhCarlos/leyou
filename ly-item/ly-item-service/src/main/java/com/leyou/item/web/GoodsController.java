package com.leyou.item.web;

import com.leyou.item.dto.PageDTO;
import com.leyou.item.dto.SkuDTO;
import com.leyou.item.dto.SpuDTO;
import com.leyou.item.service.SkuService;
import com.leyou.item.service.SpuDetailService;
import com.leyou.item.service.SpuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    private final SpuService spuService;

    private final SkuService skuService;

    private final SpuDetailService spuDetailService;

    public GoodsController(SpuService spuService, SkuService skuService, SpuDetailService spuDetailService) {
        this.spuService = spuService;
        this.skuService = skuService;
        this.spuDetailService = spuDetailService;
    }


    @GetMapping("/spu/page")
    public ResponseEntity<PageDTO<SpuDTO>> queryGoodsByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "brandId", required = false) Long brandId,
            @RequestParam(value = "id", required = false) Long id
    ){
        PageDTO<SpuDTO> pageDTO =spuService.queryGoodsByPage(brandId, categoryId, id, page, rows, saleable);
        return ResponseEntity.ok(pageDTO);
    }
}
