package gov.goias.auth.util;

import java.io.IOException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import gov.goias.auth.Certificado;
import org.apache.log4j.Logger;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.*;

@SuppressWarnings("rawtypes")
public class CertificadoExtrator {

    private static final Logger logger = Logger.getLogger(CertificadoExtrator.class);

	// OIDs especificados pela ICP Brasil - eCPF
//    public static final DERObjectIdentifier OID_PF_DADOS_TITULAR = new DERObjectIdentifier("2.16.76.1.3.1");
	public static final ASN1ObjectIdentifier OID_PF_DADOS_TITULAR = new ASN1ObjectIdentifier("2.16.76.1.3.1");
	public static final ASN1ObjectIdentifier OID_PF_ELEITORAL = new ASN1ObjectIdentifier("2.16.76.1.3.5");
	public static final ASN1ObjectIdentifier OID_PF_INSS = new ASN1ObjectIdentifier("2.16.76.1.3.6");
	// OIDs especificados pela ICP Brasil - eCNPJ
	public static final ASN1ObjectIdentifier OID_PJ_RESPONSAVEL = new ASN1ObjectIdentifier("2.16.76.1.3.2");
	public static final ASN1ObjectIdentifier OID_PJ_CNPJ = new ASN1ObjectIdentifier("2.16.76.1.3.3");
	public static final ASN1ObjectIdentifier OID_PJ_DADOS_RESPONSAVEL = new ASN1ObjectIdentifier("2.16.76.1.3.4");
	public static final ASN1ObjectIdentifier OID_PJ_INSS = new ASN1ObjectIdentifier("2.16.76.1.3.7");
	public static final ASN1ObjectIdentifier OID_PJ_NOME_EMPRESARIAL = new ASN1ObjectIdentifier("2.16.76.1.3.8");
	public static final ASN1ObjectIdentifier OID_PJ_INSCRICAO_ESTADUAL = new ASN1ObjectIdentifier("2.16.76.1.4.1");

