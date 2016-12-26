import groovy.io.FileType
import java.text.SimpleDateFormat

if (args.size() != 2){
    println("Usage: ./scriptMFD /diretorio/origem /diretorio/destino")
    return 1
}
//def origem = new File('origem')
def origem = new File(args[0])
//def destino = new File('destino')
def destino = new File(args[1])
if (!origem.isDirectory()){
    println("Origem n&#225;o é um diretório")
    return 1
}

if (!destino.isDirectory()) {
    println("Destino n&#225;o é um diretório")
    return 1
}

def transmissor, mfd, ie, ni, ecf, finalidade, anoMes, versao, dataHoraEntrega
mfd = "MFD"
finalidade = "0"
versao = "001"
SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy:hh-mm-ss")
SimpleDateFormat formatDataHoraEntrega = new SimpleDateFormat("ddMMyyyyhhmmss")
SimpleDateFormat formatDataHoraEntregaArquivo = new SimpleDateFormat("yyyyMMddhhmmss")
origem.eachFile (FileType.FILES) { file ->
    String ext = '.txt'
    if (file.isDirectory())
        return
    if (file.name.endsWith(".elp"))
        ext = '.elp'

    if (file.name.split("-").size() == 9) {
        def str = file.name.split("_")
        transmissor = "${str[0]}_${str[1]}_${str[2].split("-")[0]}"
    } else {
        transmissor = file.name.substring(0, file.name.indexOf('.')).replace("-","_")
    }

    Boolean firstLineExecuted = false
    file.eachLine { line ->
        if (!firstLineExecuted) {
            dataHoraEntrega = formatDataHoraEntrega.format(formatDataHoraEntregaArquivo.parse(line.substring(67, 81)))
            firstLineExecuted = true
        } else if (line.startsWith('E01')) {
            anoMes = line.substring(127,133)
        } else if (line.startsWith('E02')) {
            ie = line.substring(58,72).trim()
            ni = line.substring(44,58).trim()
            ecf = line.substring(3,23).trim()
        } else {
            return
        }
    }

    println("Movendo de: ${file.name}")
    if (ext == '.txt') {
        String nomeArquivo = "${destino.absolutePath}/$transmissor-$mfd-$ie-$ni-$ecf-$finalidade-$anoMes-$versao-${dataHoraEntrega}${ext}"
        println("Para: $nomeArquivo")
        file.renameTo(nomeArquivo)
    } else {
        def dirElp = new File("${destino.absolutePath}/elp/")
        if (!dirElp.exists())
            dirElp.mkdirs()

        String nomeArquivoElp = "${destino.absolutePath}/elp/$transmissor-$mfd-${dataHoraEntrega}${ext}".replace("MFD", "ELP")
        println("Para: $nomeArquivoElp")
        file.renameTo(nomeArquivoElp)
    }
}

