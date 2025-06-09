package io.devchw.springsse

import jakarta.persistence.*

/**
 * @author DevCHW
 * @since 2025-05-02
 */
@Entity
@Table(name = "notification")
class NotificationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "content")
    val content: String,

    @Column(name = "related_url")
    val relatedUrl: String,

    @Column(name = "is_read")
    var isRead: Boolean = false,

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    val notificationType: NotificationType,

    val userId: Long,
) {

    fun read() {
        this.isRead = true
    }

    enum class NotificationType {

    }
}