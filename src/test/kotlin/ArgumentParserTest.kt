import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Assertions.assertEquals

internal class ArgumentParserTest {
    @Test
    fun `ON parsing WHEN everything is correct THEN scheme get the expected values`() {
        val args =
            arrayOf("-n", "-1", "420", "-1000", "-42", "-l", "-p", "8080", "-d", "/usr/logs", "-x", "blah", "blurb")
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

        ArgumentParser.parse(args, scheme)

        assertEquals(true, scheme.isFlagPresent("-l"))
        assertEquals(false, scheme.isFlagPresent("-t"))
        assertEquals(listOf(8080), scheme.getValues("-p"))
        assertEquals(listOf("blah", "blurb"), scheme.getValues("-x"))
        assertEquals(listOf(-1, 420, -1000, -42), scheme.getValues("-n"))
    }

    @Test
    fun `ON validation WHEN argument is not in scheme THEN IllegalArgumentException is thrown`() {
        val args = arrayOf("-x")
        val scheme = Scheme(listOf(Argument("", "", "test", false)))
        assertThrows<IllegalArgumentException> { ArgumentParser.validate(args, scheme) }
    }

    @Test
    fun `ON validation WHEN argument has no parameters THEN IllegalArgumentException is thrown`() {
        val args1 = arrayOf("-x")
        val scheme1 = Scheme(listOf(Argument(shortName = "-x", name = "test", value = "", isFlag = false)))
        assertThrows<IllegalArgumentException> { ArgumentParser.validate(args1, scheme1) }

        val args2 = arrayOf("-l", "-x", "-y", "test")
        val scheme2 = Scheme(
            listOf(
                Argument(shortName = "-x", name = "test", value = false, isFlag = false),
                Argument(shortName = "-l", name = "test", value = false, isFlag = true),
                Argument(shortName = "-y", name = "test", value = false, isFlag = false),
            )
        )
        assertThrows<IllegalArgumentException> { ArgumentParser.validate(args2, scheme2) }

        val args3 = arrayOf("-l", "-y", "test", "-x")
        assertThrows<IllegalArgumentException> { ArgumentParser.validate(args3, scheme2) }
    }

    @Test
    fun `ON validation WHEN flag has parameters THEN IllegalArgumentException is thrown`() {
        val args1 = arrayOf("-x", "someArg")
        val scheme1 = Scheme(listOf(Argument(shortName = "-x", name = "test", value = false, isFlag = true)))
        assertThrows<IllegalArgumentException> { ArgumentParser.validate(args1, scheme1) }

        val args2 = arrayOf("-l", "-x", "someArg", "-y", "test")
        val scheme2 = Scheme(
            listOf(
                Argument(shortName = "-x", name = "test", value = false, isFlag = true),
                Argument(shortName = "-l", name = "test", value = false, isFlag = true),
                Argument(shortName = "-y", name = "test", value = false, isFlag = false),
            )
        )
        assertThrows<IllegalArgumentException> { ArgumentParser.validate(args2, scheme2) }

        val args3 = arrayOf("-l", "-y", "test", "-x", "someArg")
        assertThrows<IllegalArgumentException> { ArgumentParser.validate(args3, scheme2) }
    }
}