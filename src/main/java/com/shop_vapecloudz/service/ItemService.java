package com.shop_vapecloudz.service;

import com.shop_vapecloudz.model.Item;
import com.shop_vapecloudz.model.ItemVariant;
import com.shop_vapecloudz.model.UserCart;
import com.shop_vapecloudz.model.UserEntity;
import com.shop_vapecloudz.model.dto.*;
import com.shop_vapecloudz.repository.ItemRepository;
import com.shop_vapecloudz.repository.ItemVariantRepository;
import com.shop_vapecloudz.repository.UserCartRepository;
import com.shop_vapecloudz.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService implements IItemService {
    private final ItemRepository itemRepository;
    private final UserCartRepository userCartRepository;
    private final UserRepository userRepository;
    private final ItemVariantRepository itemVariantRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, UserCartRepository userCartRepository, UserRepository userRepository, ItemVariantRepository itemVariantRepository) {
        this.itemRepository = itemRepository;
        this.userCartRepository = userCartRepository;
        this.userRepository = userRepository;
        this.itemVariantRepository = itemVariantRepository;
    }

    @Override
    public Page<IItemDTO> findAll(Pageable pageable, Long brandId, Long categoryId, String name) {
        return itemRepository.findAllItems(pageable, brandId, categoryId, name);
    }

    @Override
    public void addToCartByUserEmail(CartEditDTO cartEditDTO) {
        UserCart userCart = userCartRepository.findByUserEntityEmailAndItemVariantId(cartEditDTO.getEmail(), cartEditDTO.getVariantId()).orElse(null);
        if (userCart != null) {
            userCart.setAmount(userCart.getAmount() + cartEditDTO.getAmount());
        } else {
            UserEntity userEntity = userRepository.findByEmail(cartEditDTO.getEmail()).orElse(null);
            userCart = new UserCart();
            userCart.setUserEntity(userEntity);
            ItemVariant itemVariant = itemVariantRepository.findById(cartEditDTO.getVariantId()).orElse(null);
            userCart.setItemVariant(itemVariant);
            userCart.setAmount(cartEditDTO.getAmount());
        }
        userCartRepository.save(userCart);
    }

    @Override
    public ItemDetailDTO findItemDetailById(Long id) {
        ItemDetailDTO itemDetailDTO = new ItemDetailDTO();
        Item item = itemRepository.findById(id).orElse(null);
        if (item == null) {
            return null;
        }
        ItemDTO itemDTO = new ItemDTO();
        BeanUtils.copyProperties(item, itemDTO);
        itemDTO.setImageUrl(item.getItemImage().getUrl());
        itemDetailDTO.setItemDTO(itemDTO);
        List<IItemVariantDTO> itemVariantDTOS = itemVariantRepository.findAllByItem_Id(id);
        itemDetailDTO.setItemVariantDTOS(itemVariantDTOS);
        return itemDetailDTO;
    }

    @Override
    public List<IUserCartDTO> getCartByUserEmail(String email) {
        return userCartRepository.findAllByUserEntityEmail(email);
    }

    @Override
    public void editCart(CartEditDTO cartEditDTO) {
        UserCart userCart = userCartRepository.findById(cartEditDTO.getCartId()).orElse(null);
        if (userCart == null) {
            return;
        }
        if (cartEditDTO.getAmount() == 0) {
            userCartRepository.delete(userCart);
            return;
        }
        userCart.setAmount(cartEditDTO.getAmount());
        userCartRepository.save(userCart);
    }
}
