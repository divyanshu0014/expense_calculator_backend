package com.basic.expensecalculatorbackend.data.model.entities;

import com.basic.expensecalculatorbackend.data.enums.SourceEnum;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cycles")
public class CyclesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private PlanEntity plan;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "take_home_paise", nullable = false)
    private BigDecimal takeHomePaise;

    @Column(name = "extra_credits_paise", nullable = false)
    private BigDecimal extraCreditsPaise;

    @Column(name = "opening_carry_paise", nullable = false)
    private BigDecimal openingCarryPaise;

    @Column(name = "released_carry_paise", nullable = false)
    private BigDecimal releasedCarryPaise;

    @Column(name = "label")
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private SourceEnum source;

    @Column(name = "sheet_balance_Paise")
    private BigDecimal sheetBalancePaise;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    public CyclesEntity() {}

    public CyclesEntity(PlanEntity plan, LocalDate startDate, LocalDate endDate, BigDecimal takeHomePaise, BigDecimal extraCreditsPaise, BigDecimal openingCarryPaise, BigDecimal releasedCarryPaise, String label, SourceEnum source, BigDecimal sheetBalancePaise,Boolean isActive, LocalDateTime createdAt) {
        this.plan = plan;
        this.startDate = startDate;
        this.endDate = endDate;
        this.takeHomePaise = takeHomePaise;
        this.extraCreditsPaise = extraCreditsPaise;
        this.openingCarryPaise = openingCarryPaise;
        this.releasedCarryPaise = releasedCarryPaise;
        this.label = label;
        this.source = source;
        this.sheetBalancePaise = sheetBalancePaise;
        this.isActive = isActive!=null ? isActive : Boolean.FALSE;
        this.createdAt = createdAt;
    }

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public PlanEntity getPlan() {
        return plan;
    }

    public void setPlan(PlanEntity plan) {
        this.plan = plan;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getTakeHomePaise() {
        return takeHomePaise;
    }

    public void setTakeHomePaise(BigDecimal takeHomePaise) {
        this.takeHomePaise = takeHomePaise;
    }

    public BigDecimal getExtraCreditsPaise() {
        return extraCreditsPaise;
    }

    public void setExtraCreditsPaise(BigDecimal extraCreditsPaise) {
        this.extraCreditsPaise = extraCreditsPaise;
    }

    public BigDecimal getOpeningCarryPaise() {
        return openingCarryPaise;
    }

    public void setOpeningCarryPaise(BigDecimal openingCarryPaise) {
        this.openingCarryPaise = openingCarryPaise;
    }

    public BigDecimal getReleasedCarryPaise() {
        return releasedCarryPaise;
    }

    public void setReleasedCarryPaise(BigDecimal releasedCarryPaise) {
        this.releasedCarryPaise = releasedCarryPaise;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public SourceEnum getSource() {
        return source;
    }

    public void setSource(SourceEnum source) {
        this.source = source;
    }

    public BigDecimal getSheetBalancePaise() {
        return sheetBalancePaise;
    }

    public void setSheetBalancePaise(BigDecimal sheetBalancePaise) {
        this.sheetBalancePaise = sheetBalancePaise;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
