package adms.clinic.patient.entity

import adms.clinic.common.entity.VersionableEntityBase
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Column
import javax.persistence.Entity

@Entity
@DynamicUpdate
class Patient(

        @Column(nullable = false, length = 255)
        var firstName: String,

        @Column(nullable = false, length = 255)
        var lastName: String,

        @Column(nullable = false, length = 255)
        var address: String //TODO make this separate entity

) : VersionableEntityBase()