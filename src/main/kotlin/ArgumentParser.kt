object ArgumentParser {
    fun parse(args: Array<String>, scheme: Scheme) {
        args.forEachIndexed { argCount, arg ->
            if (isArgument(arg)) {
                val argument = scheme.arguments.first { it.shortName == arg }

                if (argument.isFlag) {
                    argument.value = true
                } else {
                    val argValues = mutableListOf<String>().apply {
                        for (j in argCount + 1 until args.size) {
                            if (!isArgument(args[j])) add(args[j])
                            else break
                        }
                    }.toList()

                    argument.value = if (isListOfNumbers(argValues)) argValues.map { it.toInt() } else argValues
                }
            }
        }
    }

    fun validate(args: Array<String>, scheme: Scheme) {
        args.forEachIndexed { i, arg ->
            if (isArgument(arg)) {
                val tempArg = scheme.arguments.firstOrNull { it.shortName == arg }
                    ?: throw IllegalArgumentException("Argument $arg is not defined in the scheme")
                if (!tempArg.isFlag && (i == args.size - 1 || isArgument(args[i + 1]))) {
                    throw IllegalArgumentException("Missing parameters for $arg")
                }
                if (tempArg.isFlag && i != args.size - 1 && !isArgument(args[i + 1])) {
                    throw IllegalArgumentException("$arg is a flag and should not have any parameters")
                }
            }
        }
    }

    private fun isArgument(string: String): Boolean {
        return string.startsWith("-") && !isNumber(string)
    }

    private fun isNumber(string: String): Boolean {
        return string.matches(Regex("^-?\\d+\$"))
    }

    private fun isListOfNumbers(strings: List<String>): Boolean {
        strings.forEach { if (!isNumber(it)) return false }
        return true
    }
}