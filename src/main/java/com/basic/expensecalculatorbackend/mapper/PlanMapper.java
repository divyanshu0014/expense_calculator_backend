package com.basic.expensecalculatorbackend.mapper;

import com.basic.expensecalculatorbackend.data.entities.PlanEntity;
import com.basic.expensecalculatorbackend.data.entities.UserProfileEntity;
import com.basic.expensecalculatorbackend.data.enums.MotionEnum;
import com.basic.expensecalculatorbackend.data.model.Plan;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PlanMapper {

    public PlanEntity toPlanEntity(Plan plan, UserProfileEntity profileEntity, PlanEntity existingEntity) {
        if (existingEntity == null) {
            existingEntity = new PlanEntity();
        }
        existingEntity.setProfile(profileEntity);
        existingEntity.setSalaryDay(plan.getSalaryDay());
        existingEntity.setCurrency(plan.getCurrency());
        existingEntity.setTheme(plan.getTheme());
        existingEntity.setTakeHomePaise(plan.getTakeHomePaise());
        existingEntity.setPayFrequencyEnum(plan.getPayFrequency());
        existingEntity.setReleasedCarryPaise(plan.getReleasedCarryPaise() != null ? plan.getReleasedCarryPaise() : BigDecimal.ZERO);
        existingEntity.setMotion(plan.getMotion() != null ? plan.getMotion() : MotionEnum.STANDARD);
        existingEntity.setTourSeen(plan.getTourSeen() != null ? plan.getTourSeen() : false);
        existingEntity.setActive(plan.getIsActive() != null ? plan.getIsActive() : false);
        return existingEntity;
    }

}
