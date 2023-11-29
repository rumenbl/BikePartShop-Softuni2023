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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Autowired
    private CartService cartService;
    @MockBean
    private CartRepository cartRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CartItemsRepository cartItemsRepository;
    @MockBean
    private BikePartService bikePartService;
    @BeforeEach
    void setUp() {
        var cartItems = new CartItems(new BikePart(), new Cart());
        when(cartItemsRepository.findById(1L)).thenReturn(Optional.of(cartItems));
        when(cartRepository.findById(1L)).thenReturn(Optional.of(new Cart()));
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(bikePartService.findById(1L)).thenReturn(Optional.of(new BikePart()));
//        var partCreateDTO = mockPartCreateDto("title1");
//        bikePartService.savePart(partCreateDTO);
//        Cart cart = new Cart();
//        cart.setUser(userRepository.findById(1L).get()); // admin user
//        cartRepository.save(cart);
//        CartItems cartItems = new CartItems();
//        cartItems.setCart(cart);
//        cartItems.setPart(bikePartService.findById(1L).get());
//        cartItems.setAmount(1);
//        cartItemsRepository.save(cartItems);
    }

    @Test
    void test_getOpenUserCart_returnsCartWhenGivenValidUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        var user = userRepository.findById(1L).get();
        when(cartRepository.findByUserAndStatus(user, "open")).thenReturn(Optional.of(new Cart()));

        var cart = cartService.getOpenUserCart(user);

        assertTrue(cart.isPresent());
    }

    @Test
    void test_getOpenUserCart_returnsNullWhenGivenInvalidUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        var user = userRepository.findById(1L).get();
        when(cartRepository.findByUserAndStatus(user, "open")).thenReturn(Optional.empty());

        Optional<Cart> cart = cartService.getOpenUserCart(user);

        assertTrue(cart.isEmpty());
    }

    @Test
    void test_getCartContent_returnsContentWhenGivenValidId() {
        var cartItems = new CartItems(new BikePart(), new Cart());
        when(cartRepository.findById(1L)).thenReturn(Optional.of(new Cart()));
        when(cartItemsRepository.findCartItemsByCartId(1L)).thenReturn(List.of(cartItems));

        assertFalse(cartService.getCartContent(1L).isEmpty());
    }

    @Test
    void test_save_doesNotThrowExceptionWhenGivenValidParameters() {
        var cartItems = cartItemsRepository.findById(1L).get();
        cartItems.setAmount(2);
        var cart = cartRepository.findById(1L).get();
        cartService.save(cart, cartItems);
        assertEquals(2, cartItemsRepository.findById(1L).get().getAmount());
    }

    @Test
    void test_closeUserCart_closesUserCartWhenGivenValidUser() {
        var user = userRepository.findById(1L).get();
        var cart = cartRepository.findById(1L);

        when(cartRepository.findByUserAndStatus(user,"open")).thenReturn(cart);
        cartService.closeUserCart(user);

        verify(cartRepository,times(1)).save(any());

        assertEquals("closed", cartRepository.findById(1L).get().getStatus());
    }

    @Test
    void test_addPartToCart_incrementsPartAmountWhenGivenExistingPart() {
        var user = userRepository.findById(1L).get();
        var cart = new Cart();
        cart.setId(3L);
        var cartItems = new CartItems();
        var part = new BikePart();
        part.setId(2L);
        cartItems.setPart(part);
        cartItems.setAmount(1);
        cart.setCartItems(Set.of(cartItems));
        when(cartRepository.findById(3L)).thenReturn(Optional.of(cart));
        when(bikePartService.findById(2L)).thenReturn(Optional.of(part));
        when(cartRepository.findByUserAndStatus(user, "open")).thenReturn(Optional.of(cart));
        when(cartItemsRepository.findByPartIdAndCartId(2L, 3L)).thenReturn(Optional.of(cartItems));

        cartService.addPartToCart(user, 2L);

        assertEquals(2, cartRepository.findById(3L).get().getCartItems().stream().toList().get(0).getAmount());
    }

    @Test
    void test_addPartToCart_createsAndSavesNewCartWhenGivenEmptyCart() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(bikePartService.findById(1L)).thenReturn(Optional.of(new BikePart()));
        var adminUser = userRepository.findById(1L).get();
        cartService.closeUserCart(adminUser);
        verify(cartRepository,times(1)).findByUserAndStatus(adminUser,"open");

        cartService.addPartToCart(adminUser, 1L);
        verify(cartRepository,times(1)).save(any());

        var cart = new Cart();
        cart.setCartItems(Set.of(new CartItems(), new CartItems()));
        when(cartService.getOpenUserCart(adminUser)).thenReturn(Optional.of(cart));

        assertEquals(2, cartService.getOpenUserCart(adminUser).get().getCartItems().size());
    }
}