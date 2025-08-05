///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.hdda.pojo;
//
//import jakarta.persistence.Basic;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import jakarta.persistence.Temporal;
//import jakarta.persistence.TemporalType;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.util.Date;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
///**
// *
// * @author mahai
// */
//@Entity
//@Table(name = "traffic_report")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class TrafficReport implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Basic(optional = false)
//    @Column(name = "id")
//    private Long id;
//    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "latitude")
//    private BigDecimal latitude;
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "longitude")
//    private BigDecimal longitude;
//    @Size(max = 255)
//    @Column(name = "description")
//    private String description;
//    @Size(max = 255)
//    @Column(name = "image_url")
//    private String imageUrl;
//    @Size(max = 9)
//    @Column(name = "status")
//    private String status;
//    @Column(name = "created_at")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date createdAt;
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    @ManyToOne
//    private User userId;
//
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (id != null ? id.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof TrafficReport)) {
//            return false;
//        }
//        TrafficReport other = (TrafficReport) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "com.hdda.pojo.TrafficReport[ id=" + id + " ]";
//    }
//
//}
