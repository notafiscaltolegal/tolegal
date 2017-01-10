#!/home/henrique-rh/.gvm/groovy/current/bin/groovy

import groovy.json.JsonSlurper

if (args.size() != 3){
    println("Usage: ./remove /diretorio/origem /diretorio/log /diretorio/destino")
    return 1
}

def origem = new File(args[0])
def log = new File(args[1])
def destino = new File(args[2])

def HashMap<String, String> emailJson = new HashMap<>()
origem.eachLine('ISO-8859-1') { line ->
    try {
        def cidadao = new JsonSlurper().parseText(line)
        if (!emailJson.get(cidadao?.email)) {
            emailJson.put(cidadao?.email, line)
        }
    } catch (Exception e) {
        println "ERRO NA LINHA:"
        println line
        println "===================="
        e.printStackTrace()
    }
}

log.eachLine('ISO-8859-1') { line ->
    def str = line.split('sucesso para destinat')
    if (str.size() > 1) {
        def email = str[1].split(':')[1]
        emailJson.remove(email)
    }
}

for (String email : emailJson.keySet()) {
    def line = emailJson.get(email)
    destino.append("$line\n",'ISO-8859-1')
}
println emailJson.size()
