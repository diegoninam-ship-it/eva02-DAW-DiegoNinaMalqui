package com.tecsup.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente")
@Data                    // Lombok genera getters, setters, toString, equals
@NoArgsConstructor       // Lombok genera constructor vacío (requerido por JPA)
@AllArgsConstructor      // Lombok genera constructor con todos los campos

public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT en MySQL
    private Long id;

    @NotBlank(message = "El nombre es obligatorio") // Validación: no puede estar vacío
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @Email(message = "Formato de correo inválido")   // Validación: debe tener formato email
    @Column(unique = true) // La evaluación pide que el correo sea único
    private String correo;

    @NotBlank(message = "El teléfono no debe estar vacío")
    private String telefono;

    private String direccion;

    // Un cliente puede tener MUCHOS pedidos → @OneToMany
    // mappedBy="cliente" indica que la FK está en la tabla pedido
    // CascadeType.ALL: si eliminas un cliente, se eliminan sus pedidos
    // orphanRemoval=true: si remueves un pedido de la lista, se borra de la BD
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pedido> pedidos = new ArrayList<>();
}
