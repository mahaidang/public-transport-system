/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
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
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "stop")
public class Stop implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Mã điểm dừng không được để trống")
    @Size(min = 1, max = 20, message = "Mã phải từ 1 đến 20 ký tự")
    @Column(name = "code", nullable = false)
    private String code;

    @NotBlank(message = "Tên điểm dừng không được để trống")
    @Size(min = 1, max = 100, message = "Tên phải từ 1 đến 100 ký tự")
    @Column(name = "name", nullable = false)
    private String name;

    @DecimalMin(value = "-90.0", inclusive = true, message = "Vĩ độ phải >= -90")
    @DecimalMax(value = "90.0", inclusive = true, message = "Vĩ độ phải <= 90")
    @Column(name = "latitude", nullable = false)
    private double latitude;

    @DecimalMin(value = "-180.0", inclusive = true, message = "Kinh độ phải >= -180")
    @DecimalMax(value = "180.0", inclusive = true, message = "Kinh độ phải <= 180")
    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Size(max = 255, message = "Mô tả quá dài")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "startStop", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<RouteVariant> outboundVariants;

    @OneToMany(mappedBy = "endStop", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<RouteVariant> inboundVariants;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Stop)) {
            return false;
        }
        Stop other = (Stop) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hdda.pojo.Stop[ id=" + id + " ]";
    }

}
