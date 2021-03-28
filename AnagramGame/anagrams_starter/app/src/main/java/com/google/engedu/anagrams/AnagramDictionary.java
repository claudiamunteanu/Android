/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random;
    ArrayList<String> wordList;
    HashSet<String> wordSet;
    HashMap<String, ArrayList<String>> lettersToWord;
    HashMap<Integer, ArrayList<String>> sizeToWords;
    int wordLength;

    public AnagramDictionary(Reader reader) throws IOException {
        random = new Random();
        wordList = new ArrayList<>();
        wordSet = new HashSet<>();
        lettersToWord = new HashMap<>();
        sizeToWords = new HashMap<>();
        wordLength = DEFAULT_WORD_LENGTH;

        BufferedReader in = new BufferedReader(reader);
        String line;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sortedWord = sortLetters(word);
            //if the dictionary contains the key, we add the word to the list of values
            if (lettersToWord.containsKey(sortedWord)) {
                lettersToWord.get(sortedWord).add(word);
            } else {
                ArrayList<String> list = new ArrayList<String>();
                list.add(word);
                lettersToWord.put(sortedWord, list);
            }

            if (sizeToWords.containsKey(word.length())) {
                sizeToWords.get(word.length()).add(word);
            } else {
                ArrayList<String> list = new ArrayList<String>();
                list.add(word);
                sizeToWords.put(word.length(), list);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }

    private String sortLetters(String word) {
        char[] letters = word.toCharArray();
        Arrays.sort(letters);
        return new String(letters);
    }

    public List<String> getAnagrams(final String targetWord) {
        final ArrayList<String> result = new ArrayList<String>();
        wordList.forEach(word -> {
            if (word.length() == targetWord.length() && sortLetters(word).equals(sortLetters(targetWord)))
                result.add(word);
        });
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String alphabets = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < alphabets.length(); i++) {
            String tempString = word;
            //add every letter and get the key
            tempString += alphabets.charAt(i);
            String key = sortLetters(tempString);
            //check if that key exists
            if (lettersToWord.containsKey(key)) {
                //get all the values for that key
                ArrayList<String> words = lettersToWord.get(key);
                for (String w : words) {
                    if (isGoodWord(w, word))
                        result.add(w);
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> words = sizeToWords.get(wordLength);
        int randPosition = random.nextInt(words.size());
        String word = words.get(randPosition);
        while (getAnagramsWithOneMoreLetter(word).size() < MIN_NUM_ANAGRAMS) {
            randPosition = random.nextInt(words.size());
            word = words.get(randPosition);
        }
        if (wordLength < MAX_WORD_LENGTH) {
            wordLength++;
        }
        return word;
    }
}
