package com.leyou.item.service;

import com.leyou.common.execption.LyException;
import com.leyou.item.entity.Item;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ItemService {

    public Item saveItem(Item item){

        if (item.getPrice()== null){
            throw new LyException("价格不能为空",400);

        }

        if (item.getName()==null){
            throw new LyException("商品名称不能为空",400);
        }

        int id= new Random().nextInt(100);
        item.setId(id);
        return item;
    }
}
