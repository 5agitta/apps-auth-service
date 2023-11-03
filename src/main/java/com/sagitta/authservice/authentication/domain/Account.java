package com.sagitta.authservice.authentication.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Setter
@Data
@Table(schema = "tax")
public class Account {
    @Id
    private String etin;
    private String email;
    private String phoneNumber;
    private String password;
    private boolean isActive;
    private Date lastLogin;
    private String accessToken;

}
