package com.dev.mainbackend.security.services.ge;

import com.dev.mainbackend.models.ge.ItemGe;
import com.dev.mainbackend.repository.ge.ItemRepositoryGe;
import com.dev.mainbackend.request.ItemsRequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceGe {
    private final ItemRepositoryGe itemRepositoryGe;

    @Autowired
    public ItemServiceGe(ItemRepositoryGe itemRepositoryGe) {
        this.itemRepositoryGe = itemRepositoryGe;
    }

    public ItemGe saveItem(ItemGe itemGe) {
        return itemRepositoryGe.save(itemGe);
    }

    public List<ItemGe> getAllItems() {
        return itemRepositoryGe.findAll();
    }

    public Optional<ItemGe> getItemById(String itemId) {
        return itemRepositoryGe.findById(itemId);
    }

    public  Optional<ItemGe> updateItem(String itemId, ItemsRequests addItemRequest) throws IOException {
        Optional<ItemGe> existingItemOptional = itemRepositoryGe.findById(itemId);
        if (existingItemOptional.isPresent()) {
            ItemGe existingItemEn = existingItemOptional.get();
            existingItemEn.copyProperties(addItemRequest);
            return Optional.of(itemRepositoryGe.save(existingItemEn));
        } else {
            return Optional.empty(); // Item with the given ID not found
        }
    }

    public boolean deleteItem(String itemId) {
        Optional<ItemGe> itemOptional = itemRepositoryGe.findById(itemId);

        if (itemOptional.isPresent()) {
            itemRepositoryGe.deleteById(itemId);
            return true; // Item deleted successfully
        } else {
            return false; // Item with the given ID not found
        }
    }
}
