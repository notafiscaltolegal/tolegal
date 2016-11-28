package gov.goias.util;

/**
 * Created by Remisson Silva on 21/10/2014.
 */

public class ValidacaoDeCpf 
{
	private String cpf;
	private String numeros,digitos;
	private Integer soma,resultado;

	public ValidacaoDeCpf(String cpf){ this.cpf = cpf; }

	public Boolean isCpfValido()
	{
		if (cpf.length() < 11) return false;

		if (!isDigitosIguais())
		{
			numeros = cpf.substring(0,9);
			digitos = cpf.substring(9);
			soma = 0;

			for (int i = 10; i > 1; i--)
				soma += Integer.valueOf(String.valueOf(numeros.charAt( (10 - i)))) * i;
			resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;

			if (! resultado.toString().equals( String.valueOf(digitos.charAt(0))))
				return false;
			numeros = cpf.substring(0,10);
			soma = 0;
			for (int i = 11; i > 1; i--)
				soma += Integer.valueOf(String.valueOf(numeros.charAt( (11 - i)))) * i;
			resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
			if (! resultado.toString().equals( String.valueOf(digitos.charAt(1))))
				return false;
			return true;
		}
		else
			return false;

	}

	public boolean isDigitosIguais()
	{
		for (int i = 0; i < cpf.length() - 1; i++)
		{
			if (cpf.charAt(i) != cpf.charAt(i + 1))
			{
				return false;
			}
		}
		return true;
	}
}
