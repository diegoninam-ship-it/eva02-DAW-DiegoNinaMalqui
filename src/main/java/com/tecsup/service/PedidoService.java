package com.tecsup.service;

import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.*;
import com.tecsup.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));
    }

    @Transactional
    public Pedido registrarPedido(Long clienteId, List<DetallePedido> detalles) {

        if (detalles == null || detalles.isEmpty()) {
            throw new RuntimeException("El pedido debe contener al menos un producto");
        }

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + clienteId));

        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDate.now());
        pedido.setCliente(cliente);

        double total = 0.0;

        for (DetallePedido detalle : detalles) {

            Producto producto = productoRepository.findById(detalle.getProducto().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Producto no encontrado con id: " + detalle.getProducto().getId()));

            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException(
                        "Stock insuficiente para el producto: " + producto.getNombre() +
                                ". Stock disponible: " + producto.getStock());
            }

            double subtotal = detalle.getCantidad() * producto.getPrecio();
            detalle.setSubtotal(subtotal);

            detalle.setProducto(producto);
            detalle.setPedido(pedido); // Necesario para que JPA sepa a qué pedido pertenece

            total += subtotal;

            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);
        }

        pedido.setTotal(total);
        pedido.setDetalles(detalles);

        return pedidoRepository.save(pedido);
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        pedidoRepository.deleteById(id);
    }
}
