package org.example.smartshop.service.payment;

import org.example.smartshop.mapper.PaymentMapper;
import org.example.smartshop.repository.OrderRepository;
import org.example.smartshop.repository.PaymentRepository;
import org.example.smartshop.service.client.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private PaymentMapper paymentMapper;
    @Mock
    private ClientService clientService;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
    }
}