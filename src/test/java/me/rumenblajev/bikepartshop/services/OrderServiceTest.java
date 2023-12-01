package me.rumenblajev.bikepartshop.services;

import me.rumenblajev.bikepartshop.enums.GenderEnum;
import me.rumenblajev.bikepartshop.enums.RolesEnum;
import me.rumenblajev.bikepartshop.models.dto.OrderCreateDTO;
import me.rumenblajev.bikepartshop.models.entity.*;
import me.rumenblajev.bikepartshop.models.view.OrderViewModel;
import me.rumenblajev.bikepartshop.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Autowired
    private OrderService subject;
    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private CartService cartService;

    @MockBean
    private CartItemsService cartItemsService;

    @Test
    void test_findAllOrders_returnsOrdersCorrectly() {
        var order = mockOrder();

        when(orderRepository.findAll()).thenReturn(List.of(order));

        var orderViewModel = new OrderViewModel();
        orderViewModel.setTotalValue(100.69);
        orderViewModel.setDate(order.getDate());

        var expected = List.of(orderViewModel);
        var result = subject.findAllOrders();

        assertEquals(expected.size(), result.size());
        assertEquals(expected.get(0).getTotalValue(), result.get(0).getTotalValue());
        assertEquals(expected.get(0).getDate(), result.get(0).getDate());
    }

    @Test
    void test_saveOrderToDataBase_savesOrderToDataBase() {
        var order = mockOrder();

        when(orderRepository.saveAndFlush(order)).thenReturn(order);

        subject.saveOrderToDataBase(order);
        verify(orderRepository, times(1)).saveAndFlush(order);

        when(orderRepository.findAll()).thenReturn(List.of(order));

        assertEquals(1, subject.findAllOrders().size());
    }

    @Test
    void test_saveOrderToDataBase_addsTimeAndValue() {
        var order = mockOrder();
        var cartItems = order.getItems().get(0);
        var part = cartItems.getPart();
        order.setDate(null);
        order.setTotalValue(null);

        cartItems.setAmount(1);
        part.setPrice(10.0);

        when(orderRepository.saveAndFlush(order)).thenReturn(order);

        subject.saveOrderToDataBase(order);
        verify(orderRepository, times(1)).saveAndFlush(order);

        when(orderRepository.findAll()).thenReturn(List.of(order));

        assertEquals(1, subject.findAllOrders().size());
    }

    @Test
    void test_createOrder_createsOrderAndClosesCartAndCartItems() {
        var user = new User();
        var cart = new Cart();
        var order = mockOrder();

        user.setId(1L);
        cart.setId(1L);
        order.setId(1L);

        user.setCart(Collections.singleton(cart));
        cart.setUser(user);
        order.setClient(user);
        order.getItems().get(0).setCart(cart);

        subject.createOrder(order);

        verify(cartService, times(1)).closeUserCart(any());
        verify(cartItemsService, times(1)).closeUserCartItems(any());
        verify(orderRepository, times(1)).saveAndFlush(order);

        when(orderRepository.findAll()).thenReturn(List.of(order));

        assertEquals(1, orderRepository.findAll().size());
    }

    @Test
    void test_deleteAllOrdersForUser_deletesUserOrders() {
        var user = new User();
        user.setId(1L);

        var order1 = mockOrder();
        var order2 = mockOrder();

        order1.setClient(user);
        order2.setClient(user);

        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));
        subject.deleteAllOrdersForUser(user);

        verify(orderRepository, times(2)).delete(any());
        Mockito.reset(orderRepository);

        assertEquals(0, orderRepository.findAll().size());
    }
    @Test
    void test_createUserOrder_createsOrder() {
        var orderCreateDTO = new OrderCreateDTO();
        var user = mockUser();
        var expected = modelMapper.map(orderCreateDTO, Order.class);
        var bikePart = new BikePart();
        bikePart.setPrice(10.0);

        when(cartItemsService.findItemsByUser(any())).thenReturn(List.of(new CartItems(bikePart, new Cart())));

        subject.createUserOrder(orderCreateDTO, user);

        verify(cartService, times(1)).closeUserCart(any());
        verify(cartItemsService, times(1)).closeUserCartItems(any());
        verify(orderRepository, times(1)).saveAndFlush(any());

        expected.setClient(user);
        expected.setItems(cartItemsService.findItemsByUser(user));

        when(orderRepository.findAll()).thenReturn(List.of(expected));

        assertEquals(1, orderRepository.findAll().size());
    }
    private Order mockOrder() {
        var order = new Order();
        var date = LocalDate.now();
        order.setTotalValue(100.69);
        order.setDate(date);
        order.setItems(List.of(new CartItems(new BikePart(), new Cart())));
        return order;
    }

    private User mockUser() {
        var user = new User();
        var role = new Role();

        role.setName(RolesEnum.USER);

        user.setAge(1);
        user.setCart(new ArrayList<>());
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setGender(GenderEnum.MALE);
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setPhoneNumber("6625550144");
        user.setRole(role);
        user.setUsername("janedoe");

        return user;
    }

}