package com.bcit.lib

fun main() {
    val words = arrayOf("Hi", "Something", "Someone", "Hello", "Hahahahah", "Ha")
    val wordLengths: MutableList<Int> = mutableListOf<Int>()
    val longestWords: MutableList<String> = mutableListOf<String>()
    val shortestWords: MutableList<String> = mutableListOf<String>()
    val sortedWordLengths: List<Int>
    val longestWordLength: Int
    val shortestWordLength: Int

    var i = 0

    while (i < words.size) {
        wordLengths.add(words[i].length)
        i++
    }

    sortedWordLengths = wordLengths.sorted()
    longestWordLength = sortedWordLengths[sortedWordLengths.size-1]
    shortestWordLength = sortedWordLengths[0]

    for(word in words){
        if(word.length == longestWordLength){
            longestWords.add(word)
            continue
        }

        if(word.length == shortestWordLength){
            shortestWords.add(word)
        }
    }


    println("Words: ${getArrayStyleString(words)}");
    println("Word Lengths: ${getArrayStyleString(wordLengths)}");
    println("Longest Word(s): ${getArrayStyleString(longestWords)}");
    println("Shortest Word(s): ${getArrayStyleString(shortestWords)}");

}

fun getArrayStyleString(input: Any): String {
    return when (input) {
        is Array<*> -> input.joinToString(prefix = "[", postfix = "]")
        is Iterable<*> -> input.joinToString(prefix = "[", postfix = "]")
        else -> input.toString() // Fallback for single objects
    }
}