	public static Certificado extrairDados(X509Certificate cert) throws CertificateParsingException, ParseException {
		Certificado certificado = new Certificado();
		// Dados do proprio certificado
		certificado.setCommonName(cert.getSubjectX500Principal().getName());
		certificado.setIssuerCommonName(cert.getIssuerX500Principal().getName());
		certificado.setSerialNumber(cert.getSerialNumber().toString(16));
		// Informacoes de validade do certificado
		certificado.setDataInicioValidade(cert.getNotBefore());
		certificado.setDataFimValidade(cert.getNotAfter());
		Collection col = getSubjectAlternativeNames(cert);
		for(Object obj : col) {
			if(obj instanceof ArrayList) {
				ArrayList lst = (ArrayList) obj;
				Object value = lst.get(1);
				if(value instanceof DLSequence) {
                    DLSequence seq = (DLSequence) value;
//                    DERObjectIdentifier oid = (DERObjectIdentifier) seq.getObjectAt(0);
                    ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) seq.getObjectAt(0);
					DERTaggedObject tagged = (DERTaggedObject) seq.getObjectAt(1);
					String info = null;
//                    DERObject derObj = tagged.getObject();
                    ASN1Object derObj = tagged.getObject();
					if(derObj instanceof DEROctetString) {
						DEROctetString octet = (DEROctetString) derObj;
						info = new String(octet.getOctets());
					} else if(derObj instanceof DERPrintableString) {
						DERPrintableString octet = (DERPrintableString) derObj;
						info = new String(octet.getOctets());
					} else if(derObj instanceof DERUTF8String) {
						DERUTF8String str = (DERUTF8String) derObj;
						info = str.getString();
					}
					if(info != null && !info.isEmpty()) {
						// e-CPF: Dados do Titular
						if(oid.equals(CertificadoExtrator.OID_PF_DADOS_TITULAR)) {
							certificado.setTipoCertificado('F');
							certificado.setPfDataNascimento(new SimpleDateFormat("ddMMyyyy").parse(info.substring(0, 8)));
							certificado.setPfCpf(info.substring(8, 19));
							certificado.setPfNis(info.substring(19, 30));
							certificado.setPfRg(info.substring(30, 45));
							if(!certificado.getPfRg().equals("000000000000000") && info.length() > 46) {
								certificado.setPfRgOrgaoExp(info.substring(45, info.length()));
							}
						// e-CPF: Outros dados
						} else if(oid.equals(CertificadoExtrator.OID_PF_INSS)) {
							certificado.setPfInss(info.substring(0, 12));
						} else if(oid.equals(CertificadoExtrator.OID_PF_ELEITORAL)) {
							certificado.setPfTituloEleitoral(info.substring(0, 12));
							certificado.setPfTituloEleitoralZona(info.substring(12, 15));
							certificado.setPfTituloEleitoralSecao(info.substring(15, 19));
						// e-CNPJ: Dados da empresa
						} else if(oid.equals(CertificadoExtrator.OID_PJ_CNPJ)) {
							certificado.setPjCnpj(info);
							certificado.setTipoCertificado('J');
						} else if(oid.equals(CertificadoExtrator.OID_PJ_NOME_EMPRESARIAL)) {
							certificado.setPjNomeEmpresarial(info);
						} else if(oid.equals(CertificadoExtrator.OID_PJ_INSS)) {
							certificado.setPjInss(info);
						} else if(oid.equals(CertificadoExtrator.OID_PJ_RESPONSAVEL)) {
							certificado.setPjResponsavel(info);
						}
						// e-CNPJ: Dados do Responsavel pela empresa
					} else if(oid.equals(CertificadoExtrator.OID_PJ_DADOS_RESPONSAVEL)) {
						certificado.setPjResponsavelDataNascimento(new SimpleDateFormat("ddMMyyyy").parse(info.substring(0, 8)));
						certificado.setPjResponsavelCpf(info.substring(8, 19));
						certificado.setPjResponsavelNis(info.substring(19, 30));
						certificado.setPjResponsavelRg(info.substring(30, 45));
						if(!certificado.getPjResponsavelRg().equals("000000000000000")) {
							certificado.setPjResponsavelRgOrgaoExp(info.substring(45, 50));
						}
					}
				} else if(value instanceof String && value.toString().contains("@")) {
					certificado.setEmail(value.toString().toLowerCase());
				} else {
					logger.warn("Erro na extraç&#225;o de dados do certificado digital - Valor desconhecido: " + value);
				}
			}
		}
		return certificado;
	}
	
	public static Collection getIssuerAlternativeNames(X509Certificate cert) throws CertificateParsingException {
//		byte[] extVal = cert.getExtensionValue(X509Extensions.IssuerAlternativeName.getId());
		byte[] extVal = cert.getExtensionValue(Extension.issuerAlternativeName.getId());
		return getAlternativeName(extVal);
	}

	private static Collection getSubjectAlternativeNames(X509Certificate cert) throws CertificateParsingException {
//		byte[] extVal = cert.getExtensionValue(X509Extensions.SubjectAlternativeName.getId());
		byte[] extVal = cert.getExtensionValue(Extension.subjectAlternativeName.getId());
		return getAlternativeName(extVal);
	}
	
	@SuppressWarnings("unchecked")
	private static Collection getAlternativeName(byte[] extVal) throws CertificateParsingException {
		Collection temp = new ArrayList();
		if(extVal == null) {
			return Collections.EMPTY_LIST;
		}
		try {
//			byte[] extnValue = DEROctetString.getInstance(ASN1Object.fromByteArray(extVal)).getOctets();
			byte[] extnValue = DEROctetString.getInstance(ASN1Primitive.fromByteArray(extVal)).getOctets();
//			Enumeration it = DERSequence.getInstance(ASN1Object.fromByteArray(extnValue)).getObjects();
			Enumeration it = DERSequence.getInstance(ASN1Primitive.fromByteArray(extnValue)).getObjects();
			while(it.hasMoreElements()) {
				GeneralName genName = GeneralName.getInstance(it.nextElement());
				List list = new ArrayList();
				list.add(Integer.valueOf(genName.getTagNo()));
				switch(genName.getTagNo()) {
					case GeneralName.ediPartyName:
					case GeneralName.x400Address:
					case GeneralName.otherName:
//                        list.add(genName.getName().getDERObject());
						list.add(genName.getName().toASN1Primitive());
						break;
					case GeneralName.directoryName:
//						list.add(X509Name.getInstance(genName.getName()).toString());
						list.add(X500Name.getInstance(genName.getName()).toString());
						break;
					case GeneralName.dNSName:
					case GeneralName.rfc822Name:
					case GeneralName.uniformResourceIdentifier:
						list.add(((DERIA5String) genName.getName()).getString());
						break;
					case GeneralName.registeredID:
						list.add(ASN1ObjectIdentifier.getInstance(genName.getName()).getId());
						break;
					case GeneralName.iPAddress:
						list.add(DEROctetString.getInstance(genName.getName()).getOctets());
						break;
					default:
						throw new IOException("Bad tag number: " + genName.getTagNo());
				}
				temp.add(list);
			}
		} catch(Exception e) {
			throw new CertificateParsingException(e.getMessage());
		}
		return Collections.unmodifiableCollection(temp);
	}
}