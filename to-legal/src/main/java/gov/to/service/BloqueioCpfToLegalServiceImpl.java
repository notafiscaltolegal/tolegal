package gov.to.service;

import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

import gov.to.entidade.BloqueioCpfToLegal;
import gov.to.persistencia.ConsultasDaoJpa;

@Stateless
public class BloqueioCpfToLegalServiceImpl extends ConsultasDaoJpa<BloqueioCpfToLegal> implements BloqueioCpfToLegalService{

	@SuppressWarnings("unchecked")
	@Override
	public List<String> cpfsBloqueados() {
		
		Criteria criteria = getSession().createCriteria(BloqueioCpfToLegal.class);
		
		List<String> cpfs = (List<String>) criteria
				.setProjection(Projections.property("cpf"))
				.list();
		
		return cpfs;
	}
}