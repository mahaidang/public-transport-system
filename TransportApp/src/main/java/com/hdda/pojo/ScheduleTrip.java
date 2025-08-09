/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
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
@Table(name = "schedule_trip")
public class ScheduleTrip implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Thời gian khởi hành không được để trống")
    @Column(name = "depart_time", nullable = false)
    private LocalDateTime departTime;

    @NotNull(message = "Thời gian đến không được để trống")
    @Column(name = "arrive_time", nullable = false)
    private LocalDateTime arriveTime;

    @NotNull(message = "Lịch trình không được để trống")
    @ManyToOne(optional = false)
    @JoinColumn(name = "schedule_id", referencedColumnName = "id", nullable = false)
    private Schedule schedule;

//    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
//    @ManyToOne
//    private Vehicle vehicle;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ScheduleTrip)) {
            return false;
        }
        ScheduleTrip other = (ScheduleTrip) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hdda.pojo.ScheduleTrip[ id=" + id + " ]";
    }

}
