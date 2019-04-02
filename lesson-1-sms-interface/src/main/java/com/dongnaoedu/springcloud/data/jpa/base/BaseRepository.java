package com.dongnaoedu.springcloud.data.jpa.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean   /**@NoRepositoryBean注释的 接口，不会被作为Repository，不会为其创建代理类*/
public interface BaseRepository<T,PK extends Serializable> extends JpaRepository<T,PK> {
}
