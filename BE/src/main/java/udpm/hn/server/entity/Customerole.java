package udpm.hn.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import udpm.hn.server.entity.base.PrimaryEntity;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Setter
@Entity
@Table(name = "customer_role")
public class Customerole extends PrimaryEntity implements Serializable {
    @ManyToOne
    @JoinColumn(name = "id_ustomer")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;
}
