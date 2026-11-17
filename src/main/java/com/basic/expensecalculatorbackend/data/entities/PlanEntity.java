package com.basic.expensecalculatorbackend.data.entities;

import com.basic.expensecalculatorbackend.data.enums.CurrencyEnum;
import com.basic.expensecalculatorbackend.data.enums.MotionEnum;
import com.basic.expensecalculatorbackend.data.enums.PayFrequencyEnum;
import com.basic.expensecalculatorbackend.data.enums.ThemeEnum;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "plans")
public class PlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfileEntity profile;

    @Column(name = "salary_day", nullable = false)
    private Long salaryDay;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_frequency", nullable = false)
    private PayFrequencyEnum payFrequencyEnum;

    @Column(name = "take_home_paise", nullable = false)
    private BigDecimal takeHomePaise;

    @Column(name = "released_carry_paise", nullable = false)
    private BigDecimal releasedCarryPaise;

    @Column(name = "currency", nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyEnum currency;

    @Column(name = "theme", nullable = false)
    @Enumerated(EnumType.STRING)
    private ThemeEnum theme;

    @Column(name = "motion", nullable = false)
    @Enumerated(EnumType.STRING)
    private MotionEnum motion;

    @Column(name = "tour_seen", nullable = false)
    private Boolean tourSeen;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanAllocationEntity> allocations;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    public PlanEntity() {

    }

    public PlanEntity(Long salaryDay, PayFrequencyEnum payFrequencyEnum, BigDecimal takeHomePaise, BigDecimal releasedCarryPaise, CurrencyEnum currency, ThemeEnum theme, MotionEnum motion, Boolean tourSeen, Boolean isActive) {
        this.salaryDay = salaryDay;
        this.payFrequencyEnum = payFrequencyEnum;
        this.takeHomePaise = takeHomePaise;
        this.releasedCarryPaise = releasedCarryPaise;
        this.currency = currency;
        this.theme = theme;
        this.motion = motion;
        this.tourSeen = tourSeen;
        this.isActive = isActive;
    }

    @PrePersist
    public void setTimeStamps() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }


    public Long getId() {
        return id;
    }

    public UserProfileEntity getProfile() {
        return profile;
    }

    public void setProfile(UserProfileEntity profile) {
        this.profile = profile;
    }

    public Long getSalaryDay() {
        return salaryDay;
    }

    public void setSalaryDay(Long salaryDay) {
        this.salaryDay = salaryDay;
    }

    public BigDecimal getTakeHomePaise() {
        return takeHomePaise;
    }

    public void setTakeHomePaise(BigDecimal takeHomePaise) {
        this.takeHomePaise = takeHomePaise;
    }

    public BigDecimal getReleasedCarryPaise() {
        return releasedCarryPaise;
    }

    public void setReleasedCarryPaise(BigDecimal releasedCarryPaise) {
        this.releasedCarryPaise = releasedCarryPaise;
    }

    public CurrencyEnum getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyEnum currency) {
        this.currency = currency;
    }

    public ThemeEnum getTheme() {
        return theme;
    }

    public void setTheme(ThemeEnum theme) {
        this.theme = theme;
    }

    public MotionEnum getMotion() {
        return motion;
    }

    public void setMotion(MotionEnum motion) {
        this.motion = motion;
    }

    public Boolean getTourSeen() {
        return tourSeen;
    }

    public void setTourSeen(Boolean tourSeen) {
        this.tourSeen = tourSeen;
    }

    public PayFrequencyEnum getPayFrequencyEnum() {
        return payFrequencyEnum;
    }

    public void setPayFrequencyEnum(PayFrequencyEnum payFrequencyEnum) {
        this.payFrequencyEnum = payFrequencyEnum;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<PlanAllocationEntity> getAllocations() {
        return allocations;
    }

    public void setAllocations(List<PlanAllocationEntity> allocations) {
        this.allocations.clear();
        if (allocations != null) {
            allocations.forEach(this::addAllocation);
        }
    }

    public void addAllocation(PlanAllocationEntity allocation) {
        allocations.add(allocation);
        allocation.setPlan(this);
    }

    public void removeAllocation(PlanAllocationEntity allocation) {
        allocations.remove(allocation);
        allocation.setPlan(null);
    }
}
