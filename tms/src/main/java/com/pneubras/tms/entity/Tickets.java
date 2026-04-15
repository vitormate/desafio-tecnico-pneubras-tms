package com.pneubras.tms.entity;

import com.pneubras.tms.utils.enums.PriorityEnum;
import com.pneubras.tms.utils.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tickets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String description;

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private PriorityEnum priority;

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "due_at", nullable = false)
    private LocalDateTime dueAt;

    public Tickets(String title, String description, PriorityEnum priority, int hours) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = StatusEnum.ABERTO;
        this.createdAt = LocalDateTime.now();
        this.dueAt = LocalDateTime.now().plusHours(hours);
    }

    public static int checkDueHour(PriorityEnum priority) {
        int dueHour = 0;
        if (priority == PriorityEnum.BAIXA) {
            dueHour = 72;
        } else if (priority == PriorityEnum.MEDIA) {
            dueHour = 48;
        } else if (priority == PriorityEnum.ALTA) {
            dueHour = 24;
        } else if (priority == PriorityEnum.CRITICA) {
            dueHour = 8;
        }

        return dueHour;
    }

    public void updateTicket(String title, String description) {
        if (title == null && description == null) {
            throw new RuntimeException("The update failed. No data found.");
        }
        if (title != null) {
            this.title = title;
        }
        if (description != null) {
            this.description = description;
        }
        this.updatedAt = LocalDateTime.now();
    }
}
