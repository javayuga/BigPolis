package br.net.neuromancer.bigpolis.tserd14.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import br.net.neuromancer.bigpolis.tserd14.domain.enumeration.PolOffice;

import br.net.neuromancer.bigpolis.tserd14.domain.enumeration.GeoAdminRegion;

/**
 * A PolCandidacy.
 */
@Entity
@Table(name = "pol_candidacy")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "polcandidacy")
public class PolCandidacy implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "office", nullable = false)
    private PolOffice office;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = false)
    private GeoAdminRegion region;

    @NotNull
    @Column(name = "election_day", nullable = false)
    private LocalDate electionDay;

    @ManyToMany(mappedBy = "candidaciess")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PolCandidate> candidates = new HashSet<>();

    @ManyToMany(mappedBy = "candidaciess")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PolParty> partys = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PolOffice getOffice() {
        return office;
    }

    public void setOffice(PolOffice office) {
        this.office = office;
    }

    public GeoAdminRegion getRegion() {
        return region;
    }

    public void setRegion(GeoAdminRegion region) {
        this.region = region;
    }

    public LocalDate getElectionDay() {
        return electionDay;
    }

    public void setElectionDay(LocalDate electionDay) {
        this.electionDay = electionDay;
    }

    public Set<PolCandidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(Set<PolCandidate> polCandidates) {
        this.candidates = polCandidates;
    }

    public Set<PolParty> getPartys() {
        return partys;
    }

    public void setPartys(Set<PolParty> polPartys) {
        this.partys = polPartys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PolCandidacy polCandidacy = (PolCandidacy) o;
        return Objects.equals(id, polCandidacy.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PolCandidacy{" +
            "id=" + id +
            ", office='" + office + "'" +
            ", region='" + region + "'" +
            ", electionDay='" + electionDay + "'" +
            '}';
    }
}
