/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author mahai
 */
@Entity
@Table(name = "search_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SearchLog.findAll", query = "SELECT s FROM SearchLog s"),
    @NamedQuery(name = "SearchLog.findById", query = "SELECT s FROM SearchLog s WHERE s.id = :id"),
    @NamedQuery(name = "SearchLog.findBySearchTime", query = "SELECT s FROM SearchLog s WHERE s.searchTime = :searchTime"),
    @NamedQuery(name = "SearchLog.findByDurationMs", query = "SELECT s FROM SearchLog s WHERE s.durationMs = :durationMs")})
public class SearchLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "search_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date searchTime;
    @Column(name = "duration_ms")
    private Integer durationMs;
    @JoinColumn(name = "result_variant", referencedColumnName = "id")
    @ManyToOne
    private RouteVariant resultVariant;
    @JoinColumn(name = "from_stop_id", referencedColumnName = "id")
    @ManyToOne
    private Stop fromStopId;
    @JoinColumn(name = "to_stop_id", referencedColumnName = "id")
    @ManyToOne
    private Stop toStopId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;

    public SearchLog() {
    }

    public SearchLog(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(Date searchTime) {
        this.searchTime = searchTime;
    }

    public Integer getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Integer durationMs) {
        this.durationMs = durationMs;
    }

    public RouteVariant getResultVariant() {
        return resultVariant;
    }

    public void setResultVariant(RouteVariant resultVariant) {
        this.resultVariant = resultVariant;
    }

    public Stop getFromStopId() {
        return fromStopId;
    }

    public void setFromStopId(Stop fromStopId) {
        this.fromStopId = fromStopId;
    }

    public Stop getToStopId() {
        return toStopId;
    }

    public void setToStopId(Stop toStopId) {
        this.toStopId = toStopId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
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
        if (!(object instanceof SearchLog)) {
            return false;
        }
        SearchLog other = (SearchLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.hdda.pojo.SearchLog[ id=" + id + " ]";
    }
    
}
