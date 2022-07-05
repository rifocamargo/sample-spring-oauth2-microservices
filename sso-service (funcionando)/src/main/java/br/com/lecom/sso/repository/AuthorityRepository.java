package br.com.lecom.sso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lecom.sso.domain.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, String> {

}
