package com.ericsson.graduates.microservicemanager;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.engine.internal.Cascade;

import javax.persistence.*;
import java.util.*;

@Entity
@Embeddable
@Table(name = "MICROSERVICE")
public class Microservice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "engineer_id")
    private Engineer leadEngineer;
    private String description;
    private Date dateCreated;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name="version",
            joinColumns=@JoinColumn(name="microservice_id")
    )
    @Column(name="version_no")
    private List<String> versionNumbers;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private Microservice parent;
    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private Collection<Microservice> dependencies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Microservice() {
    }

    /**
     *
     * @param name
     * @param leadEngineer
     * @param description
     * @param dateCreated
     * @param versionNumbers
     */
    public Microservice(String name, Engineer leadEngineer, String description, Date dateCreated, List<String> versionNumbers) {
        this.name = name;
        this.leadEngineer = leadEngineer;
        this.description = description;
        this.dateCreated = dateCreated;
        this.versionNumbers = versionNumbers;
        this.dependencies = new ArrayList<Microservice>();
        this.parent = null;
    }


    public Microservice(String name, Engineer leadEngineer, String description, Date dateCreated, List<String> versionNumbers, String category) {
        this.name = name;
        this.leadEngineer = leadEngineer;
        this.description = description;
        this.dateCreated = dateCreated;
        this.versionNumbers = versionNumbers;
        this.dependencies = new ArrayList<Microservice>();
        this.parent = null;
        this.category = category;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Engineer getLeadEngineer() {
        return leadEngineer;
    }

    public void setLeadEngineer(Engineer leadEngineer) {
        this.leadEngineer = leadEngineer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<String> getVersionNumbers() {
        return versionNumbers;
    }

    public void setVersionNumbers(List<String> versionNumbers) {
        this.versionNumbers = versionNumbers;
    }

    public void addVersion(String version) {
        if (version == null)
            throw new IllegalArgumentException("Tried to pass null value to method");
        if (!getVersionNumbers().contains(version)) {
            getVersionNumbers().add(version);
        }
    }

    public Collection<Microservice> getDependencies() {
        return dependencies;
    }


    public void setDependencies(Collection<Microservice> dependencies) {
        this.dependencies = dependencies;
    }

    public void addDependency(Microservice microservice) {
        if (microservice == null)
            throw new IllegalArgumentException("Tried to pass null value to method");
        if (!getDependencies().contains(microservice)) {
            getDependencies().add(microservice);
            microservice.setParent(this);
        }
    }

    public Microservice getParent() {
        return parent;
    }

    public void setParent(Microservice parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Microservice.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("category='" + category + "'")
                .add("leadEngineer=" + leadEngineer)
                .add("description='" + description + "'")
                .add("dateCreated=" + dateCreated)
                .add("versionNumber='" + versionNumbers + "'")
                .toString();
    }
}
