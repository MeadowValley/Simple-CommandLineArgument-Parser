import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class SchemeTest {

    private val scheme = Scheme(
        listOf(
            Argument(shortName = "-l", name = "logging", value = true, isFlag = true),
            Argument(shortName = "-t", name = "testFlag", value = false, isFlag = true),
            Argument(shortName = "-p", name = "port", value = listOf(42069), isFlag = false),
            Argument(shortName = "-d", name = "directory", value = listOf("/usr/logs"), isFlag = false),
            Argument(shortName = "-x", name = "aList", value = listOf("a", "b"), isFlag = false),
            Argument(shortName = "-n", name = "someNumbers", value = listOf(1, 2, 3, -5), isFlag = false)
        )
    )

    @Test
    fun isFlagPresent() {
        assertEquals(true, scheme.isFlagPresent("-l"))
        assertEquals(false, scheme.isFlagPresent("-t"))

        val shortName1 = "-blah"
        assertThrows<NoSuchElementException> { scheme.isFlagPresent(shortName1) }.apply {
            assertEquals("Argument with shortName=$shortName1 not found", message)
        }

        val shortName2 = "-x"
        assertThrows<RuntimeException> { scheme.isFlagPresent(shortName2) }.apply {
            assertEquals("Called isFlagPresent on argument with shortName=$shortName2 but it is no flag", message)
        }
    }

    @Test
    fun getValues() {
        assertEquals(listOf(42069), scheme.getValues("-p"))
        assertEquals(listOf("/usr/logs"), scheme.getValues("-d"))
        assertEquals(listOf("a", "b"), scheme.getValues("-x"))
        assertEquals(listOf(1, 2, 3, -5), scheme.getValues("-n"))

        val shortName1 = "-blah"
        assertThrows<NoSuchElementException> { scheme.getValues(shortName1) }.apply {
            assertEquals("Argument with shortName=$shortName1 not found", message)
        }

        val shortName2 = "-l"
        assertThrows<RuntimeException> { scheme.getValues(shortName2) }.apply {
            assertEquals("Called getValues on argument with shortName=$shortName2 but it is a flag", message)
        }
    }
}