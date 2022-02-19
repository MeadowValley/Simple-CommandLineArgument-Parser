fun main(args: Array<String>) {
    println("Program arguments: ${args.joinToString(" ")}\n")

    val scheme = Scheme(
        listOf(
            Argument(shortName = "-l", name = "logging", value = false, isFlag = true),
            Argument(shortName = "-t", name = "testFlag", value = false, isFlag = true),
            Argument(shortName = "-p", name = "port", value = 42069, isFlag = false),
            Argument(shortName = "-d", name = "directory", value = "", isFlag = false),
            Argument(shortName = "-x", name = "aList", value = "", isFlag = false),
            Argument(shortName = "-n", name = "someNumbers", value = "", isFlag = false)
        )
    )
    ArgumentParser.validate(args, scheme)
    ArgumentParser.parse(args, scheme)

    println(scheme.toPrettyOutput())
}