package com.plantcare.booking.entity;

import com.plantcare.common.exception.InvalidStatusTransitionException;

import java.util.EnumSet;
import java.util.Set;

public enum BookingStatus {
    CREATED,
    PENDING_PAYMENT,
    PAYMENT_COMPLETED,
    CONFIRMED,
    SITE_INSPECTION_REQUIRED,
    SITE_INSPECTION_COMPLETED,
    WORKER_ASSIGNED,
    INSTALLATION_SCHEDULED,
    INSTALLATION_STARTED,
    INSTALLATION_COMPLETED,
    MAINTENANCE_ACTIVE,
    COMPLETED,
    CANCELLED;

    public void validateTransitionTo(BookingStatus nextStatus) {
        if (this == nextStatus) return;
        if (this == CANCELLED || this == COMPLETED) {
            throw new InvalidStatusTransitionException("Cannot transition booking from terminal state " + this + " to " + nextStatus);
        }

        Set<BookingStatus> allowedNextStates;
        switch (this) {
            case CREATED:
                allowedNextStates = EnumSet.of(PENDING_PAYMENT, CANCELLED);
                break;
            case PENDING_PAYMENT:
                allowedNextStates = EnumSet.of(PAYMENT_COMPLETED, CANCELLED);
                break;
            case PAYMENT_COMPLETED:
                allowedNextStates = EnumSet.of(CONFIRMED, SITE_INSPECTION_REQUIRED, CANCELLED);
                break;
            case CONFIRMED:
            case SITE_INSPECTION_REQUIRED:
                allowedNextStates = EnumSet.of(SITE_INSPECTION_COMPLETED, WORKER_ASSIGNED, CANCELLED);
                break;
            case SITE_INSPECTION_COMPLETED:
                allowedNextStates = EnumSet.of(WORKER_ASSIGNED, CANCELLED);
                break;
            case WORKER_ASSIGNED:
                allowedNextStates = EnumSet.of(INSTALLATION_SCHEDULED, CANCELLED);
                break;
            case INSTALLATION_SCHEDULED:
                allowedNextStates = EnumSet.of(INSTALLATION_STARTED, CANCELLED);
                break;
            case INSTALLATION_STARTED:
                allowedNextStates = EnumSet.of(INSTALLATION_COMPLETED, CANCELLED);
                break;
            case INSTALLATION_COMPLETED:
                allowedNextStates = EnumSet.of(MAINTENANCE_ACTIVE, COMPLETED, CANCELLED);
                break;
            case MAINTENANCE_ACTIVE:
                allowedNextStates = EnumSet.of(COMPLETED, CANCELLED);
                break;
            default:
                allowedNextStates = EnumSet.noneOf(BookingStatus.class);
        }

        if (!allowedNextStates.contains(nextStatus)) {
            throw new InvalidStatusTransitionException(
                    String.format("Invalid booking state transition from %s to %s", this, nextStatus)
            );
        }
    }
}
