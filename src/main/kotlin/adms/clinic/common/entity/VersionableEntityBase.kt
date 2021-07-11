package adms.clinic.common.entity

import adms.clinic.common.contract.Identifiable
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import javax.persistence.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class VersionableEntityBase(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override var id: Long = 0,

        @Version
        var version: Int = 1,

        @CreatedDate
        var createdAt: Instant = Instant.MIN,

        @LastModifiedDate
        var editedAt: Instant = Instant.MIN
) : Identifiable {
    override fun toString(): String {
        return this::class.simpleName + "(id=$id, version=$version, createdAt=$createdAt, editedAt=$editedAt)"
    }
}