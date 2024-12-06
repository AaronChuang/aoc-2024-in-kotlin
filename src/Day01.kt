import kotlin.math.abs

class SolutionCreator {

    private fun extractNumbers(text: String): List<Int> {
        val regex = Regex("\\d+")
        val matches = regex.findAll(text)
        return matches.map {
            it.value.toInt()
        }.toList()
    }

    private fun extractList(input: List<String>): Pair<List<Int>, List<Int>> {
        val list1 = mutableListOf<Int>()
        val list2 = mutableListOf<Int>()
        input.forEach { line ->
            val nums = extractNumbers(line)
            list1.add(nums[0])
            list2.add(nums[1])
        }
        return Pair(list1, list2)
    }

    fun part1Solution(input: List<String>): Int {
        val (list1, list2) = extractList(input)

        val sorted1 = list1.sorted()
        val sorted2 = list2.sorted()

        var totalDistance = 0
        sorted1.forEachIndexed { index, num ->
            var num2 = sorted2[index]
            totalDistance += abs(num - num2)
        }
        println("distance: $totalDistance")
        return totalDistance
    }

    fun part2Solution(input: List<String>): Int {
        val (list1, list2) = extractList(input)
        var cacheCountMap = mutableMapOf<Int, Int>()
        list2.forEach { t ->
            if(!cacheCountMap.containsKey(t)){
                cacheCountMap.put(t, list2.count { n -> n == t })
            }
        }

        var totalDistance = 0
        list1.forEach { num ->
            var numCount = cacheCountMap[num] ?: 0
            totalDistance += num * numCount
        }
        println("distance: $totalDistance")
        return totalDistance
    }
}


fun main() {

    val solution = SolutionCreator()
    fun part1(input: List<String>): Int {
        return solution.part1Solution(input)
    }

    fun part2(input: List<String>): Int {
        return solution.part2Solution(input)
    }
    val testInput = readInput("Day01_test")
    val input = readInput("Day01")

    // Part1
    check(part1(testInput) == 11)
    part1(input).println()

    //Part2
    check(part2(testInput) == 31)
    part2(input).println()
}
