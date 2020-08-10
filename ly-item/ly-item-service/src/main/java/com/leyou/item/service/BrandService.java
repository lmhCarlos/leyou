package com.leyou.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.item.dto.BrandDTO;
import com.leyou.item.dto.PageDTO;
import com.leyou.item.entity.Brand;

import java.util.List;


public interface BrandService extends IService<Brand> {
    PageDTO<BrandDTO> queryBrandByPage(Integer page, Integer rows, String key);

    List<Brand> queryBrandByCategoryId(Long categoryId);

    void saveBrand(BrandDTO brandDTO);
}
