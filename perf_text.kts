import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun runCommand(command: String, workingDir: File = File("."), outputFile: File? = null): String? {
    try {

        val processBuilder = ProcessBuilder(*command.split(" ").toTypedArray())
                .directory(workingDir)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)

        outputFile?.let {
            outputFile.parentFile.mkdirs()
            if(!outputFile.exists()) outputFile.createNewFile()
            processBuilder.redirectOutput(outputFile)
        }


        val process = processBuilder.start()
        process.apply {
            waitFor(60, TimeUnit.SECONDS)
        }
        return process.inputStream.bufferedReader().readText()

    } catch(e: Exception) {
        println(e.message);
        return null;
    }
}

fun main(vararg args: String) {
    val androidHomeDir = System.getenv().getOrElse("ANDROID_HOME") {
        throw NullPointerException("The ANDROID_HOME was not set into your environment variables");
    }

    val projectDir = "."
    val formatter = SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
    val gradleExecPath = "gradlew.bat"
    val date = Date()
    val logFileName = formatter.format(date)
    val outputFile = projectDir
            .appendWritePath("logs")
            .appendWritePath(logFileName)
    val installOutputFile = projectDir
            .appendWritePath("logs")
            .appendWritePath("installation")
            .appendWritePath(logFileName)
    val adbPath = androidHomeDir
            .appendExecPath("platform-tools")
            .appendExecPath("adb")
    val packageName = args.getOrNull(0) ?: "org.mariusc.gitdemo"

    runCommand("gradlew.bat :app:assembleDebug", outputFile = File(installOutputFile))
    println("Android Home: $androidHomeDir")
    println("Project Dir: $projectDir")
    println("Gradle Exec Path: $gradleExecPath")
    println("Output file: $outputFile")
    println("Adb Path: $adbPath")
    println("Package Name: $packageName")
    println("Install Output File: $installOutputFile")
}

fun String.appendWritePath(path: String): String {
    return this + File.separator + path
}

fun String.appendExecPath(path: String): String {
    return this + "\\" + path
}

main(*args)