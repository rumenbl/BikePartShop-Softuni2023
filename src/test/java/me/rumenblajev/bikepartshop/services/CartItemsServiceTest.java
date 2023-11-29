package me.rumenblajev.bikepartshop.services;

import me.rumenblajev.bikepartshop.models.entity.BikePart;
import me.rumenblajev.bikepartshop.models.entity.Cart;
import me.rumenblajev.bikepartshop.models.entity.CartItems;
import me.rumenblajev.bikepartshop.models.entity.User;
import me.rumenblajev.bikepartshop.repositories.CartItemsRepository;
import me.rumenblajev.bikepartshop.repositories.CartRepository;
import me.rumenblajev.bikepartshop.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CartItemsServiceTest {
    @Autowired
    private CartItemsService subject;
    @MockBean
    private BikePartService bikePartService;
    @MockBean
    private CartRepository cartRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CartItemsRepository cartItemsRepository;


    @BeforeEach
    void setUp() {
        var cart = new Cart();
        cart.setId(1L);
        var part = new BikePart();
        part.setId(1L);
        var cartItems = new CartItems(part,cart);
        cart.setCartItems(Collections.singleton(cartItems));

        when(cartItemsRepository.findById(1L)).thenReturn(Optional.of(cartItems));
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(bikePartService.findById(1L)).thenReturn(Optional.of(part));
    }

    @Test
    void test_findPartInCart_ReturnsPartWhenGivenValidParameters() {
        when(cartItemsRepository.findByPartIdAndCartId(1L,1L)).thenReturn(Optional.of(new CartItems()));

        var result = subject.findPartInCart
                (cartRepository.findById(1L).get(),
                bikePartService.findById(1L).get());

        assertTrue(result.isPresent());
    }

    @Test
    void test_findPartInCart_ReturnsNullWhenGivenInvalidParameters() {
        when(bikePartService.findById(2L)).thenReturn(Optional.of(new BikePart()));

        var result = subject.findPartInCart(
                cartRepository.findById(1L).get(),
                bikePartService.findById(2L).get()
        );

        assertTrue(result.isEmpty());
    }

    @Test
    void test_getCartItems_returnsAllItemsCorrectly() {
        when(cartItemsRepository.findAll()).thenReturn(List.of(new CartItems()));
        var result = subject.getCartItems();

        assertEquals(1, result.size());
    }

    @Test
    void test_removeById_removesEntityByGivenValidId() {
        subject.removeById(1L);
        var result = subject.getCartItems();
        assertEquals(0,result.size());
    }

    @Test
    void test_removeById_doesntRemoveEntityWhenGivenInvalidId() {
        when(cartItemsRepository.findAll()).thenReturn(List.of(new CartItems(new BikePart(),new Cart())));
        subject.removeById(100L);
        var result = subject.getCartItems();
        assertEquals(1,result.size());
    }

    @Test
    void test_findItemsByUser_returnsItemsCorrectlyWhenGivenValidUser() {
        var user = userRepository.findById(1L).get();
        when(cartItemsRepository.findAllByCart_UserAndCartStatus(user,"open")).thenReturn(List.of(new CartItems()));

        var result = subject.findItemsByUser(user);

        assertEquals(1,result.size());
    }

    @Test
    void test_findItemsByUser_returnsNothingWhenGivenUserWithEmptyCart() {
        User user = userRepository.findById(1L).get();
        user.setId(2L);
        user.setEmail("test@test.bg");
        user.setUsername("testingBro");
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        var result = subject.findItemsByUser(userRepository.findById(2L).get());
        assertEquals(0,result.size());
    }

    @Test
    void test_saveCartItems_savesCartItems() {
        var cartItems = cartItemsRepository.findById(1L).get();
        cartItems.setAmount(100);
        subject.saveCartItems(cartItems);

        assertEquals(100,cartItemsRepository.findById(1L).get().getAmount());
    }

    @Test
    void test_closeUserCart_closesUserCart() {
        var user = userRepository.findById(1L).get();
        var cart = new Cart();
        cart.setStatus("open");
        var cartItems = new CartItems();
        cart.setUser(user);
        cartItems.setCart(cart);
        cartRepository.save(cart);
        cartItemsRepository.save(cartItems);

        subject.closeUserCartItems(user);

        var result = subject.findItemsByUser(user);

        assertEquals(0,result.size());
    }
}