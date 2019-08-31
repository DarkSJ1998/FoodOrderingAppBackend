package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.business.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private StateService stateService;

    @Autowired
    private RestaurantCategoryService restaurantCategoryService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryItemService categoryItemService;

    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "/restaurant/name/{restaurant_name}", method = RequestMethod.GET)
    public ResponseEntity getRestaurantsByName(@PathVariable(value = "restaurant_name") final String restaurantName) {

        System.out.println("\n\t ==> RestaurantController.getRestaurantsByName() called");

        try {
            List<RestaurantEntity> list = restaurantService.getRestaurantsByName(restaurantName);
            List <RestaurantList> restaurantLists = new ArrayList<>();

            for(RestaurantEntity restaurantEntity : list) {

                Long restaurantId = restaurantEntity.getId();

                List <RestaurantCategoryEntity> restaurantCategoryList = restaurantCategoryService.getCategoriesUsingRestaurantId(restaurantId);

                String category = new String();
                List <String> categories = new ArrayList<>();

                for(RestaurantCategoryEntity restaurantCategoryEntity : restaurantCategoryList) {

                    CategoryEntity categoryEntity = categoryService.getCategoryUsingId(restaurantCategoryEntity.getCategoryId());

                    categories.add(categoryEntity.getCategoryName());
                }

                Collections.sort(categories);
                category = categories.toString().substring(1, categories.toString().length()-1);

                AddressEntity addressEntity = addressService.getAddressById(restaurantEntity.getAddressId());
                StateEntity stateEntity = stateService.getStateById(addressEntity.getStateId());

                RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                        .id( UUID.fromString( stateEntity.getUuid() ) )
                        .stateName( stateEntity.getStateName() );

                RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                        .id( UUID.fromString( addressEntity.getUuid() ) )
                        .flatBuildingName( addressEntity.getFlatBuilNumber() )
                        .locality( addressEntity.getLocality() )
                        .city( addressEntity.getCity() )
                        .pincode( addressEntity.getPincode() )
                        .state(restaurantDetailsResponseAddressState);

                RestaurantList restaurantList = new RestaurantList()
                        .id( UUID.fromString( restaurantEntity.getUuid() ) )
                        .restaurantName( restaurantEntity.getRestaurantName() )
                        .photoURL( restaurantEntity.getPhotoUrl() )
                        .customerRating( new BigDecimal(restaurantEntity.getCustomerRating()) )
                        .averagePrice( restaurantEntity.getAveragePriceForTwo() )
                        .numberCustomersRated( restaurantEntity.getNumberOfCustomersRated() )
                        .address( restaurantDetailsResponseAddress )
                        .categories( category );

                restaurantLists.add(restaurantList);
            }
            RestaurantListResponse response = new RestaurantListResponse()
                    .restaurants(restaurantLists);

            return new ResponseEntity<RestaurantListResponse>(response, HttpStatus.OK);

        } catch (RestaurantNotFoundException e) {
            ErrorResponse response = new ErrorResponse()
                    .code(e.getCode())
                    .message(e.getErrorMessage());

            return new ResponseEntity<ErrorResponse>(response, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/restaurant/category/{category_id}", method = RequestMethod.GET)
    public ResponseEntity getRestaurantsByCategory(@PathVariable(value = "category_id") final String categoryUuid) {

        System.out.println("\n\t ==> RestaurantController.getRestaurantsByCategory() called");

        try {
            List <RestaurantList> restaurantLists = new ArrayList<>();

            CategoryEntity categoryEntity = categoryService.getCategoryUsingUuid(categoryUuid);

            List <RestaurantCategoryEntity> restaurantCategoryEntityList = restaurantCategoryService
                    .getRestaurantsUsingCategoryId(categoryEntity.getId());

            for(RestaurantCategoryEntity restaurantCategoryEntity : restaurantCategoryEntityList) {
                RestaurantEntity restaurantEntity = restaurantService.getRestaurantById(restaurantCategoryEntity.getRestaurantId());

                Long restaurantId = restaurantEntity.getId();
                List <RestaurantCategoryEntity> restaurantCategoryList = restaurantCategoryService.getCategoriesUsingRestaurantId(restaurantId);

                String category = new String();
                List <String> categories = new ArrayList<>();
                for(RestaurantCategoryEntity restaurantCategoryEntity2 : restaurantCategoryList) {

                    CategoryEntity categoryEntity2 = categoryService.getCategoryUsingId(restaurantCategoryEntity2.getCategoryId());

                    categories.add(categoryEntity2.getCategoryName());
                }

                Collections.sort(categories);
                category = categories.toString().substring(1, categories.toString().length()-1);
                AddressEntity addressEntity = addressService.getAddressById(restaurantEntity.getAddressId());
                StateEntity stateEntity = stateService.getStateById(addressEntity.getStateId());

                RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                        .id( UUID.fromString( stateEntity.getUuid() ) )
                        .stateName( stateEntity.getStateName() );

                RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                        .id( UUID.fromString( addressEntity.getUuid() ) )
                        .flatBuildingName( addressEntity.getFlatBuilNumber() )
                        .locality( addressEntity.getLocality() )
                        .city( addressEntity.getCity() )
                        .pincode( addressEntity.getPincode() )
                        .state(restaurantDetailsResponseAddressState);

                RestaurantList restaurantList = new RestaurantList()
                        .id( UUID.fromString( restaurantEntity.getUuid() ) )
                        .restaurantName( restaurantEntity.getRestaurantName() )
                        .photoURL( restaurantEntity.getPhotoUrl() )
                        .customerRating( new BigDecimal(restaurantEntity.getCustomerRating()) )
                        .averagePrice( restaurantEntity.getAveragePriceForTwo() )
                        .numberCustomersRated( restaurantEntity.getNumberOfCustomersRated() )
                        .address( restaurantDetailsResponseAddress )
                        .categories( category );

                restaurantLists.add(restaurantList);
            }

            RestaurantListResponse response = new RestaurantListResponse()
                    .restaurants(restaurantLists);

            return new ResponseEntity<RestaurantListResponse>(response, HttpStatus.OK);

        } catch (CategoryNotFoundException e) {
            ErrorResponse response = new ErrorResponse()
                    .code(e.getCode())
                    .message(e.getErrorMessage());

            return new ResponseEntity<ErrorResponse>(response, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/api/restaurant/{restaurant_id}", method = RequestMethod.GET)
    public ResponseEntity getRestaurantsByID(@PathVariable(value = "restaurant_id") final String restaurantUuid) {

        System.out.println("\n\t ==> RestaurantController.getRestaurantsByID() called");

        try {

            // Getting the RestaurantEntity object using the restaurantUuid.
            RestaurantEntity restaurantEntity = restaurantService.getRestaurantByUuid(restaurantUuid);

            // Getting the RestaurantCategoryEntity object.
            List <RestaurantCategoryEntity> restaurantCategoryList = restaurantCategoryService
                    .getCategoriesUsingRestaurantId(restaurantEntity.getId());

            // Creating an empty CategoryEntity list.
            List <CategoryEntity> categories = new ArrayList<>();

            for(RestaurantCategoryEntity restaurantCategoryEntity : restaurantCategoryList) {

                // Getting a CategoryEntity object.
                CategoryEntity categoryEntity = categoryService.getCategoryUsingId(restaurantCategoryEntity.getCategoryId());

                // Saving it in CategoryEntity list.
                categories.add(categoryEntity);
            }

            // Creating an empty CategoryList list.
            List <CategoryList> categoryListList = new ArrayList<>();

            for(CategoryEntity category : categories) {

                // For getting all the items for the particular category
                List<CategoryItemEntity> list = categoryItemService
                        .getItemsUsingCategoryId(category.getId());

                // For storing the items for the particular category
                List<ItemList> itemList = new ArrayList<>();

                for (CategoryItemEntity categoryItemEntity : list) {

                    // Getting an ItemEntity object.
                    ItemEntity item = itemService.getItemUsingId(categoryItemEntity.getItemId());

                    // Creating an ItemList object and setting values.
                    ItemList itemListObject = new ItemList()
                            .id(UUID.fromString(item.getUuid()))
                            .itemName(item.getItemName())
                            .price(item.getPrice());

                    // Setting the item type.
                    if (item.getType().equals("0"))
                        itemListObject.setItemType(ItemList.ItemTypeEnum.VEG);
                    else if (item.getType().equals("1"))
                        itemListObject.setItemType(ItemList.ItemTypeEnum.NON_VEG);

                    // Adding the ItemEntity object to list.
                    itemList.add(itemListObject);
                }

                CategoryList categoryList = new CategoryList();
                categoryList
                        .id(UUID.fromString(category.getUuid()))
                        .categoryName(category.getCategoryName())
                        .itemList(itemList);

                // Saving CategoryList object in list.
                categoryListList.add(categoryList);
            }

            // Sorting the resultant Category List by their names.
            Collections.sort(categoryListList, new Comparator<CategoryList>() {
                @Override
                public int compare(CategoryList c1, CategoryList c2) {
                    return c1.getCategoryName().compareTo(c2.getCategoryName());
                }
            });

            AddressEntity addressEntity = addressService.getAddressById(restaurantEntity.getAddressId());
            StateEntity stateEntity = stateService.getStateById(addressEntity.getStateId());

            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState()
                    .id( UUID.fromString( stateEntity.getUuid() ) )
                    .stateName( stateEntity.getStateName() );

            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress()
                    .id( UUID.fromString( addressEntity.getUuid() ) )
                    .flatBuildingName( addressEntity.getFlatBuilNumber() )
                    .locality( addressEntity.getLocality() )
                    .city( addressEntity.getCity() )
                    .pincode( addressEntity.getPincode() )
                    .state(restaurantDetailsResponseAddressState);

            RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse()
                    .id( UUID.fromString( restaurantEntity.getUuid() ) )
                    .restaurantName( restaurantEntity.getRestaurantName() )
                    .photoURL( restaurantEntity.getPhotoUrl() )
                    .customerRating( new BigDecimal( restaurantEntity.getCustomerRating() ) )
                    .averagePrice( restaurantEntity.getAveragePriceForTwo() )
                    .numberCustomersRated( restaurantEntity.getNumberOfCustomersRated() )
                    .address( restaurantDetailsResponseAddress )
                    .categories( categoryListList );

            return new ResponseEntity<RestaurantDetailsResponse> (restaurantDetailsResponse, HttpStatus.FOUND);

        } catch (RestaurantNotFoundException e) {
            ErrorResponse response = new ErrorResponse()
                    .code(e.getCode())
                    .message(e.getErrorMessage());

            return new ResponseEntity<ErrorResponse>(response, HttpStatus.NOT_FOUND);
        }
    }

}
