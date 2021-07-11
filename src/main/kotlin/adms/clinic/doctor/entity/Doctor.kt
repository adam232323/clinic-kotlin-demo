package adms.clinic.doctor.entity

import adms.clinic.common.entity.VersionableEntityBase
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Column
import javax.persistence.Entity

@Entity
@DynamicUpdate
class Doctor(

        @Column(nullable = false, length = 255)
        var firstName: String,

        @Column(nullable = false, length = 255)
        var lastName: String,

        @Column(nullable = false, length = 255)
        var specialization: String //TODO make this dictionary entity

) : VersionableEntityBase()