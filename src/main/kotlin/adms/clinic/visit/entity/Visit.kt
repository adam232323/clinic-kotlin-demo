package adms.clinic.visit.entity

import adms.clinic.common.entity.VersionableEntityBase
import adms.clinic.doctor.entity.Doctor
import adms.clinic.patient.entity.Patient
import org.hibernate.annotations.DynamicUpdate
import java.time.Instant
import javax.persistence.*

@Entity
@NamedEntityGraph(
        name = GRAPH_WITH_DOCTOR_AND_PATIENT,
        attributeNodes = [NamedAttributeNode("doctor"), NamedAttributeNode("patient")]
)
@DynamicUpdate
class Visit(

        @Column(nullable = false)
        var visitTime: Instant,

        @Column(nullable = false, length = 255)
        var clinic: String, //TODO  make this separate entity

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "id_patient", nullable = false)
        var patient: Patient,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "id_doctor", nullable = false)
        var doctor: Doctor

) : VersionableEntityBase()