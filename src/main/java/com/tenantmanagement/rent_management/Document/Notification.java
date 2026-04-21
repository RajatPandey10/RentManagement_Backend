package com.tenantmanagement.rent_management.Document;

import com.tenantmanagement.rent_management.Enums.NotificationSentVia;
import com.tenantmanagement.rent_management.Enums.NotificationStatus;
import com.tenantmanagement.rent_management.Enums.NotificationType;
import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "notification")
public class Notification {

    @Id
    private String id;

    private String userId;

    private String message;

    private NotificationType type;

    private NotificationSentVia sentVia;
    private NotificationStatus status;
    private LocalDateTime sentAt;
}
