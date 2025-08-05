/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.pojo;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author mahai
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "route_variant")
public class RouteVariant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @PositiveOrZero(message = "Quãng đường phải là số không âm")
    @Column(name = "distance_km")
    private double distanceKm;

    @PositiveOrZero(message = "Thời gian phải là số không âm")
    @Column(name = "time_minute ")
    private int timeMinute;

    @Positive(message = "Thứ tự phải là số dương")
    @Column(name = "seq")
    private int seq;

    @ManyToOne(optional = false)
    @JoinColumn(name = "from_stop_id", nullable = false)
    @NotNull(message = "Điểm bắt đầu không được để trống")
    private Stop startStop;

    @ManyToOne(optional = false)
    @JoinColumn(name = "to_stop_id", nullable = false)
    @NotNull(message = "Điểm kết thúc không được để trống")
    private Stop endStop;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "variant", fetch = FetchType.LAZY)
    private List<Schedule> schedules;

    @ManyToOne(optional = false)
    @JoinColumn(name = "route_id", referencedColumnName = "id", nullable = false)
    @NotNull(message = "Tuyến không được để trống")
    private Route route;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RouteVariant)) {
            return false;
        }
        RouteVariant other = (RouteVariant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hdda.pojo.RouteVariant[ id=" + id + " ]";
    }

}
