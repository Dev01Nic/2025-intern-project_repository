package com.portal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "doc_processing_log")
public class DocProcessingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference to the document (could be any letter, manual, etc.)
    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Column(length = 100, nullable = false)
    private String task;   // e.g. Uploaded, Approved, Rejected, Signed-off

    @Column(columnDefinition = "TEXT")
    private String remarks;   // optional notes about the task

    @Column(nullable = false)
    private LocalDateTime timestamp;  // log creation timestamp

    public DocProcessingLog() {
    }

    public DocProcessingLog(Long id, Long documentId, String task, String remarks, LocalDateTime timestamp) {
        this.id = id;
        this.documentId = documentId;
        this.task = task;
        this.remarks = remarks;
        this.timestamp = timestamp;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDocumentId() { return documentId; }
    public void setDocumentId(Long documentId) { this.documentId = documentId; }

    public String getTask() { return task; }
    public void setTask(String task) { this.task = task; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
