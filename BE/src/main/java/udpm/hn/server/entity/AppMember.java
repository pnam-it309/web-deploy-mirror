package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import udpm.hn.server.entity.base.PrimaryEntity;

@Entity
@Table(name = "app_member")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppMember extends PrimaryEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id")
    private App app;

    // Thành viên có tài khoản trong hệ thống (Có thể Null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = true)
    private Customer customer;

    // Dành cho thành viên ngoài (Lưu cứng Email/Tên)
    @Column(name = "member_email")
    private String memberEmail;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "role")
    private String role; // LEADER, MEMBER
}