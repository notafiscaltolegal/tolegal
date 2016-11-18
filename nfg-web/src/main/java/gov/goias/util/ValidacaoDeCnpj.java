package gov.goias.util;

/**
 * Created by Letícia Álvares on 09/06/2016.
 */

public class ValidacaoDeCnpj
{
	private String cnpj;
	private static final int[] pesoCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

	public ValidacaoDeCnpj(String cnpj){ this.cnpj = cnpj; }

	public boolean isValidCNPJ() {

		if ((this.cnpj==null)||(this.cnpj.length()!=14)) return false;

		Integer digito1 = calcularDigito(this.cnpj.substring(0,12), pesoCNPJ);
		Integer digito2 = calcularDigito(this.cnpj.substring(0,12) + digito1, pesoCNPJ);
		return this.cnpj.equals(this.cnpj.substring(0,12) + digito1.toString() + digito2.toString());
	}

	private static int calcularDigito(String str, int[] peso) {
		int soma = 0;
		for (int indice=str.length()-1, digito; indice >= 0; indice-- ) {
			digito = Integer.parseInt(str.substring(indice,indice+1));
			soma += digito*peso[peso.length-str.length()+indice];
		}
		soma = 11 - soma % 11;
		return soma > 9 ? 0 : soma;
	}
}
