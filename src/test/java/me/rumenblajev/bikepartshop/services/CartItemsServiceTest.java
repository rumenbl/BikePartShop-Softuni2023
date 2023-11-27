package me.rumenblajev.bikepartshop.services;

import me.rumenblajev.bikepartshop.enums.BikePartCategoryEnum;
import me.rumenblajev.bikepartshop.models.dto.PartCreateDTO;
import me.rumenblajev.bikepartshop.models.entity.Cart;
import me.rumenblajev.bikepartshop.models.entity.CartItems;
import me.rumenblajev.bikepartshop.models.entity.User;
import me.rumenblajev.bikepartshop.repositories.CartItemsRepository;
import me.rumenblajev.bikepartshop.repositories.CartRepository;
import me.rumenblajev.bikepartshop.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CartItemsServiceTest {
    @Autowired
    private BikePartService bikePartService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemsRepository cartItemsRepository;
    @Autowired
    private CartItemsService cartItemsService;

    @BeforeEach
    void setUp() {
        PartCreateDTO partCreateDTO = new PartCreateDTO();
        partCreateDTO.setTitle("title1");
        partCreateDTO.setPrice(1.0);
        partCreateDTO.setStock(1);
        partCreateDTO.setBrand("brand1");
        partCreateDTO.setCategory(BikePartCategoryEnum.BRAKES);
        partCreateDTO.setPictureUrl("pictureUrl1");
        bikePartService.savePart(partCreateDTO);

        partCreateDTO.setTitle("title2");
        bikePartService.savePart(partCreateDTO);
        Cart cart = new Cart();
        cart.setUser(userRepository.findById(1L).get()); // admin user
        cartRepository.save(cart);
        CartItems cartItems = new CartItems();
        cartItems.setCart(cart);
        cartItems.setPart(bikePartService.findById(1L).get());
        cartItems.setAmount(1);
        cartItemsRepository.save(cartItems);
    }

    @Test
    void test_findPartInCart_ReturnsPartWhenGivenValidParameters() {
        var result = cartItemsService.findPartInCart
                (cartRepository.findById(1L).get(),
                bikePartService.findById(1L).get());

        assertTrue(result.isPresent());
    }

    @Test
    void test_findPartInCart_ReturnsNullWhenGivenInvalidParameters() {
        var result = cartItemsService.findPartInCart(
                cartRepository.findById(1L).get(),
                bikePartService.findById(2L).get()
        );

        assertTrue(result.isEmpty());
    }

    @Test
    void test_getCartItems_returnsAllItemsCorrectly() {
        var result = cartItemsService.getCartItems();

        assertEquals(1, result.size());
    }

    @Test
    void test_removeById_removesEntityByGivenValidId() {
        cartItemsService.removeById(1L);
        var result = cartItemsService.getCartItems();
        assertEquals(0,result.size());
    }

    @Test
    void test_removeById_doesntRemoveEntityWhenGivenInvalidId() {
        cartItemsService.removeById(100L);
        var result = cartItemsService.getCartItems();
        assertEquals(1,result.size());
    }

    @Test
    void test_findItemsByUser_returnsItemsCorrectlyWhenGivenValidUser() {
        var result = cartItemsService.findItemsByUser(userRepository.findById(1L).get());
        assertEquals(1,result.size());
    }

    @Test
    void test_findItemsByUser_returnsNothingWhenGivenUserWithEmptyCart() {
        User user = userRepository.findById(1L).get();
        user.setId(2L);
        user.setEmail("test@test.bg");
        user.setUsername("testingBro");
        userRepository.save(user);
        var result = cartItemsService.findItemsByUser(userRepository.findById(2L).get());
        assertEquals(0,result.size());
    }

    @Test
    void test_saveCartItems_savesCartItems() {
        var cartItems = cartItemsRepository.findById(1L).get();
        cartItems.setAmount(100);
        cartItemsService.saveCartItems(cartItems);

        assertEquals(100,cartItemsRepository.findById(1L).get().getAmount());
    }

    @Test
    void test_closeUserCart_closesUserCart() {
        var user = userRepository.findById(1L).get();
        cartItemsService.closeUserCartItems(user);
        var result = cartItemsService.findItemsByUser(user);

        assertEquals(0,result.size());
    }
}