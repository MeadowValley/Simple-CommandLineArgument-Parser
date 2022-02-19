data class Scheme(val arguments: List<Argument>) {
    fun isFlagPresent(argumentShortName: String): Boolean {
        val arg = arguments.find { it.shortName == argumentShortName }
            ?: throw NoSuchElementException("Argument with shortName=$argumentShortName not found")

        if (!arg.isFlag) {
            throw RuntimeException("Called isFlagPresent on argument with shortName=$argumentShortName but it is no flag")
        }

        return arg.value == true
    }

    fun getValues(argumentShortName: String): List<*> {
        val arg = arguments.find { it.shortName == argumentShortName }
            ?: throw NoSuchElementException("Argument with shortName=$argumentShortName not found")

        if (arg.isFlag) {
            throw RuntimeException("Called getValues on argument with shortName=$argumentShortName but it is a flag")
        }

        return arg.value as? List<*>
            ?: throw RuntimeException("Argument with shortName=$argumentShortName exists but could not get values")
    }

    fun toPrettyOutput(): String {
        var string = ""
        arguments.forEach {
            string += "${it.name}=${it.value}\n"
        }
        return string
    }
}