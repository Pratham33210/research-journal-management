-- Research Journal Management System Database Schema
-- MySQL 8.0+

-- Create Users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    role ENUM('AUTHOR', 'EDITOR', 'REVIEWER', 'ADMIN') NOT NULL,
    affiliation VARCHAR(255) NOT NULL,
    bio TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_is_active (is_active)
);

-- Create Papers table
CREATE TABLE papers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    abstract_text LONGTEXT,
    content LONGTEXT,
    author_id BIGINT NOT NULL,
    status ENUM('SUBMITTED', 'UNDER_REVIEW', 'REVISION_REQUESTED', 'ACCEPTED', 'REJECTED', 'PUBLISHED', 'ARCHIVED') DEFAULT 'SUBMITTED',
    plagiarism_score DECIMAL(5, 2),
    plagiarism_checked BOOLEAN DEFAULT false,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    accepted_at TIMESTAMP NULL,
    rejected_at TIMESTAMP NULL,
    published_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_author_id (author_id),
    INDEX idx_status (status),
    INDEX idx_submitted_at (submitted_at)
);

-- Create Reviews table
CREATE TABLE reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paper_id BIGINT NOT NULL,
    reviewer_id BIGINT NOT NULL,
    status ENUM('PENDING', 'SUBMITTED', 'ACCEPTED', 'DECLINED') DEFAULT 'PENDING',
    comments LONGTEXT,
    overall_rating INT CHECK (overall_rating >= 1 AND overall_rating <= 10),
    technical_quality_rating INT CHECK (technical_quality_rating >= 1 AND technical_quality_rating <= 10),
    clarity_rating INT CHECK (clarity_rating >= 1 AND clarity_rating <= 10),
    originality_rating INT CHECK (originality_rating >= 1 AND originality_rating <= 10),
    significance_rating INT CHECK (significance_rating >= 1 AND significance_rating <= 10),
    submitted_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (paper_id) REFERENCES papers(id) ON DELETE CASCADE,
    FOREIGN KEY (reviewer_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_paper_id (paper_id),
    INDEX idx_reviewer_id (reviewer_id),
    INDEX idx_status (status),
    UNIQUE KEY unique_review (paper_id, reviewer_id)
);

-- Create Revisions table
CREATE TABLE revisions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paper_id BIGINT NOT NULL,
    revision_number INT NOT NULL,
    content LONGTEXT,
    changes_summary LONGTEXT,
    submitted_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (paper_id) REFERENCES papers(id) ON DELETE CASCADE,
    INDEX idx_paper_id (paper_id),
    INDEX idx_revision_number (paper_id, revision_number)
);

-- Create database if not exists (run this first)
-- CREATE DATABASE IF NOT EXISTS research_journal_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE research_journal_db;
