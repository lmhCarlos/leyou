package com.leyou.item.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyou.item.dto.CategoryDTO;
import com.leyou.item.dto.PageDTO;
import com.leyou.item.dto.BrandDTO;
import com.leyou.item.entity.Brand;
import com.leyou.item.entity.CategoryBrand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
   private BrandService brandService;

    @GetMapping("{id}")
    public ResponseEntity<BrandDTO> queryBrandById(@PathVariable("id") Long id){
        Brand brand=brandService.getById(id);
        BrandDTO brandDTO = new BrandDTO(brand);
        return ResponseEntity.ok(brandDTO);
    }

    @GetMapping("list")
    public ResponseEntity<List<BrandDTO>> queryBrandByIds(@RequestParam("ids") List<Long> ids){
        List<Brand> brands = brandService.listByIds(ids);
        List<BrandDTO> brandDTOS = BrandDTO.convertEntityList(brands);
        return ResponseEntity.ok(brandDTOS);
    }

    @GetMapping("page")
    public ResponseEntity<PageDTO<BrandDTO>> queryBrandByPage(@RequestParam(value = "key",required = false) String key,
                                                   @RequestParam(value = "page",defaultValue = "1") Integer page,
                                                   @RequestParam(value = "rows",defaultValue = "5") Integer rows){
        return ResponseEntity
                .ok(brandService.queryBrandByPage(page,rows, key));
    }


    @GetMapping("of/category")
    public List<BrandDTO> queryBrandByCategoryId(@RequestParam("id") Long categoryId){
        List<Brand> brandList=brandService.queryBrandByCategoryId(categoryId);
        return BrandDTO.convertEntityList(brandList);
    }


    @PostMapping
    public ResponseEntity<Void> addBrand(BrandDTO brandDTO){
        brandService.saveBrand(brandDTO);


        return ResponseEntity.status(HttpStatus.CREATED).build();

    }
}
