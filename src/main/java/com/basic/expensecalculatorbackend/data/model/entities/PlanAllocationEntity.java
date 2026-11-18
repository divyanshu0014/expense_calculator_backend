package com.basic.expensecalculatorbackend.data.model.entities;

import com.basic.expensecalculatorbackend.data.enums.AllocationKindEnum;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "plan_allocations")
public class PlanAllocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "plan_id")
    private PlanEntity plan;

    @Column(name = "name")
    private String name;

    @Column(name = "kind", nullable = false)
    @Enumerated(EnumType.STRING)
    private AllocationKindEnum kind;

    @Column(name = "amount_paise", nullable = false)
    private BigDecimal amountPaise;

    @Column(name = "due_day")
    private Integer dueDay;

    public PlanAllocationEntity() {

    }

    public PlanAllocationEntity(PlanEntity plan, String name, AllocationKindEnum allocationKindEnum, BigDecimal amountPaise, Integer dueDay) {
        this.plan = plan;
        this.name = name;
        this.kind = allocationKindEnum;
        this.amountPaise = amountPaise;
        this.dueDay = dueDay;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AllocationKindEnum getKind() {
        return kind;
    }

    public void setKind(AllocationKindEnum kind) {
        this.kind = kind;
    }

    public BigDecimal getAmountPaise() {
        return amountPaise;
    }

    public void setAmountPaise(BigDecimal amountPaise) {
        this.amountPaise = amountPaise;
    }

    public Integer getDueDay() {
        return dueDay;
    }

    public void setDueDay(Integer dueDay) {
        this.dueDay = dueDay;
    }
}
