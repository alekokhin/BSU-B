package com.dev.mainbackend.security.services.en;

import com.dev.mainbackend.models.en.ItemEn;
import com.dev.mainbackend.repository.en.ItemRepository;
import com.dev.mainbackend.request.ItemsRequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceEn {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceEn(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ItemEn saveItem(ItemEn itemEn) {
        return itemRepository.save(itemEn);
    }

    public List<ItemEn> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<ItemEn> getItemById(String itemId) {
        return itemRepository.findById(itemId);
    }

    public Optional<ItemEn> updateItem(String itemId, ItemsRequests addItemRequest) throws IOException {
        Optional<ItemEn> existingItemOptional = itemRepository.findById(itemId);

        if (existingItemOptional.isPresent()) {
            ItemEn existingItemEn = existingItemOptional.get();
            existingItemEn.copyProperties(addItemRequest);
            return Optional.of(itemRepository.save(existingItemEn));
        } else {
            return Optional.empty(); // Item with the given ID not found
        }
    }

    public boolean deleteItem(String itemId) {
        Optional<ItemEn> itemOptional = itemRepository.findById(itemId);

        if (itemOptional.isPresent()) {
            itemRepository.deleteById(itemId);
            return true; // Item deleted successfully
        } else {
            return false; // Item with the given ID not found
        }
    }
}
