//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.08.07 at 03:26:21 PM BRT 
//


package gov.goias.bindings;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ConsultarCPFP3TResult" type="{https://infoconv.receita.fazenda.gov.br/ws/cpf/}ArrayOfPessoaPerfil3" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "consultarCPFP3TResult"
})
@XmlRootElement(name = "ConsultarCPFP3TResponse")
public class ConsultarCPFP3TResponse {

    @XmlElement(name = "ConsultarCPFP3TResult")
    protected ArrayOfPessoaPerfil3 consultarCPFP3TResult;

    /**
     * Gets the value of the consultarCPFP3TResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfPessoaPerfil3 }
     *     
     */
    public ArrayOfPessoaPerfil3 getConsultarCPFP3TResult() {
        return consultarCPFP3TResult;
    }

    /**
     * Sets the value of the consultarCPFP3TResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfPessoaPerfil3 }
     *     
     */
    public void setConsultarCPFP3TResult(ArrayOfPessoaPerfil3 value) {
        this.consultarCPFP3TResult = value;
    }

}
