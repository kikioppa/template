package com.example.login.model;


import javax.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;



@Data
@Entity

@Table(name="Confirmation_Token")
public class ConfirmationToken {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="token_id")
    private int tokenid;

    @Column(name="confirmation_token")
    private String confirmationToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    
    public ConfirmationToken() {
    	
    }
    
    public ConfirmationToken(User user) {
    	tokenid = user.getId();
        this.user = user;
        createdDate = new Date();
        confirmationToken = UUID.randomUUID().toString();
    }

}