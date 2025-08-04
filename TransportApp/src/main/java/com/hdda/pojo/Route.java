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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author mahai
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "route")
public class Route implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Mã tuyến không được để trống")
    @Size(min = 1, max = 20, message = "Mã tuyến phải từ 1 đến 20 ký tự")
    @Column(name = "code", nullable = false)
    private String code;

    @NotBlank(message = "Tên tuyến không được để trống")
    @Size(min = 1, max = 100, message = "Tên tuyến phải từ 1 đến 100 ký tự")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255, message = "Mô tả quá dài")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "route", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<RouteVariant> routeVariantSet;

    @OneToMany(mappedBy = "route", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Favorite> favoriteSet;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Route)) {
            return false;
        }
        Route other = (Route) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hdda.pojo.Route[ id=" + id + " ]";
    }

}
