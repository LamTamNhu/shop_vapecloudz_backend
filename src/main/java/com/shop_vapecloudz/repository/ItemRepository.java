package com.shop_vapecloudz.repository;

import com.shop_vapecloudz.model.Item;
import com.shop_vapecloudz.model.dto.IItemDTO;
import com.shop_vapecloudz.model.dto.ItemDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = "select i.id itemId, i.name itemName, description, im.url, iv.price from item i " +
                   "join item_image im on i.item_image_id = im.id " +
                   "join item_variant iv on i.id = iv.item_id " +
                   "where i.name like concat('%', :name, '%') and (i.brand_id=:brandId or :brandId is null) " +
                   "and (i.category_id=:categoryId or :categoryId is null) " +
                   "group by i.id, iv.price", nativeQuery = true)
    Page<IItemDTO> findAllItems(Pageable pageable, Long brandId, Long categoryId, String name);
}
