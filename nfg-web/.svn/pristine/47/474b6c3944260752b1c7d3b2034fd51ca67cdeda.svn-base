#!/home/henrique-rh/.gvm/groovy/current/bin/groovy

import groovy.json.JsonSlurper

if (args.size() != 2){
    println("Usage: ./rename /diretorio/origem /diretorio/destino")
    return 1
}

def origem = new File(args[0])
def destino = new File(args[1])
def HashMap<String, String> cpfJson = new HashMap<>()
origem.eachLine('ISO-8859-1') { line ->
    try {
        def cidadao = new JsonSlurper().parseText(line)
        if (!cpfJson.get(cidadao?.cpf)) {
            cpfJson.put(cidadao?.cpf, line)
            destino << "$line\n"
        }
    } catch (Exception e) {
        println "ERRO NA LINHA:"
        println line
        println "===================="
        e.printStackTrace()
    }
}
