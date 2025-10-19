package com.yatindevhub.ecommerce.entity.security;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "token")
public class RefreshToken {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String tokenHash;
    private String tokenLookupKey;
    private boolean revoked;
    private Instant expiryDate;
   @ManyToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserInfo userInfo;

}
