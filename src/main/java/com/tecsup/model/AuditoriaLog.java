package com.tecsup.model;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Entity
@Table(name = "auditoria_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accion;   // Ej: "CREATE_PEDIDO"
    private String metodo;   // Ej: "PedidoService.registrarPedido"
    private Timestamp fecha;
    private String usuario;  // Usuario autenticado en ese momento
}
