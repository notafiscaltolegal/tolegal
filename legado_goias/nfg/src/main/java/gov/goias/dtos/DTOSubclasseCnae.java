package gov.goias.dtos;

/**
 * @author henrique-rh
 * @since 20/03/15.
 */
public class DTOSubclasseCnae {

    private Long id;
    private String descricao;

    public DTOSubclasseCnae(Long idSubClasseCnae) {
        this.id = idSubClasseCnae;
    }

    public DTOSubclasseCnae() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DTOSubclasseCnae)) return false;

        DTOSubclasseCnae that = (DTOSubclasseCnae) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
