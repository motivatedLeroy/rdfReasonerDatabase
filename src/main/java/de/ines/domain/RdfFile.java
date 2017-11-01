package de.ines.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class RdfFile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    public Long id;

    public String fileName;

    public String tags;

    public double rating;

    public String dateOfUpload;

    public long numberOfEntries;

    public String provider;

    public String type;

    public RdfFile(){

    }

    public RdfFile(String fileName, String tags, double rating, String dateOfUpload, long numberOfEntries, String provider, String type) {
        this.fileName = fileName;
        this.tags = tags;
        this.rating = rating;
        this.dateOfUpload = dateOfUpload;
        this.numberOfEntries = numberOfEntries;
        this.provider = provider;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDateOfUpload() {
        return dateOfUpload;
    }

    public void setDateOfUpload(String dateOfUpload) {
        this.dateOfUpload = dateOfUpload;
    }

    public long getNumberOfEntries() {
        return numberOfEntries;
    }

    public void setNumberOfEntries(long numberOfEntries) {
        this.numberOfEntries = numberOfEntries;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
