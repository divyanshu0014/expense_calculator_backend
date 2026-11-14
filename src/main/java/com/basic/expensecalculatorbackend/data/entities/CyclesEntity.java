package com.basic.expensecalculatorbackend.data.entities;

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
    private BigDecimal opening_carry_paise;

    @Column(name = "released_carry_paise", nullable = false)
    private BigDecimal released_carry_paise;

    @Column(name = "label")
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private SourceEnum source;

    @Column(name = "sheet_balance_Paise")
    private BigDecimal sheetBalancePaise;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    public CyclesEntity() {}

    public CyclesEntity(PlanEntity plan, LocalDate startDate, LocalDate endDate, BigDecimal takeHomePaise, BigDecimal extraCreditsPaise, BigDecimal opening_carry_paise, BigDecimal released_carry_paise, String label, SourceEnum source, BigDecimal sheetBalancePaise, LocalDateTime createdAt) {
        this.plan = plan;
        this.startDate = startDate;
        this.endDate = endDate;
        this.takeHomePaise = takeHomePaise;
        this.extraCreditsPaise = extraCreditsPaise;
        this.opening_carry_paise = opening_carry_paise;
        this.released_carry_paise = released_carry_paise;
        this.label = label;
        this.source = source;
        this.sheetBalancePaise = sheetBalancePaise;
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

    public BigDecimal getOpening_carry_paise() {
        return opening_carry_paise;
    }

    public void setOpening_carry_paise(BigDecimal opening_carry_paise) {
        this.opening_carry_paise = opening_carry_paise;
    }

    public BigDecimal getReleased_carry_paise() {
        return released_carry_paise;
    }

    public void setReleased_carry_paise(BigDecimal released_carry_paise) {
        this.released_carry_paise = released_carry_paise;
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
}
