(ns newsline.core
  (:gen-class)
  (:use [clj-wordnet.core]
  					[opennlp.nlp]
  					[opennlp.tools.filters]
  					[opennlp.treebank]))

(require '(stemmers core soundex porter))

(def get-sentences (make-sentence-detector "models/en-sent.bin"))
(def tokenize (make-tokenizer "models/en-token.bin"))
(def pos-tag (make-pos-tagger "models/en-pos-maxent.bin"))
(def name-find (make-name-finder "models/en-ner-person.bin"))
(def chunker (make-treebank-chunker "models/en-chunker.bin"))

(def wordnet (make-dictionary "models/dict/"))


(defn lemma
	[word]
	(:lemma word))

(defn word-net
	"Returns a word in the IWord format of JWI. Providing the pos tag is optional"
	[word]
	(wordnet word))

(defn relatedSynsets
	"Returns the related synsets of the given word"
	[word]
	(if (not (empty? word)) (flatten(vals(related-synsets word :hypernym)))))

(defn postagger
	"Returns whether the word is a noun or a verb"
	[tag]
	(if (or (= (.toString tag) "NN") (= (.toString tag) "NNS") (= (.toString tag) "NNP") (= (.toString tag) "NNPS"))
					"noun" "verb"))

(defn synsets
  "A synset function to get synsets of all possible meanings of the word."
  [word]
  (def cnts (count word))
  (def synsetword word)
  (def synset (seq '()))
  (loop [j 0]
    (when (< j cnts)
      (def newsynsets (map :lemma (relatedSynsets (first synsetword))))
      (def synset (concat synset newsynsets))
      ;(println j)
      (def synsetword (rest synsetword))
      (recur (inc j))
      ))
  (println synset))

(defn -main
	"Main driver function."
  [& args]
      (def s (slurp (apply str (first args))))
      (def sentences (get-sentences s))
  		(def tokens (nouns-and-verbs (pos-tag (tokenize s))))
   		(def counts (count tokens))
  		(loop [i 0]
  			(when (< i counts)
  				(def word (stemmers.core/stems ((first tokens) 0)))
  				(def postag (postagger ((first tokens) 1)))
  				(println (first tokens))
          (println (first word))
  				(println postag)
  				(def wordnetWord (word-net (first word)))
          (if (not (empty? wordnetWord)) (def wordnetWordID (.getSynsetID (:id (first wordnetWord)))))
          (def wordBasket (hash-map :word (:lemma (first wordnetWord)) :id wordnetWordID))
          (println (wordBasket :id))
          (if (not (empty? wordnetWord)) (println (:lemma (first wordnetWord))))
  				;(if (not (= wordnetWord nil)) (println (:lemma wordnetWord)))
          (if (not (empty? wordnetWord)) (synsets wordnetWord))
  				;(if (not (= synsetWords nil)) (println (map :lemma synsetWords)))
          (println counts)
          (println "<------------------------------------------------>")
  				(def tokens (rest tokens))
  				(recur (inc i)))))

