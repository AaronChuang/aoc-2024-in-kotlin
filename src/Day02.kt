import kotlin.math.abs

class SolutionCreator2 {

    private fun extractNumbers(text: String): List<Int> {
        val regex = Regex("\\d+")
        val matches = regex.findAll(text)
        return matches.map {
            it.value.toInt()
        }.toList()
    }

    private fun safeListCheck(input: List<Int>): Boolean {
        var pre: Int? = null
        var isIncrease: Boolean? = null
        val leastDiffLevel = 1
        val maxDiffLevel = 3
        input.forEach { t ->
            if (pre != null) {
                val diff = t - pre
                if (abs(diff) < leastDiffLevel || abs(diff) > maxDiffLevel) {
                    return false
                }
                if (isIncrease == null) {
                    isIncrease = diff > 0
                } else {
                    if (diff > 0 && !isIncrease) {
                        return false
                    } else if (diff < 0 && isIncrease) {
                        return false
                    }
                }
            }
            pre = t
        }
        return true
    }

    private fun safeListCheckWithProblemDampener(input: List<Int>): Boolean {
        var pre: Int? = null
        var isIncrease: Boolean? = null
        val leastDiffLevel = 1
        val maxDiffLevel = 3
        var problemDampener: Int? = null
        input.forEachIndexed { index, t ->
            var damping = false
            if (pre != null) {
                var diff = t - pre
                if (abs(diff) < leastDiffLevel || abs(diff) > maxDiffLevel) {
                    if (problemDampener == null) {
                        damping = true
                        val window = getCheckWindow(input, index)
                        var subCheck = checkSubList(window)
                        if (subCheck.first) {
                            problemDampener = window[subCheck.second]
                            val subWindow =
                                window.subList(0, subCheck.second) + window.subList(subCheck.second + 1, window.size)
                            isIncrease = (subWindow[1] - subWindow[0]) > 0
                        } else {
                            println("$input is not safe.")
                            return false
                        }
                    } else {
                        println("$input is not safe.")
                        return false
                    }
                }
                if (!damping) {
                    if (isIncrease == null) {
                        isIncrease = diff > 0
                    } else {
                        if (diff > 0 && !isIncrease) {
                            if (problemDampener == null) {
                                damping = true
                                val window = getCheckWindow(input, index)
                                var subCheck = checkSubList(window)
                                if (subCheck.first) {
                                    problemDampener = window[subCheck.second]
                                    val subWindow =
                                        window.subList(0, subCheck.second) + window.subList(subCheck.second + 1, window.size)
                                    isIncrease = (subWindow[1] - subWindow[0]) > 0
                                } else {
                                    println("$input is not safe.")
                                    return false
                                }
                            } else {
                                println("$input is not safe.")
                                return false
                            }
                        } else if (diff < 0 && isIncrease) {
                            if (problemDampener == null) {
                                damping = true
                                val window = getCheckWindow(input, index)
                                var subCheck = checkSubList(window)
                                if (subCheck.first) {
                                    problemDampener = window[subCheck.second]
                                    val subWindow =
                                        window.subList(0, subCheck.second) + window.subList(subCheck.second + 1, window.size)
                                    if(index > 2 || index < (input.size - 1)){
                                        damping = (problemDampener == t && index == subCheck.second)
                                    }
                                    isIncrease = (subWindow[1] - subWindow[0]) > 0
                                } else {
                                    println("$input is not safe.")
                                    return false
                                }
                            } else {
                                println("$input is not safe.")
                                return false
                            }
                        }
                    }
                }
            }
            if(t != problemDampener || damping == false){
                pre = t
            }
        }
        if (problemDampener != null) {
            println("$input dump:$problemDampener could be safe.")
        } else {
            println("$input is safe.")
        }
        return true
    }

    fun checkSubList(list: List<Int>): Pair<Boolean, Int> {
        var checked = false
        var index = 0
        val dataLength = list.size
        var dumpNumIndex = -1
        while (checked == false && index < dataLength) {
            val subList = list.subList(0, index) + list.subList(index + 1, dataLength)
            checked = safeListCheck(subList)
            if (checked) {
                dumpNumIndex = index
            }
            index++
        }
        return Pair(checked, dumpNumIndex)
    }

    fun getCheckWindow(list: List<Int>, index: Int): List<Int> {
        if (list.size <= 5) {
            return list
        }
        return when (index) {
            0, 1 -> {
                list.subList(0, 5)
            }

            list.size - 1, list.size - 2 -> {
                list.subList(list.size - 5, list.size)
            }

            else -> list.subList(index - 2, index + 3)
        }
    }

    fun part1Solution(input: List<String>): Int {
        val safeList = input.map { line ->
            val report = extractNumbers(line)
            val checked = safeListCheck(report)
            println("report: $report $checked")
            checked
        }
        return safeList.count { bool -> bool == true }
    }

    /**
     * part2 basic solution: exhaustive
     */
    fun part2Solution(input: List<String>): Int {
        val safeList = input.map { line ->
            val report = extractNumbers(line)
            var checked = safeListCheck(report)
            if (checked == false) {
                checked = checkSubList(report).first
            }
            checked
        }
        return safeList.count { bool -> bool == true }
    }

    /**
     * part2 another solution
     */
    fun part2Solution2(input: List<String>): Int {
        val safeList = input.map { line ->
            val report = extractNumbers(line)
            safeListCheckWithProblemDampener(report)
        }
        return safeList.count { bool -> bool == true }
    }

    /**
     * check part2Solution and part2Solution2 is the same
     */
    fun validate(input: List<String>){
        input.forEach { line ->
            val report = extractNumbers(line)
            var type1 = safeListCheck(report)
            if (type1 == false) {
                type1 = checkSubList(report).first
            }
            val type2 = safeListCheckWithProblemDampener(report)
            if(type1 != type2){
                println("error $line $type1 $type2")
            }
        }
    }
}


fun main() {

    val solution = SolutionCreator2()
    fun part1(input: List<String>): Int {
        val safeReportCount = solution.part1Solution(input)
        println("safe report count: $safeReportCount")
        return safeReportCount
    }

    fun part2(input: List<String>): Int {
        val safeReportCount = solution.part2Solution2(input)
        println("safe report count: $safeReportCount")
        return safeReportCount
    }

    val testInput = readInput("Day02_test")
    val input = readInput("Day02")

    // Part1
    check(part1(testInput) == 2)
    part1(input).println()
    //Part2
    check(part2(testInput) == 4)
    part2(input).println()
}
