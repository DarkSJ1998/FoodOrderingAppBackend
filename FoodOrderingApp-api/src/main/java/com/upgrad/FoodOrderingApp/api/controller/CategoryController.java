package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.business.CategoryItemService;
import com.upgrad.FoodOrderingApp.service.business.CategoryService;
import com.upgrad.FoodOrderingApp.service.business.ItemService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryItemService categoryItemService;

    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public ResponseEntity getAllCategories() {

        System.out.println("\n\t ==> CategoryController.getAllCategories() called");

        // Getting a list of all the categories.
        List <CategoryEntity> categories = categoryService.getAllCategories();

        // Creating a new list which is to be added in response.
        List <CategoryListResponse> list = new ArrayList<>();

        for(CategoryEntity categoryEntity : categories) {

            // Creating a CategoryListResponse object.
            CategoryListResponse categoryListResponse = new CategoryListResponse()
                    .id( UUID.fromString( categoryEntity.getUuid() ) )
                    .categoryName( categoryEntity.getCategoryName() );

            // Adding it to CategoryListResponse list.
            list.add(categoryListResponse);

        }

        CategoriesListResponse response = new CategoriesListResponse()
                .categories(list);

        return new ResponseEntity<CategoriesListResponse> (response, HttpStatus.FOUND);
    }

    @RequestMapping(value = "/category/{category_id}", method = RequestMethod.GET)
    public ResponseEntity getCategoryById(@PathVariable("category_id") final String categoryUuid) {

        System.out.println("\n\t ==> CategoryController.getCategoryById() called");

        try {

            // Getting the CategoryEntity object with the UUID.
            CategoryEntity categoryEntity = categoryService.getCategoryUsingUuid(categoryUuid);

            // For getting all the items for the particular category
            List<CategoryItemEntity> list = categoryItemService
                    .getItemsUsingCategoryId(categoryEntity.getId());

            // For storing the items for the particular category
            List<ItemList> itemList = new ArrayList<>();

            for (CategoryItemEntity categoryItemEntity : list) {

                // Getting the ItemEntity object using ID.
                ItemEntity item = itemService.getItemUsingId(categoryItemEntity.getItemId());

                ItemList itemListObject = new ItemList()
                        .id(UUID.fromString(item.getUuid()))
                        .itemName(item.getItemName())
                        .price(item.getPrice());

                // Setting the type of item.
                if (item.getType().equals("0"))
                    itemListObject.setItemType(ItemList.ItemTypeEnum.VEG);
                else if (item.getType().equals("1"))
                    itemListObject.setItemType(ItemList.ItemTypeEnum.NON_VEG);

                // Adding item to the list.
                itemList.add(itemListObject);
            }

            CategoryDetailsResponse response = new CategoryDetailsResponse()
                    .id( UUID.fromString( categoryEntity.getUuid() ) )
                    .categoryName( categoryEntity.getCategoryName() )
                    .itemList( itemList );

            return new ResponseEntity<CategoryDetailsResponse> (response, HttpStatus.FOUND);

        } catch (CategoryNotFoundException e) {
            ErrorResponse response = new ErrorResponse()
                    .code(e.getCode())
                    .message(e.getErrorMessage());

            return new ResponseEntity<ErrorResponse>(response, HttpStatus.NOT_FOUND);
        }
    }

}
