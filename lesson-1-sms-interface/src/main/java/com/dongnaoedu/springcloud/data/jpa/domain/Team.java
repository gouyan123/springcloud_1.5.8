package com.dongnaoedu.springcloud.data.jpa.domain;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "t_team")
public class Team {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "teamName")
    private String teamName;
    public Team() {
    }
}
