package com.hms.user.entity;

import com.hms.user.dto.Roles;
import com.hms.user.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name="role")
    private Roles role;

    private Long profileId;
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
    @Column(updatable = false)
    private LocalDateTime updatedAt;

    public UserDTO toDTO(){
        return new UserDTO(this.id, this.name, this.email, this.password, this.role, this.profileId,
                this.createdAt, this.updatedAt);
    }

    @PrePersist
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected  void preUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
