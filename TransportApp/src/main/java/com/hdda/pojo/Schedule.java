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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
@Table(name = "schedule")
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 100, message = "Nhóm ngày tối đa 100 ký tự")
    @Column(name = "day_group")
    private String dayGroup;

    @NotNull(message = "Ngày bắt đầu có hiệu lực không được để trống")
    @Temporal(TemporalType.DATE)
    @Column(name = "effective_from", nullable = false)
    private Date effectiveFrom;

    @Temporal(TemporalType.DATE)
    @Column(name = "effective_to")
    private Date effectiveTo;

    @Size(max = 255, message = "Ghi chú quá dài")
    @Column(name = "note", length = 255)
    private String note;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "schedule", fetch = FetchType.LAZY)
    private List<ScheduleTrip> scheduleTrips;

    @NotNull(message = "Chặng không được để trống")
    @ManyToOne(optional = false)
    @JoinColumn(name = "variant_id", referencedColumnName = "id", nullable = false)
    private RouteVariant variant;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Schedule)) {
            return false;
        }
        Schedule other = (Schedule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hdda.pojo.Schedule[ id=" + id + " ]";
    }

}
