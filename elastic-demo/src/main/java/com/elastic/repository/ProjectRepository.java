package com.elastic.repository;

import com.elastic.entity.Project;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends ElasticsearchRepository<Project, Integer> {
}
