package gov.goias.dtos;

/**
 * @author henrique-rh
 * @since 19/06/15.
 */
public class DTOCnaeAutorizado {
    private Long id;
    private Boolean isCnaeAutorizado;
    private Boolean isCnaeObrigatorio;
    private Long idSubClasseCnae;

    public Boolean getIsCnaeAutorizado() {
        return isCnaeAutorizado;
    }

    public void setIsCnaeAutorizado(Boolean isCnaeAutorizado) {
        this.isCnaeAutorizado = isCnaeAutorizado;
    }

    public Boolean getIsCnaeObrigatorio() {
        return isCnaeObrigatorio;
    }

    public void setIsCnaeObrigatorio(Boolean isCnaeObrigatorio) {
        this.isCnaeObrigatorio = isCnaeObrigatorio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdSubClasseCnae() {
        return idSubClasseCnae;
    }

    public void setIdSubClasseCnae(Long idSubClasseCnae) {
        this.idSubClasseCnae = idSubClasseCnae;
    }
}
