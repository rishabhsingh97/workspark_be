package com.workspark.certificateservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a certificate template.
 * This class is used to store and manage certificate templates in the database.
 */
@Entity
@Table(name= "templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The title of the template.
     * This field stores the name of the template.
     */
    @Column(nullable = false)
    private String name;

    /**
     * The code of the template.
     * This field stores the unique code associated with the template to map with the particular category on many to many relationship.
     */
    @Column(nullable = false)
    private String templateCode;

    /**
     * The source template file from which certificate can be generated.
     * This field stores the file data of the template as a Blob object.
     */
    @Lob
    @Column(columnDefinition = "LONGBLOB", nullable = false)
    private Blob fileData;

    /**
     * The preview file of the template.
     * This field stores the preview file of the template as a Blob object.
     */
    @Lob
    @Column(columnDefinition = "LONGBLOB", nullable = false)
    private Blob previewFile;

    /**
     * List of dynamic fields associated with the template.
     * This field stores a list of strings representing the dynamic fields of the template.
     */
    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TemplateDynamicField> dynamicFields;

    /**
     * List of assets associated with the template.
     * This field stores a list of Asset objects representing the assets associated with the template.
     */
    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TemplateAsset> assets;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
