package com.dongnaoedu.springcloud;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
// spring  data rest 技术
@RepositoryRestResource(collectionResourceRel = "sms", path = "sms")
public interface SmsController extends PagingAndSortingRepository<SmsDomain, Long> {
}