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
 * A PolParty.
 */
@Entity
@Table(name = "pol_party")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "polparty")
public class PolParty implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 140)
    @Column(name = "name", length = 140)
    private String name;

    @Size(min = 2)
    @Column(name = "acronym")
    private String acronym;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "pol_party_candidacies",
               joinColumns = @JoinColumn(name="pol_partys_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="candidaciess_id", referencedColumnName="ID"))
    private Set<PolCandidacy> candidaciess = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
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
        PolParty polParty = (PolParty) o;
        return Objects.equals(id, polParty.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PolParty{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", acronym='" + acronym + "'" +
            '}';
    }
}
