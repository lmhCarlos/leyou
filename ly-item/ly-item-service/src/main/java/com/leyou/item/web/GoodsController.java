package com.leyou.item.web;

import com.leyou.item.dto.PageDTO;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.dto.SpuDTO;
import com.leyou.item.service.SkuService;
import com.leyou.item.service.SpuDetailService;
import com.leyou.item.service.SpuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    public ResponseEntity<Void> saveGoods(@RequestBody SpuDTO spuDTO){
            spuService.saveGoods(spuDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("saleable")
    public ResponseEntity<Void> updateSaleable(@RequestParam("id") Long id,@RequestParam("saleable") Boolean saleable){
        spuService.updateSaleable(id,saleable);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<SpuDTO> queryGoodsById(@PathVariable("id") Long id){
        return ResponseEntity.ok(spuService.queryGoodsById(id));
    }

    @PutMapping("/sku/list")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuDTO spuDTO){
        spuService.updateGoods(spuDTO);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/spec/value")
    public ResponseEntity<List<SpecParamDTO>> querySpecsValues(
            @RequestParam("id") Long id, @RequestParam(value = "searching", required = false) Boolean searching){
        return ResponseEntity.ok(spuDetailService.querySpecValues(id, searching));
    }


    @GetMapping("spu/{id}")
    public ResponseEntity<SpuDTO> querySpuById(@PathVariable("id") Long id){
        return ResponseEntity.ok(new SpuDTO(spuService.getById(id)));
    }
}
