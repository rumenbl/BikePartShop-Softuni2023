package me.rumenblajev.bikepartshop.services;

import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.models.entity.CartItems;
import me.rumenblajev.bikepartshop.models.entity.Order;
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
    public List<OrderViewModel> findAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> modelMapper.map(order, OrderViewModel.class))
                .toList();
    }

    public Order save(Order order) {
        if(order.getDate() == null) {
            order.setDate(LocalDate.now());
        }
        if(order.getTotalValue() == null) {
            Double value = 0.0;
            for (CartItems item : order.getItems()) {
                value += item.getPart().getPrice() * item.getAmount();
            }
            order.setTotalValue(value);
        }
        return orderRepository.saveAndFlush(order);
    }
}
