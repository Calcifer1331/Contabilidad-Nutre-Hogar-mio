package com.nutrehogar.sistemacontable.application.repository;

import com.nutrehogar.sistemacontable.exception.ReportException;
import com.nutrehogar.sistemacontable.exception.RepositoryException;

public interface BackupRepository extends SimpleRepository<String> {
    void backup(String fileName) throws ReportException;

    void restore(String filePath) throws RepositoryException;
}