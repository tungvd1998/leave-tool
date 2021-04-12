package com.example.leave.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Data
@Table(name = "permissions")
public class Permission implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String api;

    @Override
    public int hashCode() {
        return Objects.hashCode(api);
    }
}
