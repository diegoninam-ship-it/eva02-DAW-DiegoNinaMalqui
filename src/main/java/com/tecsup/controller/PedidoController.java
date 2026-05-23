package com.tecsup.controller;

import com.tecsup.model.*;
import com.tecsup.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<Pedido>> listar() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Pedido> registrar(@RequestBody Map<String, Object> payload) {

        Long clienteId = Long.valueOf(payload.get("clienteId").toString());

        List<Map<String, Object>> detallesRaw =
                (List<Map<String, Object>>) payload.get("detalles");

        List<DetallePedido> detalles = new java.util.ArrayList<>();

        for (Map<String, Object> item : detallesRaw) {
            DetallePedido detalle = new DetallePedido();

            Map<String, Object> productoMap = (Map<String, Object>) item.get("producto");
            Producto producto = new Producto();
            producto.setId(Long.valueOf(productoMap.get("id").toString()));

            detalle.setProducto(producto);
            detalle.setCantidad(Integer.valueOf(item.get("cantidad").toString()));
            detalles.add(detalle);
        }

        Pedido pedido = pedidoService.registrarPedido(clienteId, detalles);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
