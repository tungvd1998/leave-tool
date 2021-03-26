package com.example.leave.infrastructure.security;

import com.example.leave.models.Role;
import com.example.leave.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class JwtUserDetails implements UserDetails {
    private Integer id;
    private String username;
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public JwtUserDetails(String username, String password, Integer id, Collection<? extends GrantedAuthority> authorities) {
//        super(username, password, authorities);
        this.username = username;
        this.password = password;
        this.id = id;
        this.authorities = authorities;
    }

    public JwtUserDetails(Integer id, String username, String password, List<SimpleGrantedAuthority> authorities) {
    }

    public static JwtUserDetails create(User user){
        Set<Role> roles = user.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new JwtUserDetails(user.getId(), user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return (Collection<GrantedAuthority>) authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtUserDetails that = (JwtUserDetails) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
