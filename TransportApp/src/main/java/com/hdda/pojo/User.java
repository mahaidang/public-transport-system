/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author mahai
 */
@Setter
@Getter
@Entity
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(max = 50, message = "Tên đăng nhập tối đa 50 ký tự")
    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(max = 255, message = "Mật khẩu tối đa 255 ký tự")
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Email(message = "Email không hợp lệ")
    @Size(max = 100, message = "Email tối đa 100 ký tự")
    @Column(name = "email", length = 100)
    private String email;

    @Size(max = 255, message = "Đường dẫn avatar tối đa 255 ký tự")
    @Column(name = "avatar_url", length = 255)
    private String avatar;

//    @NotNull(message = "Quyền người dùng không được để trống")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Role role;



//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
//    private Set<Notification> notificationSet;

//    @OneToMany(mappedBy = "userId")
//    private Set<TrafficReport> trafficReportSet;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private Set<Favorite> favoriteSet;

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, String username, String passwordHash) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hdda.pojo.User[ id=" + id + " ]";
    }

}
