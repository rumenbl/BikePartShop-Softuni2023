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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CartServiceTest {
    @Autowired
    private CartService cartService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemsRepository cartItemsRepository;
    @Autowired
    private BikePartService bikePartService;
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
    void test_getOpenUserCart_returnsCartWhenGivenValidUser() {
        User user = userRepository.findById(1L).get(); // admin user
        Optional<Cart> cart = cartService.getOpenUserCart(user);
        assertTrue(cart.isPresent());
    }

    @Test
    void test_getOpenUserCart_returnsNullWhenGivenInvalidUser() {
        User user = userRepository.findById(1L).get();
        user.setId(2L);
        Optional<Cart> cart = cartService.getOpenUserCart(user);
        assertTrue(cart.isEmpty());
    }

    @Test
    void test_getCartContent_returnsContentWhenGivenValidId() {
        Optional<Cart> cart = cartRepository.findById(1L);
        assertFalse(cartService.getCartContent(cart.get().getId()).isEmpty());
    }

    @Test
    void test_save_doesNotThrowExceptionWhenGivenValidParameters() {
        CartItems cartItems = cartItemsRepository.findById(1L).get();
        cartItems.setAmount(2);
        Cart cart = cartRepository.findById(1L).get();
        cartService.save(cart, cartItems);
        assertEquals(2, cartItemsRepository.findById(1L).get().getAmount());
    }

    @Test
    void test_closeUserCart_closesUserCartWhenGivenValidUser() {
        User user = userRepository.findById(1L).get();
        cartService.closeUserCart(user);
        assertEquals("closed", cartRepository.findById(1L).get().getStatus());
    }

    @Test
    void test_addPartToCart_incrementsPartAmountWhenGivenExistingPart() {
        cartService.addPartToCart(userRepository.findById(1L).get(), 1L);
        assertEquals(2, cartRepository.findById(1L).get().getCartItems().stream().toList().get(0).getAmount());
    }
    @Test
    void test_addPartToCart_incrementsPartAmountWhenGivenExistingCart() {
        PartCreateDTO partCreateDTO = new PartCreateDTO();
        partCreateDTO.setTitle("title2");
        partCreateDTO.setPrice(1.0);
        partCreateDTO.setStock(1);
        partCreateDTO.setBrand("brand1");
        partCreateDTO.setCategory(BikePartCategoryEnum.BRAKES);
        partCreateDTO.setPictureUrl("pictureUrl1");
        bikePartService.savePart(partCreateDTO);

        cartService.addPartToCart(userRepository.findById(1L).get(), 2L);
        assertEquals(2, cartRepository.findById(1L).get().getCartItems().size());
    }

    @Test
    void test_addPartToCart_createsAndSavesNewCartWhenGivenEmptyCart() {
        User adminUser = userRepository.findById(1L).get();
        cartService.closeUserCart(adminUser);
        cartService.addPartToCart(adminUser, 1L);
        assertEquals(1, cartService.getOpenUserCart(adminUser).get().getCartItems().size());
    }
}