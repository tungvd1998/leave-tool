package com.example.leave.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Table(name = "roles")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (!(o instanceof Role)) {
//            return false;
//        }
//        return Objects.equals(name, ((Role) o).name);
//    }
//
    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
