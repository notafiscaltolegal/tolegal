package gov.goias.entidades;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Repository;

import gov.goias.dtos.DTOCnaeCadastro;
import gov.goias.persistencia.CnaeAutorizadoRepository;

/**
 * Created by thiago-mb on 18/07/2014.
 */
@Entity
@Table(name = "NFG_CNAE_AUTORIZADO")
@Repository
@Cacheable(true)
public class CnaeAutorizado extends CnaeAutorizadoRepository implements Serializable {
    private static final long serialVersionUID = 958538797256100278L;
    @Id
    @Column(name = "ID_CNAE_AUTORIZADO")
    @GeneratedValue(generator = "triggerAssigned")
    @GenericGenerator(name = "triggerAssigned", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    private Integer idCnaeAutorizado;

    @Column(name = "DATA_INCLUSAO_CNAE")
    private Date dataInclusaoCnae;

    @ManyToOne
    @JoinColumn(name = "ID_SUBCLASSE_CNAEF")
    @Fetch(value = FetchMode.JOIN)
    private CCESubClasseCnae subClasseCnae;

    @Column(name = "DATA_EXCLUSAO_CNAE")
    private Date dataExclusaoCnae;

    @Column(name = "DATA_OBRIGAT_NFG")
    private Date dataObrigatoriedade;

    public CnaeAutorizado() {
        this.subClasseCnae = new CCESubClasseCnae();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getIdCnaeAutorizado() {
        return idCnaeAutorizado;
    }

    public void setIdCnaeAutorizado(Integer idCnaeAutorizado) { this.idCnaeAutorizado = idCnaeAutorizado; }

    public Date getDataInclusaoCnae() {
        return dataInclusaoCnae;
    }

    public void setDataInclusaoCnae(Date dataInclusaoCnae) { this.dataInclusaoCnae = dataInclusaoCnae; }

    public CCESubClasseCnae getSubClasseCnae() {
        return subClasseCnae;
    }

    public void setSubClasseCnae(CCESubClasseCnae subClasseCnae) {
        this.subClasseCnae = subClasseCnae;
    }

    public void setIdSubClasseCnae(Long idSubClasseCnae) {
        this.subClasseCnae.setIdSubClasseCnae(idSubClasseCnae);
    }

    public Date getDataExclusaoCnae() { return dataExclusaoCnae; }

    public void setDataExclusaoCnae(Date dataExclusaoCnae) { this.dataExclusaoCnae = dataExclusaoCnae; }

    public Date getDataObrigatoriedade() {
        return dataObrigatoriedade;
    }

    public void setDataObrigatoriedade(Date dataObrigatoriedade) {
        this.dataObrigatoriedade = dataObrigatoriedade;
    }

    //metodo para exibir data formatada
    public String getDataInclusaoCnaeString() throws ParseException {
        Date dataInclTemp = dataInclusaoCnae;
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
        String date = DATE_FORMAT.format(dataInclTemp);
        return date;
    }

    public String getDataObrigatoriedadeFormatada(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(dataObrigatoriedade);
    }

    public void inserir(DTOCnaeCadastro dtoCnaeCadastro) {

        if(dtoCnaeCadastro.deveInserirTodosDaDivisao()){
            createFromDivisao(Integer.valueOf(dtoCnaeCadastro.getDivisao()), dtoCnaeCadastro.getDataObrigatoriedade());
        } else if(dtoCnaeCadastro.deveInserirTodosDoGruo()){
            createFromGrupo(Integer.valueOf(dtoCnaeCadastro.getGrupo()), dtoCnaeCadastro.getDataObrigatoriedade());
        } else if(dtoCnaeCadastro.deveInserirTodosDaClasse()){
            createFromClasse(Integer.valueOf(dtoCnaeCadastro.getClasse()), dtoCnaeCadastro.getDataObrigatoriedade());
        } else{
            insertCnaeAutorizado(Long.valueOf(dtoCnaeCadastro.getSubclasse()), dtoCnaeCadastro.getDataObrigatoriedade());
//            CnaeAutorizado cnaeAutorizado = new CnaeAutorizado();
//            cnaeAutorizado.setDataInclusaoCnae(new Date());
//            cnaeAutorizado.setDataObrigatoriedade(dtoCnaeCadastro.getDataObrigatoriedade());
//            cnaeAutorizado.setIdSubClasseCnae(Long.valueOf(dtoCnaeCadastro.getSubclasse()));
//            cnaeAutorizado.save();
        }
    }
}