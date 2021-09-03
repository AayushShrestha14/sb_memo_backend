package com.sb.solutions.api.document.service;

import java.util.List;
import java.util.Map;

import com.sb.solutions.api.document.entity.Document;
import com.sb.solutions.core.service.BaseService;


public interface DocumentService extends BaseService<Document> {

    Map<Object, Object> documentStatusCount();

    List<Document> getByStatus(String statusName);
}
