package me.rumenblajev.bikepartshop.services;

import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.models.dto.OrderCreateDTO;
import me.rumenblajev.bikepartshop.models.entity.CartItems;
import me.rumenblajev.bikepartshop.models.entity.Order;
import me.rumenblajev.bikepartshop.models.entity.User;
import me.rumenblajev.bikepartshop.models.view.OrderViewModel;
import me.rumenblajev.bikepartshop.repositories.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository  orderRepository;
    private final ModelMapper modelMapper;
    private final CartItemsService cartItemsService;
    private final CartService cartService;

    public List<OrderViewModel> findAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> modelMapper.map(order, OrderViewModel.class))
                .toList();
    }

    public void saveOrderToDataBase(final Order order) {
        if(order.getDate() == null) {
            order.setDate(LocalDate.now());
        }

        if(order.getTotalValue() == null) {
            order.setTotalValue(getTotalItemsValue(order.getItems()));
        }

        orderRepository.saveAndFlush(order);
    }

    private double getTotalItemsValue(final List<CartItems> items) {
        double value = 0.0;

        for (var cartItem : items) {
            value += cartItem.getPart().getPrice() * cartItem.getAmount();
        }

        return value;
    }

    public void createUserOrder(final OrderCreateDTO orderCreateDTO, final User user) {
        var order = modelMapper.map(orderCreateDTO, Order.class);
        order.setClient(user);
        order.setItems(cartItemsService.findItemsByUser(user));
        createOrder(order);
    }

    public void createOrder(final Order order) {
        saveOrderToDataBase(order);
        cartService.closeUserCart(order.getClient());
        cartItemsService.closeUserCartItems(order.getClient());
    }

    public void deleteAllOrdersForUser(final User user) {
        orderRepository.findAll().stream().filter(order ->
                order.getClient().getId().equals(user.getId())
        ).forEach(orderRepository::delete);
    }
}
