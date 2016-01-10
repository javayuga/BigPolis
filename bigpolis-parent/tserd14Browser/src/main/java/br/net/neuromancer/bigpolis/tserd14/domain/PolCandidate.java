package br.net.neuromancer.bigpolis.tserd14.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PolCandidate.
 */
@Entity
@Table(name = "pol_candidate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "polcandidate")
public class PolCandidate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 5, max = 140)
    @Column(name = "full_name", length = 140)
    private String fullName;

    @Size(min = 2, max = 140)
    @Column(name = "alias", length = 140)
    private String alias;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "pol_candidate_candidacies",
               joinColumns = @JoinColumn(name="pol_candidates_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="candidaciess_id", referencedColumnName="ID"))
    private Set<PolCandidacy> candidaciess = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Set<PolCandidacy> getCandidaciess() {
        return candidaciess;
    }

    public void setCandidaciess(Set<PolCandidacy> polCandidacys) {
        this.candidaciess = polCandidacys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PolCandidate polCandidate = (PolCandidate) o;
        return Objects.equals(id, polCandidate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PolCandidate{" +
            "id=" + id +
            ", fullName='" + fullName + "'" +
            ", alias='" + alias + "'" +
            '}';
    }
}
