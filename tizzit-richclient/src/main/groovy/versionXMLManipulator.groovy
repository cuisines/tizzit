import java.lang.StringBuffer;
import groovy.util.XmlNodePrinter 
import java.io.*
import java.util.zip.*
import org.xml.sax.InputSource 
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Node
import org.tizzit.util.XercesHelper
import org.apache.xml.serialize.OutputFormat

def jnlpFile = new File("${pom.basedir}/target/jnlp/juwimm-cms-client.jnlp")
def versionFile = new File("${pom.basedir}/target/jnlp/version.xml")

def jnlpRootNode = new XmlParser().parse(jnlpFile)

def builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
def versionDocument = builder.parse(new FileInputStream(versionFile))
def jnlpVersionsNode = versionDocument.documentElement

def hrefString = ''
def version = '' 
	
for (jar in jnlpRootNode.resources.jar) {
	hrefString = "${jar.'@href'}"
	version = "${jar.'@version'}"
	
// to be generated: (XXX is the version extension according to the pom.xml, e.g. "-2008")	
//	<resource>
//     <pattern>
//        <name>libraryName-version.jar</name>
//         <version-id>version-XXX</version-id>
//      </pattern>
//      <file>libraryName-version.jar</file>
//  </resource>

	def resourceElement = versionDocument.createElement('resource')
	def patternElement = versionDocument.createElement('pattern')
	def nameElement = versionDocument.createElement('name')
	def versionIdElement = versionDocument.createElement('version-id')
	def file = versionDocument.createElement('file')
	
	nameElement.appendChild(versionDocument.createTextNode("$hrefString"))
	versionIdElement.appendChild(versionDocument.createTextNode("$version"))
	
	patternElement.appendChild(nameElement)
	patternElement.appendChild(versionIdElement)
	resourceElement.appendChild(patternElement)
	
	file.appendChild(versionDocument.createTextNode("$hrefString"))
	resourceElement.appendChild(file)

	jnlpVersionsNode.appendChild(resourceElement)
	
	//jar.'@version' = version
}


def versionContent = XercesHelper.doc2String((org.w3c.dom.Document) versionDocument)

def fileOutputStream = new FileOutputStream(versionFile)
def printWriter = new PrintWriter(fileOutputStream)
printWriter.write(versionContent)
printWriter.flush()
/*
fileOutputStream = new FileOutputStream(jnlpFile)
printWriter = new PrintWriter(fileOutputStream)
def xmlNodePrinter = new XmlNodePrinter(printWriter)
xmlNodePrinter.print(jnlpRootNode)

printWriter.flush()
printWriter.close()*/
fileOutputStream.close()

// Delete ZIP from webstart plugin
def outFilename = "${pom.basedir}/target/${pom.artifactId}-${pom.version}.zip";
new File(outFilename).delete()

// Create ZIP archive

def out = new ZipOutputStream(new FileOutputStream(outFilename));
def buf = new byte[1024];
def jnlpDir = new File("${pom.basedir}/target/jnlp")
jnlpDir.eachFile {
	def fis = it.newInputStream()
    out.putNextEntry(new ZipEntry(it.name));
    // Transfer bytes from the file to the ZIP file
    def len;
    while ((len = fis.read(buf)) > 0) {
        out.write(buf, 0, len);
    }
    // Complete the entry
    out.closeEntry();
    fis.close();
}

// Complete the ZIP file
out.close();

// delete jnlp dir to get it fresh generated for the next time
jnlpDir.eachFileRecurse{ it.deleteOnExit() }






